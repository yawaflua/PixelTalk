package git.yawaflua.tech;

import git.yawaflua.tech.command.ReportCommand;
import git.yawaflua.tech.database.DatabaseManager;
import git.yawaflua.tech.filter.ProfanityFilter;
import git.yawaflua.tech.listener.ChatListener;
import git.yawaflua.tech.listener.PlayerJoinListener;
import git.yawaflua.tech.listener.PlayerQuitListener;
import git.yawaflua.tech.listener.PvPListener;
import git.yawaflua.tech.questionnaire.QuestionnaireManager;
import git.yawaflua.tech.score.ScoreManager;
import git.yawaflua.tech.tab.TabManager;
import git.yawaflua.tech.voice.VoiceIntegration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class pixeltalk extends JavaPlugin {

    private DatabaseManager databaseManager;
    private ScoreManager scoreManager;
    private VoiceIntegration voiceIntegration;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // 1. Initialize Database
        try {
            databaseManager = new DatabaseManager(getLogger(), getConfig());
        } catch (Exception e) {
            getLogger().severe("Failed to connect to MongoDB! Disabling plugin.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 2. Initialize Managers
        scoreManager = new ScoreManager(databaseManager, getConfig());
        QuestionnaireManager questionnaireManager = new QuestionnaireManager(databaseManager);
        TabManager tabManager = new TabManager(this, scoreManager);
        ProfanityFilter profanityFilter = new ProfanityFilter(getConfig());

        // 3. Optional Voice Integration
        if (Bukkit.getPluginManager().isPluginEnabled("PlasmoVoice")) {
            getLogger().info("PlasmoVoice found! Enabling voice integration.");
            try {
                voiceIntegration = new VoiceIntegration(this, scoreManager, getConfig());
                voiceIntegration.start();
            } catch (NoClassDefFoundError | Exception e) {
                getLogger().warning("Failed to hook into PlasmoVoice API. Voice integration disabled.");
            }
        }

        // 4. Register Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(scoreManager, questionnaireManager, profanityFilter, getConfig()), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(scoreManager, questionnaireManager, tabManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(scoreManager), this);
        getServer().getPluginManager().registerEvents(new PvPListener(scoreManager, getConfig()), this);

        // 5. Register Commands
        this.getLifecycleManager().registerEventHandler(io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents.COMMANDS, event -> {
            event.registrar().register("report", new ReportCommand(databaseManager, getConfig(), voiceIntegration));
        });

        getLogger().info("PixelTalk enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (scoreManager != null) {
            scoreManager.saveAll();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("PixelTalk disabled.");
    }
}

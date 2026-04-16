package git.yawaflua.tech.listener;

import git.yawaflua.tech.model.PlayerData;
import git.yawaflua.tech.questionnaire.QuestionnaireManager;
import git.yawaflua.tech.score.ScoreManager;
import git.yawaflua.tech.tab.TabManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final ScoreManager scoreManager;
    private final QuestionnaireManager questionnaireManager;
    private final TabManager tabManager;

    public PlayerJoinListener(ScoreManager scoreManager, QuestionnaireManager questionnaireManager, TabManager tabManager) {
        this.scoreManager = scoreManager;
        this.questionnaireManager = questionnaireManager;
        this.tabManager = tabManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // 1. Load player data into cache
        scoreManager.loadPlayer(uuid);
        
        // 2. Check registration status
        PlayerData data = scoreManager.getPlayerData(uuid);
        if (data != null && !data.isRegistered()) {
            questionnaireManager.startQuestionnaire(player);
        }

        // 3. Update their tab
        tabManager.updatePlayerTab(player);
    }
}

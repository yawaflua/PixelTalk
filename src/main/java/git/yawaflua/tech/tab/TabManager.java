package git.yawaflua.tech.tab;

import git.yawaflua.tech.model.PlayerData;
import git.yawaflua.tech.score.ScoreManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TabManager {

    private final ScoreManager scoreManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public TabManager(Plugin plugin, ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAll();
            }
        }.runTaskTimer(plugin, 20L, 100L);
    }

    public void updateAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerTab(player);
        }
    }

    public void updatePlayerTab(Player player) {
        PlayerData data = scoreManager.getPlayerData(player.getUniqueId());
        if (data != null && data.isRegistered()) {
            String format = String.format("<white>%s</white> <gray>|</gray> <aqua>🌍 %s</aqua> <gray>|</gray> <gold>🎯 %s</gold> <gray>|</gray> <green>📅 %d</green> <gray>|</gray> <yellow>⭐ %.1f</yellow>",
                    player.getName(), data.getLanguage(), data.getInterests(), data.getAge(), data.getPoints());
            player.playerListName(miniMessage.deserialize(format));
        } else {
            player.playerListName(miniMessage.deserialize("<gray>" + player.getName() + " (Заполняет анкету)</gray>"));
        }
    }
}

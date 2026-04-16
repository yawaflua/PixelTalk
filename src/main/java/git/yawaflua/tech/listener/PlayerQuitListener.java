package git.yawaflua.tech.listener;

import git.yawaflua.tech.score.ScoreManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final ScoreManager scoreManager;

    public PlayerQuitListener(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        scoreManager.unloadPlayer(event.getPlayer().getUniqueId());
    }
}

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
        // Save to DB and remove from cache
        scoreManager.unloadPlayer(event.getPlayer().getUniqueId());
    }
}

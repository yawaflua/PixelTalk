package git.yawaflua.tech.listener;

import git.yawaflua.tech.messages.Messages;
import git.yawaflua.tech.score.ScoreManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PvPListener implements Listener {

    private final ScoreManager scoreManager;
    private final long cooldownMs;

    private final Map<UUID, Long> lastHitTime = new HashMap<>();

    public PvPListener(ScoreManager scoreManager, FileConfiguration config) {
        this.scoreManager = scoreManager;
        this.cooldownMs = config.getLong("pvp.cooldown-seconds", 1) * 1000L;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();

            UUID damagerId = damager.getUniqueId();

            long now = System.currentTimeMillis();
            long lastHit = lastHitTime.getOrDefault(damagerId, 0L);
            double damage = Math.round(event.getDamage() * 10) / 10.0;
            if (now - lastHit >= cooldownMs) {
                lastHitTime.put(damagerId, now);
                scoreManager.removePoints(damagerId, damage);

                String warnMsg = Messages.PVP_WARNING.replace("<amount>", String.valueOf(damage));
                damager.sendMessage(Messages.parse(warnMsg));

                damager.sendActionBar(
                        Messages.parse(Messages.POINTS_REMOVED.replace("<amount>", String.valueOf(damage))));

                if (scoreManager.getPlayerData(damagerId).getPoints() <= 0) {
                    damager.ban(Messages.PVP_BAN, Date.from(Instant.now().plus(7, ChronoUnit.DAYS)),
                            "PvP points reached 0");
                }
            }
        }
    }
}

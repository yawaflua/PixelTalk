package git.yawaflua.tech.listener;

import git.yawaflua.tech.messages.Messages;
import git.yawaflua.tech.score.ScoreManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemShareListener implements Listener {

    private final ScoreManager scoreManager;
    private final Set<Material> valuableItems = new HashSet<>();
    private final double pointsPerShare;
    private final long cooldownMs;

    // Key: "dropper_uuid:picker_uuid", Value: last reward timestamp
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    // Key: Item entity UUID -> dropper player UUID
    private final Map<UUID, UUID> droppedByMap = new ConcurrentHashMap<>();

    public ItemShareListener(ScoreManager scoreManager, FileConfiguration config) {
        this.scoreManager = scoreManager;
        this.pointsPerShare = config.getDouble("sharing.points-per-share", 3.0);
        this.cooldownMs = config.getLong("sharing.cooldown-seconds", 300) * 1000L;

        List<String> items = config.getStringList("sharing.valuable-items");
        for (String itemName : items) {
            try {
                valuableItems.add(Material.valueOf(itemName.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // Invalid material name in config, skip
            }
        }
    }

    /**
     * Track when a player drops an item so we know who the original dropper is.
     */
    @EventHandler
    public void onPlayerDropItem(org.bukkit.event.player.PlayerDropItemEvent event) {
        Player dropper = event.getPlayer();
        Item itemEntity = event.getItemDrop();
        Material mat = itemEntity.getItemStack().getType();

        if (valuableItems.contains(mat)) {
            droppedByMap.put(itemEntity.getUniqueId(), dropper.getUniqueId());
        }
    }

    /**
     * When a player picks up an item dropped by another player, reward the dropper with points.
     */
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player picker)) {
            return;
        }

        Item itemEntity = event.getItem();
        Material mat = itemEntity.getItemStack().getType();

        if (!valuableItems.contains(mat)) {
            return;
        }

        UUID dropperUuid = droppedByMap.remove(itemEntity.getUniqueId());
        if (dropperUuid == null) {
            return; // Not tracked (e.g. world-generated drop)
        }

        // Don't reward picking up your own items
        if (dropperUuid.equals(picker.getUniqueId())) {
            return;
        }

        // Check cooldown between this pair
        String cooldownKey = dropperUuid.toString() + ":" + picker.getUniqueId().toString();
        long now = System.currentTimeMillis();
        long lastReward = cooldowns.getOrDefault(cooldownKey, 0L);

        if (now - lastReward < cooldownMs) {
            return;
        }

        cooldowns.put(cooldownKey, now);
        scoreManager.addPoints(dropperUuid, pointsPerShare);

        // Notify the dropper if they're online
        Player dropper = org.bukkit.Bukkit.getPlayer(dropperUuid);
        if (dropper != null && dropper.isOnline()) {
            String msg = Messages.ITEM_SHARE_REWARD
                    .replace("<amount>", String.valueOf(pointsPerShare))
                    .replace("<item>", formatMaterialName(mat))
                    .replace("<player>", picker.getName());
            dropper.sendMessage(Messages.parse(msg));
        }
    }

    private String formatMaterialName(Material mat) {
        return mat.name().replace('_', ' ').toLowerCase();
    }
}

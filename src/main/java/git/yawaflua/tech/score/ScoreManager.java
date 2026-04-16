package git.yawaflua.tech.score;

import git.yawaflua.tech.database.DatabaseManager;
import git.yawaflua.tech.model.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ScoreManager {
    private final DatabaseManager databaseManager;
    private final ConcurrentMap<UUID, PlayerData> cache = new ConcurrentHashMap<>();
    private final double minScore;

    public ScoreManager(DatabaseManager databaseManager, FileConfiguration config) {
        this.databaseManager = databaseManager;
        this.minScore = config.getDouble("scores.min-score", 0.0);
    }

    public void loadPlayer(UUID uuid) {
        PlayerData data = databaseManager.getPlayer(uuid);
        if (data == null) {
            data = new PlayerData(uuid, "", "English", "", 0, 0.0, false, System.currentTimeMillis());
        }
        cache.put(uuid, data);
    }

    public void unloadPlayer(UUID uuid) {
        PlayerData data = cache.remove(uuid);
        if (data != null) {
            databaseManager.savePlayer(data);
        }
    }

    public PlayerData getPlayerData(UUID uuid) {
        return cache.get(uuid);
    }

    public double getPoints(UUID uuid) {
        PlayerData data = cache.get(uuid);
        return data != null ? data.getPoints() : 0.0;
    }

    public void addPoints(UUID uuid, double amount) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            data.setPoints(data.getPoints() + amount);
        }
    }

    public void removePoints(UUID uuid, double amount) {
        PlayerData data = cache.get(uuid);
        if (data != null) {
            double newPoints = Math.max(minScore, data.getPoints() - amount);
            data.setPoints(newPoints);
        }
    }

    public void saveAll() {
        for (PlayerData data : cache.values()) {
            databaseManager.savePlayer(data);
        }
    }
}

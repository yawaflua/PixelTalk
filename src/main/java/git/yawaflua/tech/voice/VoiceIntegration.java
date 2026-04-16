package git.yawaflua.tech.voice;

import git.yawaflua.tech.messages.Messages;
import git.yawaflua.tech.score.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import su.plo.voice.api.server.PlasmoVoiceServer;
import su.plo.voice.api.server.player.VoiceServerPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class VoiceIntegration {

    private final ScoreManager scoreManager;
    private final double voiceRewardPoints;
    private final double voiceDistance;
    private final Plugin plugin;
    private final Logger logger;
    
    // UUID -> Ticks speaking
    private final Map<UUID, Integer> speakingTime = new HashMap<>();

    public VoiceIntegration(Plugin plugin, ScoreManager scoreManager, FileConfiguration config) {
        this.plugin = plugin;
        this.scoreManager = scoreManager;
        this.logger = plugin.getLogger();
        
        this.voiceRewardPoints = config.getDouble("scores.voice-chat-near-player-per-20s", 1.0);
        this.voiceDistance = config.getDouble("scores.voice-chat-near-distance", 30.0);
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkSpeakingPlayers();
            }
        }.runTaskTimer(plugin, 20L, 20L);
        
        logger.info("PlasmoVoice integration initialized.");
    }

    private void checkSpeakingPlayers() {
    }

    public void dumpAudio(UUID targetId, String reason) {
        Player target = Bukkit.getPlayer(targetId);
        if (target == null) return;

        File dumpFolder = new File(plugin.getDataFolder(), "audio_dumps");
        if (!dumpFolder.exists()) {
            dumpFolder.mkdirs();
        }

        String fileName = target.getName() + "_" + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".txt";
        File dumpFile = new File(dumpFolder, fileName);
        
        try (FileWriter writer = new FileWriter(dumpFile)) {
            writer.write("Audio dump for player: " + target.getName() + "\n");
            writer.write("Reason: " + reason + "\n");
            writer.write("Time: " + new Date().toString() + "\n");
            writer.write("\n");
            writer.write("[WARNING] Fully fledged audio recording requires creating an audio buffer on the proxy/server (PlasmoVoice doesn't provide historical packets natively).\n");
            writer.write("This file is a placeholder to demonstrate report generation.\n");
            
            writer.write("\nPlayers within " + voiceDistance + " blocks radius:\n");
            for (Entity entity : target.getNearbyEntities(voiceDistance, voiceDistance, voiceDistance)) {
                if (entity instanceof Player) {
                    writer.write("- " + entity.getName() + "\n");
                }
            }
            
        } catch (IOException e) {
            logger.warning("Failed to create audio dump: " + e.getMessage());
        }
    }
}

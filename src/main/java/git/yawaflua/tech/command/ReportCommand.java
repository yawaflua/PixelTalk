package git.yawaflua.tech.command;

import git.yawaflua.tech.database.DatabaseManager;
import git.yawaflua.tech.messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportCommand implements BasicCommand {

    private final DatabaseManager databaseManager;
    private final git.yawaflua.tech.voice.VoiceIntegration voiceIntegration;
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final long cooldownMs;

    public ReportCommand(DatabaseManager databaseManager, FileConfiguration config, git.yawaflua.tech.voice.VoiceIntegration voiceIntegration) {
        this.databaseManager = databaseManager;
        this.voiceIntegration = voiceIntegration;
        this.cooldownMs = config.getLong("report.cooldown-seconds", 60) * 1000L;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.parse(Messages.ONLY_PLAYERS));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(Messages.parse(Messages.REPORT_USAGE));
            return;
        }

        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();
        long lastReport = cooldowns.getOrDefault(uuid, 0L);

        if (now - lastReport < cooldownMs) {
            long remaining = (cooldownMs - (now - lastReport)) / 1000;
            player.sendMessage(Messages.parse(Messages.REPORT_COOLDOWN.replace("<time>", String.valueOf(remaining))));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Messages.parse(Messages.REPORT_PLAYER_NOT_FOUND));
            return;
        }

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
        }
        String reason = reasonBuilder.toString().trim();

        databaseManager.logReport(uuid, target.getUniqueId(), reason);
        cooldowns.put(uuid, now);
        
        if (voiceIntegration != null) {
            voiceIntegration.dumpAudio(target.getUniqueId(), reason);
        }

        String opMsg = Messages.REPORT_NOTIFICATION_OP
                .replace("<reporter>", player.getName())
                .replace("<target>", target.getName())
                .replace("<reason>", reason);

        for (Player op : Bukkit.getOnlinePlayers()) {
            if (op.isOp() || op.hasPermission("pixeltalk.moderator")) {
                op.sendMessage(Messages.parse(opMsg));
                op.playSound(op.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            }
        }
        
        player.sendMessage(Messages.parse(Messages.REPORT_SENT));

        return;
    }
}

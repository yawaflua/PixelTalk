package git.yawaflua.tech.command;

import git.yawaflua.tech.database.DatabaseManager;
import git.yawaflua.tech.messages.Messages;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PtCommand implements BasicCommand {

    private final DatabaseManager databaseManager;

    public PtCommand(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {
        CommandSender sender = stack.getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.parse(Messages.ONLY_PLAYERS));
            return;
        }

        System.out.println(String.join(",", args));
        if (args.length == 3 && args[0].equalsIgnoreCase("web") && args[1].equalsIgnoreCase("auth")) {
            String code = args[2];
            System.out.println(code);
            boolean success = databaseManager.resolveWebAuth(code, player.getUniqueId());
            System.out.println(success);
            if (success) {
                player.sendMessage(Messages.parse(Messages.WEB_AUTH_SUCCESS));
            } else {
                player.sendMessage(Messages.parse(Messages.WEB_AUTH_FAILED));
            }
            return;
        }

        player.sendMessage(Messages.parse(Messages.WEB_AUTH_USAGE));
    }
}

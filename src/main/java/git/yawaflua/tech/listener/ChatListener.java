package git.yawaflua.tech.listener;

import git.yawaflua.tech.filter.ProfanityFilter;
import git.yawaflua.tech.messages.Messages;
import git.yawaflua.tech.model.PlayerData;
import git.yawaflua.tech.questionnaire.QuestionnaireManager;
import git.yawaflua.tech.score.ScoreManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ChatListener implements Listener {

    private final ScoreManager scoreManager;
    private final QuestionnaireManager questionnaireManager;
    private final ProfanityFilter profanityFilter;
    
    private final double chatMessagePoints;
    private final double profanityPenaltyPoints;

    public ChatListener(ScoreManager scoreManager, QuestionnaireManager questionnaireManager, ProfanityFilter profanityFilter, FileConfiguration config) {
        this.scoreManager = scoreManager;
        this.questionnaireManager = questionnaireManager;
        this.profanityFilter = profanityFilter;
        
        this.chatMessagePoints = config.getDouble("scores.chat-message", 0.5);
        this.profanityPenaltyPoints = Math.abs(config.getDouble("scores.profanity-chat", -0.5));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        String plainMessage = PlainTextComponentSerializer.plainText().serialize(event.message());

        // Check if answering questionnaire
        if (questionnaireManager.isAnswering(uuid)) {
            event.setCancelled(true);
            PlayerData data = scoreManager.getPlayerData(uuid);
            if (data != null) {
                questionnaireManager.handleAnswer(player, plainMessage, data);
            }
            return;
        }

        boolean hasProfanity = profanityFilter.containsProfanity(plainMessage);

        if (hasProfanity) {
            String censored = profanityFilter.censorMessage(plainMessage);
            event.message(Component.text(censored));
            
            scoreManager.removePoints(uuid, profanityPenaltyPoints);
            String warnMsg = Messages.PROFANITY_WARNING.replace("<amount>", String.valueOf(profanityPenaltyPoints));
            player.sendMessage(Messages.parse(warnMsg));
            
            player.sendActionBar(Messages.parse(Messages.POINTS_REMOVED.replace("<amount>", String.valueOf(profanityPenaltyPoints))));
        } else {
            scoreManager.addPoints(uuid, chatMessagePoints);
            player.sendActionBar(Messages.parse(Messages.POINTS_ADDED.replace("<amount>", String.valueOf(chatMessagePoints))));
        }
    }
}

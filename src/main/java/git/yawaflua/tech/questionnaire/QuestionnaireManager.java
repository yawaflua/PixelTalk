package git.yawaflua.tech.questionnaire;

import git.yawaflua.tech.database.DatabaseManager;
import git.yawaflua.tech.messages.Messages;
import git.yawaflua.tech.model.PlayerData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuestionnaireManager {

    private final DatabaseManager databaseManager;
    private final ConcurrentMap<UUID, QuestionnaireState> activeQuestionnaires = new ConcurrentHashMap<>();

    public enum State {
        LANGUAGE,
        INTERESTS,
        AGE
    }

    public static class QuestionnaireState {
        public State currentState = State.LANGUAGE;
    }

    public QuestionnaireManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void startQuestionnaire(Player player) {
        activeQuestionnaires.put(player.getUniqueId(), new QuestionnaireState());
        player.sendMessage(Messages.parse(Messages.REGISTRATION_START));
        player.sendMessage(Messages.parse(Messages.QUESTION_LANGUAGE));
    }

    public boolean isAnswering(UUID uuid) {
        return activeQuestionnaires.containsKey(uuid);
    }

    public void handleAnswer(Player player, String answer, PlayerData playerData) {
        QuestionnaireState state = activeQuestionnaires.get(player.getUniqueId());
        if (state == null)
            return;

        switch (state.currentState) {
            case LANGUAGE:
                playerData.setLanguage(answer.trim());
                state.currentState = State.INTERESTS;
                player.sendMessage(Messages.parse(Messages.QUESTION_INTERESTS));
                break;
            case INTERESTS:
                playerData.setInterests(answer.trim());
                state.currentState = State.AGE;
                player.sendMessage(Messages.parse(Messages.QUESTION_AGE));
                break;
            case AGE:
                try {
                    int age = Integer.parseInt(answer.trim());
                    if (age >= 6 && age <= 18) {
                        playerData.setAge(age);
                        playerData.setRegistered(true);
                        activeQuestionnaires.remove(player.getUniqueId());
                        playerData.setName(player.getName());
                        databaseManager.savePlayer(playerData);
                        player.sendMessage(Messages.parse(Messages.REGISTRATION_COMPLETE));
                    } else {
                        player.sendMessage(Messages.parse(Messages.ERROR_AGE_RANGE));
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(Messages.parse(Messages.ERROR_AGE_FORMAT));
                }
                break;
        }
    }
}

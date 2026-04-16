package git.yawaflua.tech.filter;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.regex.Pattern;

public class ProfanityFilter {

    private final List<String> badWords;

    public ProfanityFilter(FileConfiguration config) {
        this.badWords = config.getStringList("profanity.words");
    }

    public boolean containsProfanity(String message) {
        if (message == null || message.isEmpty()) return false;
        
        String lowerCaseMsg = message.toLowerCase();
        
        // Simple word checking.
        // For a more advanced filter, regex boundaries should be used.
        for (String word : badWords) {
            if (lowerCaseMsg.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String censorMessage(String message) {
        if (message == null || message.isEmpty()) return message;
        
        String censored = message;
        for (String word : badWords) {
            // Case-insensitive replacement
            censored = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(censored).replaceAll("***");
        }
        return censored;
    }
}

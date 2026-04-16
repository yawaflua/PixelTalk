package git.yawaflua.tech.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Messages {

        private static final MiniMessage MM = MiniMessage.miniMessage();

        public static Component parse(String text) {
                return MM.deserialize(text);
        }

        public static final String PREFIX = "<gray>[<color:#ffaa00>PixelTalk</color>]</gray> ";
        public static final String NO_PERMISSION = PREFIX + "<red>U cant use this command.</red>";
        public static final String ONLY_PLAYERS = PREFIX + "<red>This command is only for players.</red>";

        public static final String REGISTRATION_START = PREFIX + "<green>Welcome! Let's fill your profile.</green>";
        public static final String QUESTION_LANGUAGE = PREFIX
                        + "<aqua>Step 1:</aqua> What language do you prefer? Write it in chat (for example: Русский, English, עברית, العربية).";
        public static final String QUESTION_INTERESTS = PREFIX
                        + "<aqua>Step 2:</aqua> Tell us about your interests (favorite games, hobbies, etc.).";
        public static final String QUESTION_AGE = PREFIX
                        + "<aqua>Step 3:</aqua> Enter your age (a number between 6 and 18).";

        public static final String ERROR_AGE_FORMAT = PREFIX + "<red>Please enter a valid number.</red>";
        public static final String ERROR_AGE_RANGE = PREFIX + "<red>Age must be between 6 and 18.</red>";

        public static final String REGISTRATION_COMPLETE = PREFIX
                        + "<green>Thank you! Your profile has been saved. Now you can communicate.</green>";

        public static final String PROFANITY_WARNING = PREFIX
                        + "<red>Warning! Profanity is prohibited. You have been deducted <amount> points.</red>";
        public static final String PVP_WARNING = PREFIX
                        + "<red>Warning! Attacking other players is prohibited. You have been deducted <amount> points. If you`re points falls to 0 you are take ban</red>";
        public static final String PVP_BAN = "You have been banned for attacking other players.";
        public static final String POINTS_ADDED = "<green>+<amount> points</green>";
        public static final String POINTS_REMOVED = "<red>-<amount> points</red>";

        public static final String REPORT_USAGE = PREFIX + "<yellow>Usage: /report <player> <reason></yellow>";
        public static final String REPORT_PLAYER_NOT_FOUND = PREFIX + "<red>Player not found.</red>";
        public static final String REPORT_SENT = PREFIX + "<green>Report sent successfully.</green>";
        public static final String REPORT_COOLDOWN = PREFIX
                        + "<red>Wait <time> sec. before sending another report.</red>";
        public static final String REPORT_NOTIFICATION_OP = "<red>[REPORT]</red> <yellow><reporter></yellow> reported <yellow><target></yellow>. Reason: <gray><reason></gray>";

        public static final String WEB_AUTH_SUCCESS = PREFIX + "<green>Website authentication successful!</green>";
        public static final String WEB_AUTH_FAILED = PREFIX + "<red>Invalid or already used authentication code.</red>";
        public static final String WEB_AUTH_USAGE = PREFIX + "<yellow>Usage: /pt web auth <code></yellow>";

        public static final String ITEM_SHARE_REWARD = PREFIX + "<green>+<amount> points for sharing <item> with <player>!</green>";
}

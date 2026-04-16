package git.yawaflua.tech.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import git.yawaflua.tech.model.PlayerData;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;
import java.util.logging.Logger;

public class DatabaseManager {

    private final Logger logger;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> playersCollection;
    private MongoCollection<Document> reportsCollection;
    private MongoCollection<Document> webAuthCollection;

    public DatabaseManager(Logger logger, FileConfiguration config) {
        this.logger = logger;
        connect(config);
    }

    private void connect(FileConfiguration config) {
        String host = config.getString("database.host", "localhost");
        int port = config.getInt("database.port", 27017);
        String dbName = config.getString("database.database", "pixeltalk");

        String username = config.getString("database.username", "");
        String password = config.getString("database.password", "");

        String uri = "mongodb://";
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            uri += username + ":" + password + "@";
        }
        uri += host + ":" + port;

        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase(dbName);
        playersCollection = database.getCollection("players");
        reportsCollection = database.getCollection("reports");
        webAuthCollection = database.getCollection("auth_requests");

        logger.info("Connected to MongoDB -> " + dbName);
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("Disconnected from MongoDB.");
        }
    }

    public PlayerData getPlayer(UUID uuid) {
        Document doc = playersCollection.find(Filters.eq("uuid", uuid.toString())).first();
        if (doc == null) {
            return null;
        }

        return new PlayerData(
                uuid,
                doc.getString("name"),
                doc.getString("language"),
                doc.getString("interests"),
                doc.getInteger("age", 0),
                doc.getDouble("points"),
                doc.getBoolean("registered", false),
                doc.getLong("firstJoin"));
    }

    public void savePlayer(PlayerData player) {
        Document doc = new Document("uuid", player.getUuid().toString())
                .append("name", player.getName())
                .append("language", player.getLanguage())
                .append("interests", player.getInterests())
                .append("age", player.getAge())
                .append("points", player.getPoints())
                .append("registered", player.isRegistered())
                .append("firstJoin", player.getFirstJoin());

        playersCollection.replaceOne(
                Filters.eq("uuid", player.getUuid().toString()),
                doc,
                new ReplaceOptions().upsert(true));
    }

    public void logReport(UUID reporter, UUID target, String reason) {
        Document report = new Document("reporter", reporter.toString())
                .append("target", target.toString())
                .append("reason", reason)
                .append("timestamp", System.currentTimeMillis())
                .append("resolved", false);
        reportsCollection.insertOne(report);
    }

    public boolean resolveWebAuth(String code, UUID playerUuid) {
        UpdateResult result = webAuthCollection.updateOne(
                Filters.and(
                        Filters.eq("code", code),
                        Filters.eq("resolved", false)),
                Updates.combine(
                        Updates.set("uuid", playerUuid.toString()),
                        Updates.set("resolved", true)));
        return result.getModifiedCount() > 0;
    }
}

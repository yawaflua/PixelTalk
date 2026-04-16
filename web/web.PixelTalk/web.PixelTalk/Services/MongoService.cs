using MongoDB.Bson;
using MongoDB.Driver;
using web.PixelTalk.Models;

namespace web.PixelTalk.Services;

public class MongoService
{
    private readonly IMongoDatabase _database;

    public IMongoCollection<PlayerData> Players => _database.GetCollection<PlayerData>("players");
    public IMongoCollection<Report> Reports => _database.GetCollection<Report>("reports");
    public IMongoCollection<AuthRequest> AuthRequests => _database.GetCollection<AuthRequest>("auth_requests");
    public IMongoCollection<StoreItem> StoreItems => _database.GetCollection<StoreItem>("store_items");
    public IMongoCollection<PurchaseLog> PurchaseLogs => _database.GetCollection<PurchaseLog>("purchases_log");

    public MongoService(IMongoClient client, IConfiguration config)
    {
        _database = client.GetDatabase(config.GetValue<string>("MongoDatabaseName") ?? "pixeltalk");
    }

    public async Task<bool> DeductPointsAsync(string uuid, int amount)
    {
        var filter = Builders<PlayerData>.Filter.And(
            Builders<PlayerData>.Filter.Eq(x => x.Uuid, uuid),
            Builders<PlayerData>.Filter.Gte(x => x.Points, amount)
        );
        var update = Builders<PlayerData>.Update.Inc(x => x.Points, -amount);
        
        var result = await Players.UpdateOneAsync(filter, update);
        return result.ModifiedCount > 0;
    }
}


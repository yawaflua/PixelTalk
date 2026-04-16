using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace web.PixelTalk.Models;

public class PlayerData
{
    [BsonId]
    [BsonElement("_id")]
    public ObjectId Id { get; set; }
    [BsonElement("uuid")]
    public string Uuid { get; set; } = string.Empty;
    [BsonElement("language")]
    public string Language { get; set; } = string.Empty;

    [BsonElement("age")] public int Age { get; set; } = 6;
    [BsonElement("registered")] public bool Registred { get; set; }
    [BsonElement("firstJoin")] public ulong FirstJoinTimestamp { get; set; }
    [BsonElement("interests")] public string Interests { get; set; }
    [BsonElement("name")]
    public string Nickname { get; set; } = string.Empty;
    [BsonElement("points")]
    public double Points { get; set; }
    [BsonElement("role")]
    public string Role { get; set; } = "player";
}

public class Report
{
    [BsonId]
    public ObjectId Id { get; set; }
    [BsonElement("reporter")]
    public string ReporterUuid { get; set; } = string.Empty;
    [BsonElement("target")]
    public string TargetUuid { get; set; } = string.Empty;
    [BsonElement("reason")]
    public string Reason { get; set; } = string.Empty;
    [BsonElement("timestamp")]
    public long Timestamp { get; set; }
    [BsonElement("resolved")]
    public bool Resolved { get; set; }
}

public class AuthRequest
{
    [BsonId]
    public ObjectId Id { get; set; }
    [BsonElement("code")]
    public string Code { get; set; } = string.Empty;
    [BsonElement("resolved")]
    public bool Resolved { get; set; }
    [BsonElement("uuid")]
    public string? Uuid { get; set; }
    [BsonElement("timestamp")]
    public long Timestamp { get; set; }
}

public class StoreItem
{
    [BsonId]
    public ObjectId Id { get; set; }
    [BsonElement("name")]
    public string Name { get; set; } = string.Empty;
    [BsonElement("description")]
    public string Description { get; set; } = string.Empty;
    [BsonElement("cost")]
    public int Cost { get; set; }
}

public class PurchaseLog
{
    [BsonId]
    public ObjectId Id { get; set; }
    [BsonElement("uuid")]
    public string Uuid { get; set; } = string.Empty;
    [BsonElement("itemId")]
    public ObjectId ItemId { get; set; }
    [BsonElement("itemName")]
    public string ItemName { get; set; } = string.Empty;
    [BsonElement("cost")]
    public int Cost { get; set; }
    [BsonElement("timestamp")]
    public long Timestamp { get; set; }
}

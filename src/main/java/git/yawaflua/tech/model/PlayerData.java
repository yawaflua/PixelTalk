package git.yawaflua.tech.model;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private String name;
    private String language;
    private String interests;
    private int age;
    private double points;
    private boolean registered;
    private long firstJoin;

    public PlayerData() {
    }

    public PlayerData(UUID uuid, String name, String language, String interests, int age, double points, boolean registered, long firstJoin) {
        this.uuid = uuid;
        this.name = name;
        this.language = language;
        this.interests = interests;
        this.age = age;
        this.points = points;
        this.registered = registered;
        this.firstJoin = firstJoin;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public long getFirstJoin() {
        return firstJoin;
    }

    public void setFirstJoin(long firstJoin) {
        this.firstJoin = firstJoin;
    }
}

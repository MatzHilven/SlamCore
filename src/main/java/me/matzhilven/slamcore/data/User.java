package me.matzhilven.slamcore.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity(value = "Players", noClassnameStored = true)
public class User {

    @Id
    private String uuid;

    @Indexed
    private String username;

    private long gems;

    public User() {

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getGems() {
        return gems;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }
}

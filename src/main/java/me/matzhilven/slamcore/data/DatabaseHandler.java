package me.matzhilven.slamcore.data;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHandler {

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;
    private UserDAO userDAO;

    public DatabaseHandler(String host, int port, String db) {

        ServerAddress addr = new ServerAddress(host, port);
        List<MongoCredential> credentials = new ArrayList<>();
        credentials.add(MongoCredential.createCredential("Prison", "admin", "HkYRYk0fXLAOsQTDzk".toCharArray()));

        mc = new MongoClient(addr, credentials);
        morphia = new Morphia();
        morphia.map(User.class);

        datastore = morphia.createDatastore(mc, db);
        datastore.ensureIndexes();

        userDAO = new UserDAO(User.class, datastore);
    }

    public User getUserByPlayer(Player player) {
        User user = userDAO.findOne("uuid", player.getUniqueId().toString());
        if (user == null) {
            user = new User();
            user.setUsername(player.getName());
            user.setUuid(player.getUniqueId().toString());
            user.setGems(0);
            userDAO.save(user);
        } else {
            if (!user.getUsername().equals(player.getName())) {
                user.setUsername(player.getName());
                userDAO.save(user);
            }
        }
        return user;
    }

    public User getUserByUUID(UUID uuid) {
        return userDAO.findOne("uuid", uuid.toString());
    }

    public void savePlayer(Player player) {
        saveUser(getUserByPlayer(player));
    }

    public void saveUser(User user) {
        userDAO.save(user);
    }

    public void saveUsers(User... users) {
        for (User user : users) {
            userDAO.save(user);
        }
    }

    public List<User> getAllUsers() {
        return userDAO.find().asList();
    }
}

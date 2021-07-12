package me.matzhilven.slamcore.data;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class UserDAO extends BasicDAO<User,String> {
    public UserDAO(Class<User> entityClass, Datastore ds) {
        super(entityClass, ds);
    }
}

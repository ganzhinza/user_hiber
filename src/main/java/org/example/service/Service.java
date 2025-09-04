package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDao;
import org.example.model.User;

import java.util.List;
import java.util.Optional;


public class Service {
    private static final Logger logger = LogManager.getLogger(org.example.service.Service.class);
    private final UserDao userDao;

    public Service(UserDao userDao){
        this.userDao = userDao;
    }

    public User createUser(User user){
        Long id = userDao.saveUser(user);
        System.out.println(id);
        if (id != null) {
            logger.info("User created with ID: {}", id);
            user.setId(id);
            return user;
        } else {
            logger.error("Failed to create user");
            return null;
        }
    }

    public List<User>  showAllUsers(){
        return userDao.getAllUsers();
    }

    public Optional<User> findUserById(Long id){
        return userDao.getUserById(id);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void deleteUser(long id){
        userDao.deleteUser(id);
    }
}

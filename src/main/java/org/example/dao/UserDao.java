package org.example.dao;

import org.example.util.HibernateUtil;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDao {
    private static final Logger logger = LogManager.getLogger(UserDao.class);

    public Long saveUser(User user){
        Transaction transaction = null;
        Session session = null;
        try{
            logger.debug("Start of user creation: {}", user);
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.persist(user);
            transaction.commit();
            logger.info("User saved, id={}", user.getId());
            return user.getId();
        }catch (Exception e){
            if(transaction != null) {
                try {
                    transaction.rollback();
                    logger.warn("Transaction rollback. Saving user error: {}", e.getMessage());
                } catch (Exception rollbackEx) {
                    logger.error("Rollback error: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            logger.error("User saving error", e);
            return null;
        }finally {
            if(session != null){
                session.close();
                logger.debug("Session closed after user saving");
            }
        }
    }

    public Optional<User> getUserById(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            logger.debug("Getting user with id={}",id);
            User user = session.find(User.class, id);
            if(user != null){
                logger.info("User found: {}", user);
            }else{
                logger.info("User with id = {} not found", id);
            }
            return  Optional.ofNullable(user);
        }catch (Exception e){
            logger.error("Getting user by id = {} error", id, e);
            return Optional.empty();
        }
    }

    public List<User> getAllUsers(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            logger.debug("Getting all users");
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            logger.info("Got {} users", users.size());
            return users;
        }catch (Exception e){
            logger.error("Getting all users error", e);
            return List.of();
        }
    }

    public void updateUser(User user){
        Transaction transaction = null;
        Session session = null;
        try{
            logger.debug("Updating user: {}", user);
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.merge(user);
            transaction.commit();
            logger.info("User updated: id={}", user.getId());
        }catch (Exception e){
            if(transaction != null){
                try{
                    transaction.rollback();
                    logger.warn("Transaction rollback error in user update: {}", e.getMessage());
                }catch (Exception rollbackEx) {
                    logger.error("Rollback error: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            logger.error("User update error", e);
        }finally {
            if (session != null){
                session.close();
                logger.debug("Session closed after user update");
            }
        }
    }

    public void deleteUser(Long id){
        Transaction transaction = null;
        Session session = null;
        try {
            logger.debug("Deleting user with id={}", id);
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            User user = session.find(User.class, id);
            if(user != null){
                session.remove(user);
                logger.info("User deleted: id={}", id);
            }else{
                logger.info("User with id={} not found for deleting", id);
            }
            transaction.commit();
        }catch (Exception e){
            if(transaction != null){
                try {
                    transaction.rollback();
                    logger.warn("Transaction rollback error in deleteUser: {}", e.getMessage());
                }catch (Exception rollbackEx){
                    logger.error("Rollback error: {}", rollbackEx.getMessage(), rollbackEx);
                }
            }
            logger.error("Deleting user with id={} error",id,e);
        }finally {
            if (session != null){
                session.close();
                logger.debug("Session closed after user deleting");
            }
        }
    }

}

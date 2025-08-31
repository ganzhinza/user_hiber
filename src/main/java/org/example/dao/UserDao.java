package org.example.dao;

import org.example.util.HibernateUtil;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDao {
    public Long saveUser(User user){
        Transaction transaction = null;
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.persist(user);
            transaction.commit();
            return user.getId();
        }catch (Exception e){
            if(transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback error: " + rollbackEx);
                }
            }
            e.printStackTrace();
            return null;
        }finally {
            if(session != null){
                session.close();
            }
        }
    }

    public Optional<User> getUserById(Long id){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            User user = session.find(User.class, id);
            return  Optional.ofNullable(user);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<User> getAllUsers(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<User> query = session.createQuery("FROM User", User.class);
            return query.list();
        }catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

    public void updateUser(User user){
        Transaction transaction = null;
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.merge(user);
            transaction.commit();
        }catch (Exception e){
            if(transaction != null){
                try{
                    transaction.rollback();
                }catch (Exception rollbackEx) {
                    System.err.println("Rollback error: " + rollbackEx.getMessage());
                }
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(Long id){
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            User user = session.find(User.class, id);
            if(user != null){
                session.remove(user);
            }
            transaction.commit();
        }catch (Exception e){
            if(transaction != null){
                try {
                    transaction.rollback();
                }catch (Exception rollbackEx){
                    System.err.println("Rollback error: " + rollbackEx.getMessage());
                }
                transaction.rollback();
            }
            e.printStackTrace();
        }finally {
            if (session != null){
                session.close();
            }
        }
    }

}

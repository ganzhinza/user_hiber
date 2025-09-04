package org.example.dao;

import org.example.model.User;
import org.example.util.HibernateUtil;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UserDaoTest {
    private static UserDao userDao;

    @BeforeAll
    static void  setUp(){
        userDao = new UserDao();
    }

    @AfterAll
    static void shutDown(){
        userDao.getAllUsers().forEach(user ->userDao.deleteUser(user.getId()));
        HibernateUtil.shutdown();
    }

    @BeforeEach
    void clearDatabase(){
        userDao.getAllUsers().forEach(user ->userDao.deleteUser(user.getId()));
    }

    @Test
    void testSaveUser(){
        LocalDateTime t = LocalDateTime.now();
        String name = "John Doe";
        String email = "example@exampl.com";
        int age = 30;
        User user = new User(name, email, 30, t);

        Long userID = userDao.saveUser(user);

        assertNotNull(userID);
        assertTrue(userID > 0);
    }

    @Test
    void testGetUserById(){
        LocalDateTime t = LocalDateTime.now();
        String name = "John Doe";
        String email = "example@exampl.com";
        int age = 30;
        User user = new User(name, email, 30, t);

        Long id = userDao.saveUser(user);
        var userOp = userDao.getUserById(id);


        assertTrue(userOp.isPresent());
        assertEquals(name, userOp.get().getName());
        assertEquals(email, userOp.get().getEmail());
        assertEquals(age, userOp.get().getAge());
        assertEquals(t.truncatedTo(ChronoUnit.SECONDS), userOp.get().getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testGetAllUsers(){
        userDao.saveUser(new User("John Doe", "example1@exampl.com",  30, LocalDateTime.now()));
        userDao.saveUser(new User("Jane Doe", "example2@exampl.com",  25, LocalDateTime.now()));

        assertEquals(2, userDao.getAllUsers().size());
    }


    @Test
    void testUpdateUser(){
        User user = new User("John Doe", "example1@exampl.com",  30, LocalDateTime.now());
        Long userID = userDao.saveUser(user);

        var t = LocalDateTime.now();

        user.setId(userID);
        user.setName("Jane Doe");
        user.setEmail("example2@exmapl.com");
        user.setAge(25);
        user.setCreatedAt(t);

        userDao.updateUser(user);

        var updatedUserOp = userDao.getUserById(userID);

        assertTrue(updatedUserOp.isPresent());
        var updatedUser = updatedUserOp.get();
        assertEquals(updatedUser.getId(), userID);
        assertEquals(updatedUser.getAge(), user.getAge());
        assertEquals(updatedUser.getName(), user.getName());
        assertEquals(updatedUser.getEmail(), user.getEmail());
        assertEquals(t.truncatedTo(ChronoUnit.SECONDS), updatedUser.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}

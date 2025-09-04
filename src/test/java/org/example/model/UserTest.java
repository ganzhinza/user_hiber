package org.example.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testUserCreation(){
        LocalDateTime t = LocalDateTime.now();
        String name = "John Doe";
        String email = "example@exampl.com";
        int age = 30;

        User user = new User(name, email, 30, t);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertEquals(t, user.getCreatedAt());
    }

    @Test
    void testUserSetters(){
        LocalDateTime t = LocalDateTime.now();
        String name = "John Doe";
        String email = "example@exampl.com";
        int age = 30;

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        user.setCreatedAt(t);

        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(age, user.getAge());
        assertEquals(t, user.getCreatedAt());
    }

    @Test
    void testUserToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime t = LocalDateTime.now();
        String name = "John Doe";
        String email = "example@exampl.com";
        int age = 30;
        User user = new User(name, email, 30, t);



        String toString = user.toString();

        assertTrue(toString.contains(name));
        assertTrue(toString.contains(email));
        assertTrue(toString.contains(Integer.toString(age)));
        assertTrue(toString.contains(t.format(formatter)));

    }
}

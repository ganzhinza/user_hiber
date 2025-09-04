package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private Service service;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setAge(30);
        testUser.setEmail("john@example.com");
    }

    @Test
    void testCreateUser_Success() {
        Long expectedId = 1L;
        when(userDao.saveUser(testUser)).thenReturn(expectedId);

        User result = service.createUser(testUser);

        assertNotNull(result);
        assertEquals(expectedId, result.getId());
        assertEquals("John Doe", result.getName());
        verify(userDao, times(1)).saveUser(testUser);
    }

    @Test
    void testCreateUser_Failure() {
        when(userDao.saveUser(testUser)).thenReturn(null);

        User result = service.createUser(testUser);

        assertNull(result);
        verify(userDao, times(1)).saveUser(testUser);
    }

    @Test
    void testShowAllUsers() {
        List<User> expectedUsers = Arrays.asList(testUser, new User());
        when(userDao.getAllUsers()).thenReturn(expectedUsers);


        List<User> result = service.showAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedUsers, result);
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testFindUserById_UserFound() {
        Long userId = 1L;
        when(userDao.getUserById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> result = service.findUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userDao, times(1)).getUserById(userId);
    }

    @Test
    void testFindUserById_UserNotFound() {
        Long userId = 999L;
        when(userDao.getUserById(userId)).thenReturn(Optional.empty());

        Optional<User> result = service.findUserById(userId);

        assertFalse(result.isPresent());
        verify(userDao, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUser() {
        service.updateUser(testUser);

        verify(userDao, times(1)).updateUser(testUser);
    }

    @Test
    void testDeleteUser() {
        long userId = 1L;

        service.deleteUser(userId);

        verify(userDao, times(1)).deleteUser(userId);
    }
}

package org.example;

import org.example.dao.UserDao;
import org.example.model.User;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        System.out.println("=== ДЕМОНСТРАЦИЯ CRUD ОПЕРАЦИЙ ===\n");

        // Test CREATE - создаем нескольких пользователей
        System.out.println("1. СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ");

        User user1 = new User();
        user1.setName("Иван Иванов");
        user1.setEmail("ivan@example.com");
        user1.setAge(25);
        Long user1Id = userDao.saveUser(user1);
        System.out.println("   Создан пользователь 1 с ID: " + user1Id);

        User user2 = new User();
        user2.setName("Мария Петрова");
        user2.setEmail("maria@example.com");
        user2.setAge(30);
        Long user2Id = userDao.saveUser(user2);
        System.out.println("   Создан пользователь 2 с ID: " + user2Id);

        User user3 = new User();
        user3.setName("Алексей Сидоров");
        user3.setEmail("alex@example.com");
        user3.setAge(28);
        Long user3Id = userDao.saveUser(user3);
        System.out.println("   Создан пользователь 3 с ID: " + user3Id);

        // Test READ ALL - показываем всех пользователей
        System.out.println("\n2. ВСЕ ПОЛЬЗОВАТЕЛИ В БАЗЕ:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test READ - читаем одного пользователя
        System.out.println("\n3. ПОИСК ПОЛЬЗОВАТЕЛЯ ПО ID:");
        User foundUser = userDao.getUserById(user2Id).orElse(null);
        System.out.println("   Найден пользователь: " + foundUser);

        // Test UPDATE - обновляем пользователя
        System.out.println("\n4. ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
        if (foundUser != null) {
            foundUser.setEmail("maria.new@example.com");
            foundUser.setAge(31);
            userDao.updateUser(foundUser);
            System.out.println("   Пользователь обновлен: " + foundUser);
        }

        // Test READ ALL - снова показываем всех после обновления
        System.out.println("\n5. ВСЕ ПОЛЬЗОВАТЕЛИ ПОСЛЕ ОБНОВЛЕНИЯ:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test DELETE - удаляем одного пользователя
        System.out.println("\n6. УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
        System.out.println("   Удаляем пользователя с ID: " + user2Id);
        userDao.deleteUser(user2Id);
        System.out.println("   Пользователь удален");

        // Test READ ALL - показываем оставшихся пользователей
        System.out.println("\n7. ОСТАВШИЕСЯ ПОЛЬЗОВАТЕЛИ:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test DELETE - удаляем всех пользователей
        System.out.println("\n8. УДАЛЕНИЕ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ");
        userDao.getAllUsers().forEach(u -> {
            System.out.println("   Удаляем пользователя с ID: " + u.getId());
            userDao.deleteUser(u.getId());
        });
        System.out.println("   Все пользователи удалены");

        // Final check
        System.out.println("\n9. ФИНАЛЬНАЯ ПРОВЕРКА");
        System.out.println("   Пользователей в базе: " + userDao.getAllUsers().size());

        System.out.println("\n=== ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА ===");
    }
}
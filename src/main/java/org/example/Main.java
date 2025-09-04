package org.example;

import org.example.dao.UserDao;
import org.example.handler.Handler;
import org.example.model.User;
import java.util.Scanner;

import org.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        int AppType = -1;
        Scanner scanner = new Scanner(System.in);
        while (AppType < 0 || AppType > 2){
            System.out.println("Choose mode:\n1.Demonstration\n2.Console application\n0.Exit");
            AppType = Integer.parseInt(scanner.nextLine());
        }
        switch (AppType){
            case 1 -> demonstration();
            case 2 -> start();
        }
        logger.info("Application terminated");
    }

    public static void start(){
        Handler handler;
        handler = new Handler(new UserDao());
        logger.info("Starting console application");
        while(true){
            handler.printMenu();
            int op = handler.getIntInput("Choose action: ");


            switch (op){
                case 1 -> handler.createUser();
                case 2 -> handler.showAllUsers();
                case 3 -> handler.findUserById();
                case 4 -> handler.updateUser();
                case 5 -> handler.deleteUser();
                case 0 -> {
                    System.out.println("Exit...");
                    HibernateUtil.shutdown();
                    logger.info("Application stopped");
                    return;
                }
                default -> System.out.println("wrong choice");
            }
        }
    }

    public static void demonstration(){
        UserDao userDao = new UserDao();
        System.out.println("=== CRUD OPERATIONS DEMONSTRATION ===\n");

        // Test CREATE - creating several users
        System.out.println("1. CREATING USERS");

        User user1 = new User();
        user1.setName("Ivan Ivanov");
        user1.setEmail("ivan@example.com");
        user1.setAge(25);
        Long user1Id = userDao.saveUser(user1);
        System.out.println("   Created user 1 with ID: " + user1Id);

        User user2 = new User();
        user2.setName("Maria Petrova");
        user2.setEmail("maria@example.com");
        user2.setAge(30);
        Long user2Id = userDao.saveUser(user2);
        System.out.println("   Created user 2 with ID: " + user2Id);

        User user3 = new User();
        user3.setName("Alexey Sidorov");
        user3.setEmail("alex@example.com");
        user3.setAge(28);
        Long user3Id = userDao.saveUser(user3);
        System.out.println("   Created user 3 with ID: " + user3Id);

        // Test READ ALL - showing all users
        System.out.println("\n2. ALL USERS IN DATABASE:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test READ - reading one user
        System.out.println("\n3. SEARCH USER BY ID:");
        User foundUser = userDao.getUserById(user2Id).orElse(null);
        System.out.println("   Found user: " + foundUser);

        // Test UPDATE - updating user
        System.out.println("\n4. UPDATING USER");
        if (foundUser != null) {
            foundUser.setEmail("maria.new@example.com");
            foundUser.setAge(31);
            userDao.updateUser(foundUser);
            System.out.println("   User updated: " + foundUser);
        }

        // Test READ ALL - showing all after update
        System.out.println("\n5. ALL USERS AFTER UPDATE:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test DELETE - deleting one user
        System.out.println("\n6. DELETING USER");
        System.out.println("   Deleting user with ID: " + user2Id);
        userDao.deleteUser(user2Id);
        System.out.println("   User deleted");

        // Test READ ALL - showing remaining users
        System.out.println("\n7. REMAINING USERS:");
        userDao.getAllUsers().forEach(System.out::println);

        // Test DELETE - deleting all users
        System.out.println("\n8. DELETING ALL USERS");
        userDao.getAllUsers().forEach(u -> {
            System.out.println("   Deleting user with ID: " + u.getId());
            userDao.deleteUser(u.getId());
        });
        System.out.println("   All users deleted");

        // Final check
        System.out.println("\n9. FINAL CHECK");
        System.out.println("   Users in database: " + userDao.getAllUsers().size());

        HibernateUtil.shutdown();
        System.out.println("\n=== DEMONSTRATION COMPLETED ===");
    }
}

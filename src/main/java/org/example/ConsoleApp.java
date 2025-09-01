package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDao;
import org.example.model.User;
import org.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;


public class ConsoleApp {
    private static final Logger logger = LogManager.getLogger(UserDao.class);
    private final UserDao userDao = new UserDao();
    private final Scanner scanner = new Scanner(System.in);

    public void start(){
        logger.info("Starting console application");
        while(true){
            printMenu();
            int op = getIntInput("Choose action: ");


            switch (op){
                case 1 -> createUser();
                case 2 -> showAllUsers();
                case 3 -> findUserById();
                case 4 -> updateUser();
                case 5 -> deleteUser();
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

    private int getIntInput(String str){
        while (true){
            try{
                System.out.println(str);
                return Integer.parseInt(scanner.nextLine());
            }catch (NumberFormatException e){
                System.out.println("Error: input integer");
                logger.warn("Invalid number input");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Create user");
        System.out.println("2. Show all users");
        System.out.println("3. Find user by ID");
        System.out.println("4. Update user");
        System.out.println("5. Delete user");
        System.out.println("0. Exit");
    }


    private void createUser(){
        logger.debug("Creating new user");
        System.out.println("User creation: ");

        User user = new User();

        System.out.println("Enter name");
        user.setName(scanner.nextLine());

        System.out.println("Enter age");
        user.setAge(getIntInput("Age: "));

        System.out.println("Enter email:");
        user.setEmail(scanner.nextLine());

        Long id = userDao.saveUser(user);
        if (id != null) {
            System.out.println("User created with ID: " + id);
            logger.info("User created with ID: {}", id);
        } else {
            System.out.println("Failed to create user");
            logger.error("Failed to create user");
        }
    }

    private void showAllUsers(){
        logger.debug("Showing all users");
        System.out.println("All users:");

        List<User> users = userDao.getAllUsers();
        System.out.println("Users found: " + users.size());

        users.forEach(System.out::println);
    }

    private void findUserById(){
        System.out.println("User finding");
        Long id = (long) getIntInput("Input user ID: ");
        logger.debug("Searching for user with ID: {}", id);

        var user = userDao.getUserById(id);
        if(user.isPresent()){
            User u = user.get();
            System.out.println("User with ID: "+u.getId());
            System.out.println(u);
            logger.info("User found: {}", u);
        }else{
            System.out.println("User with ID: "+ id +" not found");
            logger.warn("User witn ID: {} not found", id);
        }
    }

    private void updateUser(){
        System.out.println("User updating");
        Long id = (long) getIntInput("Input user ID: ");
        logger.debug("Updating user with ID: {}", id);

        var userOpt = userDao.getUserById(id);
        if(userOpt.isEmpty()){
            System.out.println("User with ID: "+ id +" not found");
            logger.warn("User witn ID: {} not found for update", id);
            return;
        }

        User user = userOpt.get();

        System.out.println("Current data: " + user);

        System.out.print("New name(Enter for old): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) user.setName(name);

        System.out.print("New email(Enter for old): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("New age(0 for old):  ");
        int age = getIntInput("Age: ");
        if (age > 0) user.setAge(age);

        userDao.updateUser(user);
        System.out.println("User updated");
        logger.info("User updated: {}", user);
    }

    private void deleteUser(){
        System.out.println("User deleting");
        Long id = (long) getIntInput("User ID for deleting: ");
        logger.debug("Deleting user with ID: {}", id);

        var userOpt = userDao.getUserById(id);
        if (userOpt.isPresent()) {
            userDao.deleteUser(id);
            System.out.println("User deleted");
            logger.info("User with ID {} deleted", id);
        } else {
            System.out.println("User with ID " + id + " not found");
            logger.warn("User with ID {} not found for deletion", id);
        }
    }
}

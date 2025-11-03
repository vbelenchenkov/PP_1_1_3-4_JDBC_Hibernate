package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {

        // реализуйте алгоритм здесь
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Tom", "Anderson", (byte) 45);
        userService.saveUser("Kate", "Tyler", (byte) 25);
        userService.saveUser("Ivan", "Petrov", (byte) 32);
        userService.saveUser("Elena", "Ivanova", (byte) 28);
        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        System.out.println(userService.getAllUsers());
        userService.dropUsersTable();
    }
}

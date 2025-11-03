package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlTableCreation = "CREATE TABLE IF NOT EXISTS users " +
                "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(45), " +
                "lastName VARCHAR(45), " +
                "age TINYINT ) ";
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlTableCreation);
            System.out.println("Table has been created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sqlTableDeletion = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlTableDeletion);
            System.out.println("Table has been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlUserInput = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sqlUserInput)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("New user has been added");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeUserById(long id) {
        String sqlUserDeletion = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUserDeletion)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("The user has been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sqlGetAllUsers = "SELECT * FROM users";
        try(Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlGetAllUsers);
            while(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("LastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            return users;
    }

    public void cleanUsersTable() {
        String sqlAllUsersDeletion = "DELETE FROM users";
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlAllUsersDeletion);
            System.out.println("All users have been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

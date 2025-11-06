package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection;

    public UserDaoJDBCImpl() {
        this.connection = new Util().getConnection();
    }

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(45), " +
                    "lastName VARCHAR(45), " +
                    "age TINYINT)";

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String SAVE_USER_SQL =
            "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER_SQL = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String CLEAN_TABLE_SQL = "DELETE FROM users";


    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_SQL);
            System.out.println("Table has been created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE_SQL);
            System.out.println("Table has been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER_SQL)) {
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("The user has been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {

            while (resultSet.next()) {
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
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_TABLE_SQL);
            System.out.println("All users have been deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

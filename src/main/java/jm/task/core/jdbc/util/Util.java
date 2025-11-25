package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {

    private static final String HOST = "jdbc:mysql://localhost:3306/jdbc_test";
    private static final String USER = "root";
    private static final String PWD = "1111";
    private static final String DRV = "com.mysql.cj.jdbc.Driver";

    private SessionFactory sessionFactory;

    public static Connection getConnection() {
        try {
            Class.forName(DRV);
            return DriverManager.getConnection(HOST, USER, PWD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("DataBase connection error", e);
        }
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                configuration.setProperty("hibernate.connection.driver_class", DRV);
                configuration.setProperty("hibernate.connection.url", HOST);
                configuration.setProperty("hibernate.connection.username", USER);
                configuration.setProperty("hibernate.connection.password", PWD);

                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                throw new RuntimeException("SessionFactory craetion error", e);
            }
        }
        return sessionFactory;
    }
}
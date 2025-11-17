package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.NativeQuery;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users (" +
            "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(45) NOT NULL, " +
            "age TINYINT NOT NULL)";

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = createSessionFactory();
    }

    private SessionFactory createSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/jdbc_test");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "1111");

            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            configuration.addAnnotatedClass(User.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("SessionFactory creation error", e);
        }
    }

    @Override
    public void createUsersTable() {

        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            NativeQuery<?> query = session.createNativeQuery(CREATE_TABLE_SQL);
            query.executeUpdate();

            session.getTransaction().commit();
            System.out.println("users table has been created (Hibernate).");
        } catch (Exception e) {
            if(session.getTransaction() != null) {
                session.getTransaction().rollback();
            } throw new RuntimeException("Table creation error", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            NativeQuery<?> query = session.createNativeQuery(DROP_TABLE_SQL);
            query.executeUpdate();

            session.getTransaction().commit();
            System.out.println("users table has been deleted (Hibernate).");
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            } throw new RuntimeException("Table deletion error", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            User user = new User(name,lastName, age);
            session.save(user);

            session.getTransaction().commit();
            System.out.println("New user - " + name + " has been added to the DB." +
                    "(Hibernate)");
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            } throw new RuntimeException("Saving new user error", e);
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            User user = session.get(User.class, id);
            if(user != null) {
                session.delete(user);
                System.out.println("User with ID = " + id + " has been deleted (Hibernate)");
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            } throw new RuntimeException("User deletion error", e);
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM User", User.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            session.createQuery("DELETE FROM User").executeUpdate();

            session.getTransaction().commit();
            System.out.println("Users table has been cleared (Hibernate). ");
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            } throw new RuntimeException("Clearing table error", e);
        } finally {
            session.close();
        }
    }
}

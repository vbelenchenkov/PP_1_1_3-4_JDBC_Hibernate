package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(45) NOT NULL, " +
                    "lastName VARCHAR(45) NOT NULL, " +
                    "age TINYINT NOT NULL)";

            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();

            transaction.commit();
            System.out.println("users table has been created (Hibernate).");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String sql = "DROP TABLE IF EXISTS users";
            NativeQuery<?> query = session.createNativeQuery(sql);
            query.executeUpdate();

            transaction.commit();
            System.out.println("users table has been deleted (Hibernate).");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name,lastName, age);
            session.save(user);

            transaction.commit();
            System.out.println("New user - " + name + " has been added to the DB." +
                    "(Hibernate)");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if(user != null) {
                session.delete(user);
                System.out.println("User with ID = " + id + " has been deleted (Hibernate)");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            String hql = "DELETE FROM User";
            session.createQuery(hql).executeUpdate();

            transaction.commit();
            System.out.println("Users table has been cleared (Hibernate). ");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            } e.printStackTrace();
        }
    }
}

package util;

import entity.Account;
import entity.User;
import model.BankSettingsDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.NoResultException;
import java.util.Properties;

public class DatabaseService {

    protected SessionFactory sessionFactory;
    protected boolean connected;

    public DatabaseService() {
    }

    public void connect(BankSettingsDTO dto) {

        /*
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
         */

        String dbType = null;
        if (dto.getDbDriver().contains("mysql")) {
            dbType = "mysql";
        }

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, dto.getDbDriver());
        settings.put(Environment.URL, "jdbc:" + dbType + "://" + dto.getDbHost() + ":" + dto.getDbPort() + "/" + dto.getDbDatabase());
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MariaDB103Dialect");
        settings.put(Environment.USER, dto.getDbUser());
        settings.put(Environment.PASS, dto.getDbPassword());
        settings.put(Environment.SHOW_SQL, false);
        settings.put(Environment.FORMAT_SQL, false);

        Configuration cfg = new Configuration();
        cfg.setProperties(settings);

        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(Account.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        sessionFactory = cfg.buildSessionFactory(serviceRegistry);
        connected = true;
    }

    public void exit() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public User login(String username, String userPassword) {
        Session session = sessionFactory.openSession();
        User user;

        try {
            user = session.createQuery("select u FROM User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", username)
                    .setParameter("password", userPassword)
                    .getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        } finally {
            session.close();
        }

        return user;
    }


    public boolean isConnected() {
        return connected;
    }

    public boolean saveUser(User user) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception ex) {
            session.close();
            return false;
        }

        return true;
    }
}

package controller;

import entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import java.util.List;
import java.util.Properties;

/**
 * BookManager.java
 * A Hibernate hello world program
 *
 * @author www.codejava.net
 */
public class BookManager {

    protected SessionFactory sessionFactory;

    public static void main(String[] args) {
        BookManager manager = new BookManager();
        manager.setup();

//        manager.create();
//        manager.read();
//        manager.delete();
        List<Book> ergebnis = manager.select(2);

        for (Book b : ergebnis) {
            System.out.println(b.getTitle() + " " + b.getAuthor());
        }

        manager.exit();
    }

    protected void setup() {
//        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().configure().applySetting("mapping", )

        Configuration cfg = new Configuration();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://h2:3306/bank");
        settings.put(Environment.USER, "bank");
        settings.put(Environment.PASS, "bank");
//        settings.put(Environment.DIALECT, "org.hibernate.dialect.MariaDB103Dialect");
        settings.put(Environment.SHOW_SQL, false);
        settings.put(Environment.FORMAT_SQL, false);


/**
 * Connection Information..
 */
/*
        cfg.setProperty("hibernate.connection.driver_class", );
        cfg.setProperty("hibernate.connection.url", );
        cfg.setProperty("hibernate.connection.username", "bank");
        cfg.setProperty("hibernate.connection.password", "bank");
        cfg.setProperty("hibernate.show_sql", "true");
*/
        cfg.setProperties(settings);
        cfg.addAnnotatedClass(Book.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        //final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings from hibernate.cfg.xml .build();
/*
        if ( H2Dialect.class.equals( Dialect.getDialect().getClass() ) ) {
            ssrb.applySetting( AvailableSettings.URL, "jdbc:h2:mem:db-mvcc;MVCC=true" );
        }
*/
//        StandardServiceRegistry

        try {

            //  sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            //StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    protected void exit() {
        sessionFactory.close();
    }

    protected void create() {
        Book book = new Book();
        book.setTitle("Effective Java2");
        book.setAuthor("Joshua Bloch2");
        book.setPrice(39.99f);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(book);

        session.getTransaction().commit();
        session.close();
    }

    public List<Book> select(int limit) {
        Session session = sessionFactory.openSession();
        String hql = "SELECT b FROM Book b";
        Query query = session.createQuery(hql);
        query.setMaxResults(limit);
        List<Book> results = query.list();

        session.close();

        return results;
    }

    public void updateBySQL() {
        Session session = sessionFactory.openSession();
        String hql = "UPDATE Employee set salary = :salary WHERE id = :employee_id";
        Query query = session.createQuery(hql);
        query.setParameter("salary", 1000);
        query.setParameter("employee_id", 10);
        int result = query.executeUpdate();
        System.out.println("Rows affected: " + result);

        session.close();
    }

    protected void read() {
        Session session = sessionFactory.openSession();

        long bookId = 1;
        Book book = session.get(Book.class, bookId);

        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("Price: " + book.getPrice());

        session.close();
    }

    protected void update() {
        Book book = new Book();
        book.setId(1);
        book.setTitle("Ultimate Java Programming");
        book.setAuthor("Nam Ha Minh");
        book.setPrice(19.99f);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.update(book);

        session.getTransaction().commit();
        session.close();
    }

    protected void delete() {
        Book book = new Book();
        book.setId(20);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(book);

        session.getTransaction().commit();
        session.close();
    }

}

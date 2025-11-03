package ua.cn.stu.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ua.cn.stu.domain.Product;

import java.io.InputStream;
import java.util.Properties;

public class HibernateDAOFactory {

    private static SessionFactory sessionFactory;
    private ThreadLocal<Session> threadLocalSession = new ThreadLocal<>();

    private static final HibernateDAOFactory instance = new HibernateDAOFactory();

    private HibernateDAOFactory() {}

    public static HibernateDAOFactory getInstance() {
        return instance;
    }

    public static void buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                Properties dbProps = new Properties();

                try (InputStream input = HibernateDAOFactory.class.getClassLoader()
                        .getResourceAsStream("db.properties")) {

                    if (input == null) {
                        throw new RuntimeException("Cannot find db.properties in classpath");
                    }
                    dbProps.load(input);
                }

                Configuration configuration = new Configuration();

                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.connection.url", dbProps.getProperty("db.url"));
                configuration.setProperty("hibernate.connection.username", dbProps.getProperty("db.username"));
                configuration.setProperty("hibernate.connection.password", dbProps.getProperty("db.password"));
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                configuration.setProperty("hibernate.show_sql", "true");
                configuration.setProperty("hibernate.format_sql", "true");

                configuration.addAnnotatedClass(Product.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                throw new RuntimeException("Error building SessionFactory", e);
            }
        }
    }

    public Session getCurrentSession() {
        Session session = threadLocalSession.get();
        if (session == null || !session.isOpen()) {
            if (sessionFactory == null) {
                throw new IllegalStateException("SessionFactory not initialized");
            }
            session = sessionFactory.openSession();
            threadLocalSession.set(session);
        }
        return session;
    }

    public ProductDAO getProductDAO() {
        return new ProductDAO(getCurrentSession());
    }

    public void closeCurrentSession() {
        Session session = threadLocalSession.get();
        if (session != null && session.isOpen()) {
            session.close();
            threadLocalSession.remove();
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }

}

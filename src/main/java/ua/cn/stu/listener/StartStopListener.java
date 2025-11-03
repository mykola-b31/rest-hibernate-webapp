package ua.cn.stu.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ua.cn.stu.dao.HibernateDAOFactory;

@WebListener
public class StartStopListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        HibernateDAOFactory.buildSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        HibernateDAOFactory.closeSessionFactory();
    }

}

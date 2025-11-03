package ua.cn.stu.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import ua.cn.stu.dao.HibernateDAOFactory;

import java.io.IOException;

@WebFilter("/*")
public class HibernateSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HibernateDAOFactory.getInstance().getCurrentSession();

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            HibernateDAOFactory.getInstance().closeCurrentSession();
        }
    }
}

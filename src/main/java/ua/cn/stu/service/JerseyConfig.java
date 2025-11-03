package ua.cn.stu.service;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("rest")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("ua.cn.stu.service");
    }

}
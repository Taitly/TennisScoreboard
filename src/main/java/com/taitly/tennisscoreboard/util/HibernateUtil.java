package com.taitly.tennisscoreboard.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            return configuration.buildSessionFactory();
        } catch (HibernateException e) {
            throw new RuntimeException("There was an error building the factory");
        }
    }
}
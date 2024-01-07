package org.masonord.delivery.config;

import org.masonord.delivery.DeliveryApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {
    private static PropertiesConfig environment;

    @Autowired
    HibernateConfig(PropertiesConfig propertiesConfig) {
        HibernateConfig.environment = propertiesConfig;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] {"org.masonord.delivery.model"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getConfigValue("jdbc.driverClassName"));
        dataSource.setUrl(environment.getConfigValue("jdbc.url"));
        dataSource.setUsername(environment.getConfigValue("jdbc.username"));
        dataSource.setPassword(environment.getConfigValue("jdbc.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getConfigValue("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getConfigValue("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getConfigValue("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getConfigValue("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.max_fetch_depth", environment.getConfigValue("hibernate.max.depth"));
        return properties;
    }

    @Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}

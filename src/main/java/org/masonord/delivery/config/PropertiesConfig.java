package org.masonord.delivery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
public class PropertiesConfig  {

    private static ConfigurationClass config;
    @Autowired
    public PropertiesConfig(ConfigurationClass config) {
        PropertiesConfig.config= config;
    }

    public static String getConfigValue(String configKey) {
        return PropertiesConfig.config.getConfigValue(configKey);
    }
}


@Configuration
@PropertySource({"classpath:custom.properties", "application.properties"})
class ConfigurationClass implements EnvironmentAware {

    @Autowired
    private Environment env;

    public String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }
}

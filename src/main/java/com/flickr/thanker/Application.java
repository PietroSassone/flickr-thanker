package com.flickr.thanker;

import com.flickr.thanker.core.LogicExecutor;
import com.flickr.thanker.util.WebDriverFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("com.flickr.thanker")
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    private LogicExecutor executor;

    @Bean(destroyMethod = "shutDownDriver")
    public WebDriverFactory factory() {
        return new WebDriverFactory();
    }

    public static void main(final String[] args) {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class)) {
            context.getBean(Application.class).execute();
        }
    }

    private void execute() {
        LOGGER.info("Application started.");
        executor.executeLogic();
        LOGGER.info("Application finished.");
    }
}

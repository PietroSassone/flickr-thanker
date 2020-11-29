package com.flickr.thanker.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import static java.util.Objects.isNull;

/**
 * Factory class to set up and close webdriver.
 */
@PropertySource("classpath:application.properties")
public class WebDriverFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverFactory.class);
    private static final String CHROME_DRIVER = "chromedriver";
    private static final String FIREFOX_DRIVER = "firefoxdriver";

    @Value("${webdriver.name:chromedriver}")
    private String driverName;

    private WebDriver driver;

    public WebDriver getDriver() {
        if (isNull(driver)) {
            switch (driverName) {
                case CHROME_DRIVER:
                    driver = setUpChromeDriver();
                    break;
                case FIREFOX_DRIVER:
                    driver = setUpFirefoxDriver();
                    break;
                default:
                    throw new RuntimeException("Unsupported driver name=" + driverName);
            }
            driver = new EventFiringWebDriver(driver).register(new WebDriverLogger());
            driver.manage().window().maximize();
        }
        return driver;
    }

    private WebDriver setUpChromeDriver() {
        WebDriverManager.chromedriver().setup();
        LOGGER.info("ChromeDriver was created");

        return new ChromeDriver(new ChromeOptions().addArguments("disable-infobars"));
    }

    private WebDriver setUpFirefoxDriver() {
        LOGGER.info("Firefox Driver was created");
        return new FirefoxDriver();
    }

    public void shutDownDriver() {
        driver.close();
        driver.quit();
        driver = null;
        LOGGER.info("WebDriver was closed.");
    }
}

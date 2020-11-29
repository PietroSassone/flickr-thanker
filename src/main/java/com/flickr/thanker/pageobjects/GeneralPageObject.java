package com.flickr.thanker.pageobjects;

import com.flickr.thanker.util.WebDriverFactory;

import org.awaitility.Duration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Common page object related logic.
 */
public class GeneralPageObject {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralPageObject.class);
    private static final long PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS = 15;
    private static final String COMPLETE = "complete";
    private static final String RETURN_DOCUMENT_READY_STATE = "return document.readyState";
    private static final long TIMEOUT_MILLIS = 10000L;

    private WebDriverFactory factory;

    public GeneralPageObject(final WebDriverFactory factory) {
        PageFactory.initElements(factory.getDriver(), this);
        this.factory = factory;
    }

    public void moveToElement(final WebElement webElement) {
        new Actions(getDriver()).moveToElement(webElement);
    }

    public void waitForElementToBeClickable(final WebElement webElement) {
        LOGGER.info("Waiting for element to be clickable, seconds={}", PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS);
        try {
            getWebDriverWaitWithSeconds().until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (NoSuchElementException e) {
            LOGGER.error("Element is not clickable!");
        }
    }

    public WebDriver getDriver() {
        return factory.getDriver();
    }

    public void synchronizedWait() {
        final WebDriver driver = getDriver();
        try {
            synchronized (driver) {
                driver.wait(TIMEOUT_MILLIS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void goToUrl(final String url, final Supplier<Boolean> isThePageLoaded) {
        getDriver().get(url);
        waitForPageToLoad(isThePageLoaded);
    }

    protected void waitForElementToBeDisplayed(final WebElement webElement) {
        await(format("Element was not loaded in %s seconds", PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS))
                .atMost(new Duration(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS, TimeUnit.SECONDS))
                .until(webElement::isDisplayed, equalTo(true));
    }

    protected void genericWaitForPageReadiness() {
        getWebDriverWaitWithSeconds().until(driver ->
                valueOf(((JavascriptExecutor) driver).executeScript(RETURN_DOCUMENT_READY_STATE))
                        .equals(COMPLETE)
        );
    }

    private WebDriverWait getWebDriverWaitWithSeconds() {
        return new WebDriverWait(getDriver(), PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS);
    }

    private void waitForPageToLoad(final Supplier<Boolean> isThePageLoaded) {
        LOGGER.info("Waiting {} seconds for page load", PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS);

        genericWaitForPageReadiness();

        new WebDriverWait(getDriver(), PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS)
                .until(webdriver -> isThePageLoaded.get());
    }
}

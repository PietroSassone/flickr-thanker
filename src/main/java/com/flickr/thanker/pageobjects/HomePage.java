package com.flickr.thanker.pageobjects;

import com.flickr.thanker.util.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

/**
 * PageObject for the home page after login.
 */
@Component
public class HomePage extends GeneralPageObject {

    @FindBy(className = "c-account-buddyicon")
    private WebElement profileIcon;

    public HomePage(final WebDriverFactory factory) {
        super(factory);
    }

    public void waitForHomePageToLoad() {
        waitForElementToBeClickable(profileIcon);
    }

}

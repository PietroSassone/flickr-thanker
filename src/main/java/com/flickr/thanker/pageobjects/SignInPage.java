package com.flickr.thanker.pageobjects;

import com.flickr.thanker.util.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * PageObject for the Signin page.
 */
@Component
public class SignInPage extends GeneralPageObject {
    private static final String LOGIN_PAGE_URL = "https://identity.flickr.com/login";

    @FindBy(className = "content-container")
    private WebElement pageContent;

    @FindBy(id = "login-email")
    private WebElement emailInput;

    @FindBy(id = "login-password")
    private WebElement passwordInput;

    @FindBy(css = "#login-form > button")
    private WebElement confirmButton;

    @FindBy(css = " m")
    private WebElement viewMoreLinks;

    @Value("${email}")
    private String email;

    @Value("${password}")
    private String passWord;

    public SignInPage(final WebDriverFactory factory) {
        super(factory);
    }

    public void navigateToSignInPage() {
        goToUrl(LOGIN_PAGE_URL, this::isSignInPageDisplayed);
    }

    public void logIn() {
        waitForElementToBeDisplayed(emailInput);
        emailInput.sendKeys(email);
        confirmButton.click();

        genericWaitForPageReadiness();

        waitForElementToBeClickable(passwordInput);
        passwordInput.sendKeys(passWord);
        confirmButton.click();

        genericWaitForPageReadiness();
    }

    public Boolean isSignInPageDisplayed() {
        waitForElementToBeDisplayed(pageContent);
        return pageContent.isDisplayed();
    }
}

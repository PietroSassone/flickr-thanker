package com.flickr.thanker.pageobjects;

import com.flickr.thanker.util.WebDriverFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * PageObject for the page of the photo.
 */
@Component
public class PhotoPage extends GeneralPageObject {
    private static final String NEW_COMMENT_MESSAGE = "Thank you all very much!";
    private static final String PHOTO_PAGE_BASE_URL_PATTERN = "https://www.flickr.com/photos/%s/%s";

    @FindBy(className = "photo-page-scrappy-view")
    private WebElement pageContent;

    @FindBy(css = ".comments-more a")
    private WebElement expandCommentSectionButton;

    @FindBy(className = "new-comment-text")
    private WebElement newCommentInputField;

    @FindBy(className = "ui-button-cta comment")
    private WebElement addCommentButton;

    @Value("${userId}")
    private String userId;

    @Value("${photoId}")
    private String photoId;

    public PhotoPage(final WebDriverFactory factory) {
        super(factory);
    }

    public void navigateToPhotoPage() {
        goToUrl(format(PHOTO_PAGE_BASE_URL_PATTERN, userId, photoId), this::isPhotoPageDisplayed);
    }

    public Boolean isPhotoPageDisplayed() {
        return pageContent.isDisplayed();
    }

    public void clickExpandCommentsSection() {
        waitForElementToBeClickable(expandCommentSectionButton);
        moveToElement(expandCommentSectionButton);
        expandCommentSectionButton.click();

        genericWaitForPageReadiness();
        synchronizedWait();
    }

    public void addNewComment() {
        moveToElement(newCommentInputField);
        newCommentInputField.sendKeys(NEW_COMMENT_MESSAGE);
    }

    public void clickOnAddCommentButton() {
        waitForElementToBeDisplayed(addCommentButton);
        moveToElement(addCommentButton);
        addCommentButton.click();
    }
}

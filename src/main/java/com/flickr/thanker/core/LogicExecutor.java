package com.flickr.thanker.core;

import com.flickr.thanker.pageobjects.HomePage;
import com.flickr.thanker.pageobjects.PhotoPage;
import com.flickr.thanker.pageobjects.SignInPage;
import com.flickr.thanker.util.WebDriverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LogicExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogicExecutor.class);

    @Autowired
    private SignInPage signInPage;

    @Autowired
    private HomePage homePage;

    @Autowired
    private PhotoPage photoPage;

    @Autowired
    private WebDriverFactory factory;

    public void executeLogic() {
        signInPage.navigateToSignInPage();

        signInPage.logIn();

        LOGGER.info("Login successful.");

        homePage.waitForHomePageToLoad();

        photoPage.navigateToPhotoPage();

        photoPage.clickExpandCommentsSection();

        clickOnReplyButtons(
                gatherReceivedCommentsWithUniqueAuthors(
                        photoPage.getDriver().findElements(By.className("comment-author"))
                )
        );

        LOGGER.info("Adding new comment.");
        photoPage.addNewComment();

        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//            photoPage.clickOnAddCommentButton();
        LOGGER.info("Successfully added new comment.");
    }

    private Map<String, WebElement> gatherReceivedCommentsWithUniqueAuthors(final List<WebElement> comments) {
        LOGGER.info("Gathering received comments, once per author.");
        LOGGER.info("Found comments, before filtering to once per author: {}", comments.size());
        Map<String, WebElement> commentAuthorsWithReplyButtonsMap = new HashMap<>();

        comments.forEach(comment -> {
            WebElement commentAuthor;
            WebElement replyButton;
            try {
                commentAuthor = comment.findElement(By.xpath("./a"));
                replyButton = comment.findElement(By.xpath("./span[2]/a[1]"));
                commentAuthorsWithReplyButtonsMap.putIfAbsent(
                        commentAuthor.getText(), replyButton
                );
            } catch (NoSuchElementException e) {
                LOGGER.info("Author was deleted, skipping it.");
            }
        });
        LOGGER.info("Number of comments after filtering out multiles from the same author: {}", commentAuthorsWithReplyButtonsMap.size());
        return commentAuthorsWithReplyButtonsMap;
    }

    private void clickOnReplyButtons(final Map<String, WebElement> commentAuthorsWithReplyButtonsMap) {
        LOGGER.info("Clicking on all the reply buttons.");
        commentAuthorsWithReplyButtonsMap.forEach((authorName, replyButton) -> {
            ((JavascriptExecutor) photoPage.getDriver()).executeScript("arguments[0].click();", replyButton);
        });
    }
}

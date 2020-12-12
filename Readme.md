# Flickr thanker

This is a small selenium based Java maven project.
The goal of this project is to demonstrate how to create a small useful application.
Using technologies that QA engineers use in real life.

## What it does

As a hobby photographer, I used to upload photograps to Flickr.
Then I added the uploaded photos to groups.
This can result in having several dozens or sometimes hundreds of comments being received under a photo.
The application does the following taks in the given order:
* logs in to Flickr with any valid credential supplied to it
* navigates to the webpage where 1 chosen photo of the logged in user is uploaded
* expands the comments section
* collects all the comments received by still active accounts
* clicks on the reply buttons for each collected comment, but only once per comment author
* adds 1 new comment thanking everyone for the comments.

Notes: 
* This is useful only once per photo.
* After you've added a 'Thank you' comment, the app would still thank all comments when running again for the same photo.
* At the moment, the final click on the send comment button is commented out.
In order to be able to try the app without actually sending a comment. It can be re-enabled any time.

## Tech details

The app is running on Java 8.
Technologies/patterns/tools used:
* Spring core
* Maven compiler
* Maven exec
* Selenium
* Bonigarcia webdriver manager
* Awaitility
* Logback logging
* Standard sun checkstyle, with some modifications
* Page object pattern
* Factory pattern with Webdriver factory

# Pre-requirements to run the app:
* OpenJDK installed, at least version 8.
* Chrome installed.
* Firefox installed.
* Cromedriver and Geckodriver installed.

# How to use it

Setup:
Let's look at an example link to one of my uploaded photos:
https://www.flickr.com/photos/160735500@N04/49586783068
From the link 160735500@N04 is the userId and 49586783068 is the photoId.

Parameters to supply to the application inside the application.properties file:
| Mandatory | Parameter name   | Explanation                                                                                                                                                                                      |
|-----------|------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Yes       | email            | The email address used to login to Flickr.                                                                                        |
| Yes       | password         | The password used to login to Flickr.                                                                                             |
| Yes       | userId           | Flickr identifier number of the user. See the example above.                                                                      |
| Yes       | photoId          | Flickr identifier number of the photo that needs to be commented. See the example above.                                          |
| No        | webdriver.name   | Used to set up which browser should be used by the app. Possible values: chromedriver, firefoxdriver. Default value: chromedriver |


Run:
* Build the application with mvn clean install in the application root directory.
1.
* Run it by typing mvn exec:java into a command line terminal from the application root directory.
2.
* If the properties were not supplied in the application properties before the build, they can be supplied like:
mvn exec:java -Demail=<email address> -Dpassword=<password> -DuserId=<real user Id> -DphotoId=<Id of uploaded photo> -Dwebdriver.name=<desired webdriver>

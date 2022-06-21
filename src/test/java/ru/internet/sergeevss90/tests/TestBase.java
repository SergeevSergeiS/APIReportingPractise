package ru.internet.sergeevss90.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import ru.internet.sergeevss90.config.CredentialsConfig;
import ru.internet.sergeevss90.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);
    static String selenoidURL;
    static String userLogin;
    static String userPassword;
    static String authCookie;

    @BeforeAll
    static void setUp() {

        String login = config.login();
        String password = config.password();
        userLogin = config.userLogin();
        userPassword = config.userPassword();
        authCookie = config.authCookie();
        selenoidURL = System.getProperty("selenoidURL");

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());


        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.browserSize = "1920x1080";
        Configuration.remote = "https://" + login + ":" + password + "@selenoid.autotests.cloud/wd/hub";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "100.0");
        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));
        Configuration.browserCapabilities = capabilities;
    }


    @AfterEach
    void addAttachments() {
        Attach.screenshotAs("Test screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo(selenoidURL);
        closeWebDriver();
    }
}

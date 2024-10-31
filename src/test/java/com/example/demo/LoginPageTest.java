package com.example.demo;

import com.example.demo.message.AuthMessage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPageTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginHelper loginHelper;

    @BeforeAll
    public static void setUpChromeDriverPath()
    {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver-win64/chromedriver.exe");
    }

    @BeforeEach
    public void setUp() {
        // Khởi tạo WebDriver and WebDriverWait
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allowed-ips");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        // chuyển đến trang đăng nhập
        driver.get(MapPath.LOGIN_PATH);

        loginHelper = new LoginHelper(driver, wait);
    }
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testUsernameNull() {
        loginHelper.login("", "pass1234");

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("txt-username-helper-text")));
        Assertions.assertEquals(AuthMessage.USERNAME_REQUIRED,
                message.getText());
    }
    @Test
    public void testPasswordNull() {
        loginHelper.login("test@example.com", "");

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiFormHelperText-root")));
        Assertions.assertEquals(AuthMessage.PASSWORD_REQUIRED,
                message.getText());
    }
    @Test
    public void testHaveUsernameAndWrongPassword() {
        loginHelper.login("test@example.com", "password123");

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-qlg5wx")));
        Assertions.assertEquals(AuthMessage.CHECK_ACCOUNT,
                message.getText());
    }
    @Test
    public void testNotHaveUsername() {
        loginHelper.login("user", "Abc@12345");

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-qlg5wx")));
        Assertions.assertEquals(AuthMessage.CHECK_ACCOUNT,
                message.getText());
    }
    @Test
    public void testSuccessfulLogin()  {
        loginHelper.login("test@example.com", "cde@12345");

        WebElement title =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-h6")));
        Assertions.assertEquals("Nhóm 2", title.getText());
    }
}

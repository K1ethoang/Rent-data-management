package com.example.demo;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChangePasswordPageTest {
    private WebElement oldPasswordField;
    private WebElement newPasswordField;
    private WebElement repeatPasswordField;
    private WebElement changePasswordButton;
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

        // Login và lấy phần tử
        loginHelper.loginSuccess();
        driver.navigate().to(MapPath.CHANGE_PASSWORD_PATH);
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-h2")));
        oldPasswordField = driver.findElement(By.id("txt-currentPassword"));
        newPasswordField = driver.findElement(By.id("txt-newPassword"));
        repeatPasswordField  = driver.findElement(By.id("txt-repeatNewPassword"));
        changePasswordButton  = driver.findElement(By.cssSelector(".MuiButton" +
                "-contained"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testOldPasswordNull() {
        oldPasswordField.sendKeys("");
        newPasswordField.sendKeys("abc@12345");
        repeatPasswordField.sendKeys("abc@12345");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "p.Mui-error:nth-child(3)")));
        Assertions.assertEquals("Current password is a required field",message.getText());
    }

    @Test
    @Order(2)
    public void testNewPasswordNull() {
        oldPasswordField.sendKeys("abc@12345");
        newPasswordField.sendKeys("");
        repeatPasswordField.sendKeys("hellofriend"); // khong can thiet
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "p.Mui-error:nth-child(3)")));
        Assertions.assertEquals("New password is a required field",
                message.getText());
    }

    @Test
    @Order(3)
    public void testRepeatPasswordNull(){
        oldPasswordField.sendKeys("abc@12345");
        newPasswordField.sendKeys("cde@12345");
        repeatPasswordField.sendKeys("");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "p.Mui-error:nth-child(3)")));
        Assertions.assertEquals("Repeat Password is required",
                message.getText());
    }

    @Test
    @Order(4)
    public void testIncorrectOldPassword(){
        oldPasswordField.sendKeys("ABC@12345");
        newPasswordField.sendKeys("cde@12345");
        repeatPasswordField.sendKeys("cde@12345");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "div.css-1slz3n5")));
        Assertions.assertEquals("Current password is not match",
                message.getText());
    }

    @Test
    @Order(5)
    public void testNewPasswordEqualOldPassword(){
        oldPasswordField.sendKeys("cde@12345");
        newPasswordField.sendKeys("cde@12345");
        repeatPasswordField.sendKeys("Abc@12345");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "p.Mui-error:nth-child(3)")));
        Assertions.assertEquals("New password must be different from current password",
                message.getText());
    }

    @Test
    @Order(6)
    public void testNewPasswordAtLeast8Char(){
        oldPasswordField.sendKeys("Abc@12345");
        newPasswordField.sendKeys("cde@123");
        repeatPasswordField.sendKeys("cde@123");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "div.css-1slz3n5")));
        Assertions.assertEquals("New Password must be at least 8 characters",
                message.getText());
    }

    @Test
    @Order(7)
    public void testRepeatPasswordNotMatch(){
        oldPasswordField.sendKeys("Abc@12345");
        newPasswordField.sendKeys("cde@12345");
        repeatPasswordField.sendKeys("cde@123");
        changePasswordButton.click();

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
                        "p.Mui-error:nth-child(3)")));
        Assertions.assertEquals("Repeat Password is incorrect",
                message.getText());
    }

    @Test
    @Order(8)
    public void testChangeSuccessfully(){
        oldPasswordField.sendKeys("Abc@12345");
        newPasswordField.sendKeys("cde@12345");
        repeatPasswordField.sendKeys("cde@12345");
        changePasswordButton.click();

        WebElement title =
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h2.MuiTypography-h2")));
        Assertions.assertEquals("Edit Account", title.getText());
    }
}

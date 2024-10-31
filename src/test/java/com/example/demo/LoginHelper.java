package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginHelper {
    private final WebElement usernameField;
    private final WebElement passwordField;
    private final WebElement loginButton;
    private WebDriverWait wait;

    public LoginHelper(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        usernameField = driver.findElement(By.id("txt-username"));
        passwordField = driver.findElement(By.id("txt-password"));
        loginButton = driver.findElement(By.cssSelector(".MuiButton-root"));
    }

    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public void loginSuccess()
    {
        usernameField.sendKeys("test@example.com");
        passwordField.sendKeys("Abc@12345");
        loginButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".MuiTypography-h6")));
    }
}

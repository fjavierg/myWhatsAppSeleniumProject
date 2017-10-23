package org.openqa.selenium.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WhatsApp {
    
    public WebDriver driver;

    public WhatsApp() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.setProperty("webdriver.chrome.driver", properties.getProperty("CHROME_DRIVER_PATH"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + properties.getProperty("CHROME_USER_DATA_DIR"));
        driver = new ChromeDriver(options);
    

    }
    
    public void send(String phoneNb, String message) {
        
        driver.get("https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message);
        WebElement mySendButton = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.id("action-button")));      
        // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
        mySendButton.click();      
        WebElement myDynamicElement = (new WebDriverWait(driver, 100))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("compose-btn-send")));        
        myDynamicElement.click();
        
    }
    
}

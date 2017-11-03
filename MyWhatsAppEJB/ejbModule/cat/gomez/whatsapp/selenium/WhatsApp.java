package cat.gomez.whatsapp.selenium;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


@ApplicationScoped
public class WhatsApp {
    
    @Inject
    @Property("CHROME_DRIVER_PATH")
    private String chromeDriverPath;
    @Inject
    @Property("CHROME_USER_DATA_DIR")
    private String chromeUserDataDir;
    private WebDriver driver;
    private ChromeOptions options = new ChromeOptions();

    public WhatsApp() {
    }
    
    @PostConstruct
    public void init () {
        System.setProperty("webdriver.chrome.driver",chromeDriverPath);        
        options.addArguments("user-data-dir=" + chromeUserDataDir);
        driver = new ChromeDriver(options);
    }
    
    public boolean send(String phoneNb, String message) {
        
        int count = 0;
        int maxTries = 2;
        while(true) {
            try {
                driver.get("https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message);
    
                WebElement mySendButton = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("action-button")));      
                // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
                mySendButton.click();      
                WebElement myDynamicElement = (new WebDriverWait(driver, 100))
                        .until(ExpectedConditions.presenceOfElementLocated(By.className("compose-btn-send")));        
                myDynamicElement.click();
                return true;
            }
            catch (TimeoutException ex) {
               System.out.println("Timeout Sending message");
               if (++count == maxTries) throw ex;
            }
            catch (NoSuchSessionException ex) {
                System.out.println("Driver communication problem");
                driver.close();
                driver = new ChromeDriver(options);
                if (++count == maxTries) throw ex;
             }
            catch (Exception ex) {
                System.out.println("Other exception");
                throw ex;
             }
        }
    }

    
    @PreDestroy
    public void dispose() {
        System.out.println("@PreDestroy is called...");
        driver.close();
    }
}

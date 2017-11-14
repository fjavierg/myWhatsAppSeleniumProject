package cat.gomez.whatsapp.selenium;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationScoped
public class WhatsApp {
    private final Logger logger = LoggerFactory.getLogger(WhatsApp.class);
    @Inject
    @Property("CHROME_DRIVER_PATH")
    private String chromeDriverPath;
    @Inject
    @Property("CHROME_USER_DATA_DIR")
    private String chromeUserDataDir;
    private WebDriver driver;
    private ChromeOptions options = new ChromeOptions();
    private DesiredCapabilities cap = DesiredCapabilities.chrome();

    public WhatsApp() {
    }
    
    @PostConstruct
    public void init () {
        logger.info("New Chrome Driver");
        System.setProperty("webdriver.chrome.driver",chromeDriverPath);        
        options.addArguments("user-data-dir=" + chromeUserDataDir);

        cap.setCapability(ChromeOptions.CAPABILITY, options);
        cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        driver = new ChromeDriver(cap);
    }
    
    public synchronized boolean send(String phoneNb, String message) {
        
        int count = 0;
        int maxTries = 2;
        logger.info("New Message to : "+phoneNb+" text : "+message);
        while(true) {
            try {
                driver.get("https://api.whatsapp.com/send?phone="+phoneNb+"&text="+message);
    
                WebElement mySendButton = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("action-button")));      
                // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
                mySendButton.click();      
                WebElement myDynamicElement = (new WebDriverWait(driver, 30))
                        .until(ExpectedConditions.presenceOfElementLocated(By.className("compose-btn-send")));        
                myDynamicElement.click();
                WebElement mynewDynamicElement = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='msg'][last()]//span[@data-icon='msg-check']")));        
              
                return true;
            }
            catch (TimeoutException | UnhandledAlertException ex) {
               logger.error("Timeout Sending message");
               if (++count == maxTries) throw ex;
            }
            catch (WebDriverException ex) {
                logger.error("Driver communication problem");
                //driver.close();
                driver = new ChromeDriver(cap);
                if (++count == maxTries) throw ex;
             }
            catch (Exception ex) {
                logger.error("Other exception");
                throw ex;
             }
        }
    }

    
    @PreDestroy
    public void dispose() {
        logger.info("@PreDestroy is called...");
        driver.close();
    }
}

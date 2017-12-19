package cat.gomez.whatsapp.selenium;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
    @Inject
    private Events events;
    private WebDriver driver;
    private ChromeOptions options = new ChromeOptions();
    private DesiredCapabilities cap = DesiredCapabilities.chrome();
    private Map<String,String> lastMessage = new HashMap<String, String>();


    public WhatsApp() {
    }
    public void init( @Observes @Initialized( ApplicationScoped.class ) Object init ) {
        logger.info("Initialization");
    }
    
    @PostConstruct
    public void init () {
        logger.info("New Chrome Driver");
        System.setProperty("webdriver.chrome.driver",chromeDriverPath);     
        Map<String, Object> chromePreferences = new Hashtable<String, Object>();
        chromePreferences.put("profile.default_content_settings.popups", 0);
        chromePreferences.put("download.prompt_for_download", "false");
        chromePreferences.put("download.extensions_to_open", "");
        options.addArguments("user-data-dir=" + chromeUserDataDir);
        options.setExperimentalOption("prefs", chromePreferences);

        cap.setCapability(ChromeOptions.CAPABILITY, options);
        cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        driver = new ChromeDriver(cap);

        this.read();
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
    

    public synchronized boolean sendNew(String phoneNb, String message) {
        

        logger.info("New Message to : "+phoneNb+" text : "+message);
        try {

            WebElement mySearchInput = (new WebDriverWait(driver, 10))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id("input-chatlist-search")));      
            // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
            mySearchInput.sendKeys(phoneNb+Keys.ENTER);

            WebElement myMessageInput = (new WebDriverWait(driver, 30))
                    .until(ExpectedConditions.presenceOfElementLocated(By.className("pluggable-input-body")));        
            
            myMessageInput.sendKeys(message+Keys.ENTER);
            
            return true;
        }
        catch (TimeoutException | UnhandledAlertException ex) {
           logger.error("Timeout Sending message");
           throw ex;
        }
        catch (WebDriverException ex) {
            logger.error("Driver communication problem");
            throw ex;
        }
        catch (Exception ex) {
            logger.error("Other exception");
            throw ex;
        }
   }

    
   public synchronized void read() {
        
        List<WebElement> myListOfMessages;
        WebElement myButton;
        String title, typeValue;
        MessageType type;

        logger.info("Start : ");
        driver.get("https://web.whatsapp.com/");
        while(true) {
            try {
                
                System.out.println("Let's wait");
                myButton = (new WebDriverWait(driver, 10000))
                        .until(ExpectedConditions.presenceOfElementLocated(By.className("unread")));      
                // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
                myButton.click();
                
                myListOfMessages = driver.findElements(By.className("message-in"));
                WebElement chatActive = driver.findElement(By.className("pane-chat-header"));
                title = chatActive.findElement(By.className("chat-title")).getText();
                
                WebElement newLastMessage = myListOfMessages.get(myListOfMessages.size()-1);
                if (lastMessage.get(title) == null) {
                    lastMessage.put(title, "");
                }

                for(ListIterator<WebElement> li = myListOfMessages.listIterator(myListOfMessages.size()); li.hasPrevious();) {
                    WebElement temp = (WebElement)li.previous();
                    if (lastMessage.get(title).contains(temp.getText())) {
                        lastMessage.put(title, newLastMessage.getText());
                        break;
                    }
                    typeValue = temp.getAttribute("class").split(" ")[1].split("-")[1];
                    type = MessageType.valueOf(typeValue);
                    
                    switch (type) {
                        case chat: 
                            MessageParser.parseChat(this, events, title,temp);
                            break;
                            
                        case image:
                            MessageParser.parseImage(this, events, title,temp,driver);
                            break;
                            
                        default:
                            
                    }

                }
                
                if (lastMessage.get(title) == "") {
                    lastMessage.put(title, newLastMessage.getText());
                }

            }
            catch (TimeoutException | UnhandledAlertException ex) {
               logger.error("Timeout Sending message");
               throw ex;
            }
            catch (WebDriverException ex) {
                logger.error("Driver communication problem");
                throw ex;
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

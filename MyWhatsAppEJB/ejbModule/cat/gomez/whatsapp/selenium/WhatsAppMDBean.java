package cat.gomez.whatsapp.selenium;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Message-Driven Bean implementation class for: myWhatsAPPMessageConsumer
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "java:/myJmsTest/MyQueue"
        )
})

public class WhatsAppMDBean implements MessageListener {

    @Inject
    public WhatsApp myWhatsApp;    
    /**
     * @see MessageListener#onMessage(Message)
     */
    
    @Override
    public void onMessage(Message message) {

        /*final WebDriver driver;

        System.setProperty("webdriver.chrome.driver", "C:/Users/Javier/git/myWhatsAppSeleniumProject/MyWhatsAppSeleniumProj/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=" + "C:/Users/Javier/tmp");
        driver = new ChromeDriver(options);*/
        
        String phoneNb = "34644016790";
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                //
                // Send message using injected object
                //
               myWhatsApp.send(phoneNb, textMessage.getText());

/*                driver.get("https://api.whatsapp.com/send?phone="+phoneNb+"&text="+textMessage.getText());

                WebElement mySendButton = (new WebDriverWait(driver, 10))
                        .until(ExpectedConditions.presenceOfElementLocated(By.id("action-button")));      
                // Click Send button in API page and wait for main Whatsapp Interface page with new Send button
                mySendButton.click();      
                WebElement myDynamicElement = (new WebDriverWait(driver, 100))
                        .until(ExpectedConditions.presenceOfElementLocated(By.className("compose-btn-send")));        
                myDynamicElement.click();
                return;*/
            }
           catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


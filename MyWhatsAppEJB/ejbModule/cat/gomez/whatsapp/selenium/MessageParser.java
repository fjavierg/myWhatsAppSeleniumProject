package cat.gomez.whatsapp.selenium;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MessageParser {

    private static final String myNumber="34625369980";
    
    private static String getFrom ( String title, WebElement message) {
        
        String data = message.getAttribute("data-pre-plain-text");
        if (data!=null) {
            String from = data.split("] ")[1];
            from = from.substring(0, from.length() - 2);
            return from;
        }
        
        List<WebElement> fromElements = message.findElements(By.className("_111ze"));
        if (fromElements.size()>0) {
            String from = fromElements.get(0).getText();
            return from;
        }
        
        return title;
        
    }
    
    private static Date getDate (String title,WebElement message ) {
        
        try {
            String data = message.getAttribute("data-pre-plain-text");
            if (data!=null) {
                SimpleDateFormat formatter = new SimpleDateFormat("[HH:mm, dd/MM/yyyy]");
                return formatter.parse(data);
            }
            
            String time = message.findElement(By.className("bubble-image-meta")).getText();
            String now = new SimpleDateFormat("dd-MM-yyyy ").format(new Date());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            return formatter.parse(now+time);
        }
        catch (ParseException e)
        {
          e.printStackTrace();
          return null;
        }
        
    }

    public static void parseChat(WhatsApp myWhatsApp, Events events, String title, WebElement element) {
        
            WebElement message = element.findElement(By.className("bubble-text"));
            
            String from = getFrom(title,message);
            String to = title;
            if (title.equals(from)) {
                to = myNumber;
            }
            
            List<WebElement> textElements = message.findElements(By.className("_3zb-j"));
            List<WebElement> linkElements = message.findElements(By.tagName("a"));
            String link ="";
            if (!linkElements.isEmpty()) {link = linkElements.get(0).getAttribute("href");}
        

          events.onGetMessage(myWhatsApp, to, from, getDate (title, message), textElements.get(0).getText(),link);

    }
        
    public static void parseImage(WhatsApp myWhatsApp, Events events, String title, WebElement element, WebDriver driver) {
        String caption;

        
        WebElement message = element.findElement(By.className("bubble-image"));
        
        String from = getFrom(title,message);
        String to = title;
        if (title.equals(from)) {
            to = myNumber;
        }

        List<WebElement> textElements = message.findElements(By.className("bubble-image-caption-text"));
        if (textElements.size()>0) {
            caption = textElements.get(0).getText();
        } else {
            caption = "";
        }
        
        WebElement image = message.findElement(By.tagName("img"));
        String link = image.getAttribute("src");

        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver)
                .executeScript("var a = document.createElement(\"a\");" +
                                "document.body.appendChild(a);" + 
                                "a.style = \"display: none\";" +
                                "a.href = '" + link + 
                                "';a.download = '" + link +
                                ".jpg';a.click();");
        }

        events.onGetImage(myWhatsApp, to, from, getDate(title,message), caption,link,0,0,caption);
    
    }
    
    
}

package org.openqa.selenium.example;

public class WhatsAppMain {
    
    public static void main(String[] args) {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        WhatsApp myWhatsApp = new WhatsApp();

        myWhatsApp.send("34644016790","Hello world");
       
        //Close the browser
        //driver.quit();
    }    
}

package cat.gomez.whatsapp.selenium;

public class MainTest {
    public final static WhatsAppTest whatsApp = new WhatsAppTest();
    

    public static void main(String[] args) {

        //whatsApp.send("34644016790","Hello world");
        while (true) {
            whatsApp.read();
        }


    }

}

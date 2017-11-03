package cat.gomez.whatsapp.selenium;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

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

    @Override
    public void onMessage(Message message) {

        if (message instanceof MapMessage) {
            MapMessage mapMessage = (MapMessage) message;
            try {
                //
                // Send message using injected object
                //
               myWhatsApp.send(mapMessage.getString("nb"), mapMessage.getString("msg"));

            }
           catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


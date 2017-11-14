package cat.gomez.whatsapp.selenium;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private final Logger logger = LoggerFactory.getLogger(WhatsAppMDBean.class);
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
                logger.info("New JMS message : " + mapMessage.toString());
               myWhatsApp.send(mapMessage.getString("nb"), mapMessage.getString("msg"));

            }
           catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


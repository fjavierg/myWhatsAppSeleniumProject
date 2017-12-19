package cat.gomez.whatsapp.selenium;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEvents {
    
    protected final Logger logger = LoggerFactory.getLogger(WhatsApp.class);

    public void onGetAudio(String to,String from,Date date,String name,Integer size,String url,String file,Integer duration,String acodec){
        
    }
    
    public void onGetImage(WhatsApp myWhatsApp, String to,String from,Date date,String name,String url,Integer width,Integer height,String caption){
        
        logger.info("onGetImage  -> New image @ "+date+" from:"+from+" to:" +to+" caption:"+caption+" link:"+url);
        
    }

    public void onGetLocation(String to,String from,Date date,String name,String author,Float longitude,Float latitude,String url){
        
    }

    public void onGetMessage(WhatsApp myWhatsApp, String to,String from,Date date,String body, String url){    

        logger.info("onGetMessage  -> New image @ "+date+" from:"+from+" to:" +to+" link:"+url);

    }
}

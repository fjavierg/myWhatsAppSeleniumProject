package cat.gomez.whatsapp.selenium;

import java.util.Date;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class Events {
    
    static AIDataService dataService;
    
    public Events(String key) {
        
        AIConfiguration configuration = new AIConfiguration(key);
        dataService = new AIDataService(configuration);
    }

    public static void onGetAudio(WhatsAppTest whatsapp,String to,String from,Date date,String name,Integer size,String url,String file,Integer duration,String acodec){
       
    }
    
    public static void onGetImage(WhatsAppTest whatsapp,String to,String from,Date date,String name,String url,Integer width,Integer height,String caption){
        
        System.out.println("onGetImage  -> New image @ "+date+" from:"+from+" to:" +to+" caption:"+caption+" link:"+url);
        
    }

    public static void onGetLocation(WhatsAppTest whatsapp, String to,String from,Date date,String name,String author,Float longitude,Float latitude,String url){
        
    }

    public static void onGetMessage(WhatsAppTest whatsapp, String to,String from,Date date,String body, String url){    
           
        String dest;

        System.out.println("onGetMessage -> New message @ "+date+" from:"+from+" to:"+to+" body:"+body+" link:"+url);
        
        if (to.replaceAll("[^0-9]+","")!=MessageParser.MYNUMBER) { //Message sent to group to
            dest = to;
        } else {
            dest= from;
            
        }
        try {
            AIRequest request = new AIRequest(body);

            AIResponse response = dataService.request(request);

            if (response.getStatus().getCode() == 200) {
                whatsapp.sendNew(dest,response.getResult().getFulfillment().getSpeech());
            } else {
                whatsapp.sendNew(dest,response.getStatus().getErrorDetails());
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
    }
}

package cat.gomez.whatsapp.selenium;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

@ApplicationScoped
public class Events extends AbstractEvents{
    
    @Inject
    @Property("DIALOGFLOW_KEY")
    private String dialogflowKey;
    @Inject
    @Property("MYNUMBER")
    private String myNumber;
    
    private Map<String,AIDataService> dataServiceMap = new HashMap<String, AIDataService>();
    private AIConfiguration configuration;

    @PostConstruct
    public void init () {
        configuration = new AIConfiguration(dialogflowKey);
    }
    
    public void onGetMessage(WhatsApp myWhatsApp, String to,String from,Date date,String body, String url){    

        logger.info("onGetMessage -> New message @ "+date+" from:"+from+" to:"+to+" body:"+body+" link:"+url);
        
        String dest = (to.replaceAll("[^0-9]+","")!=myNumber) ? to : from ;
        
        AIDataService dataService = dataServiceMap.get(dest);
        if (dataService == null) {
            dataService = new AIDataService(configuration);
            dataServiceMap.put(dest, dataService);
        }
        
        try {
            AIRequest request = new AIRequest(body);
            AIResponse response = dataService.request(request);

            if (response.getStatus().getCode() == 200) {
               myWhatsApp.sendNew(dest,response.getResult().getFulfillment().getSpeech());
            } else {
               myWhatsApp.sendNew(dest,response.getStatus().getErrorDetails());
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
    }
}

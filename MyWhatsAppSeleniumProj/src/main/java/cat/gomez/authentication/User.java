package cat.gomez.authentication;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    private String id;
    private String secret;

    User(){
     }
    
    User(String id, String secret){
       this.id = id;
       this.secret = secret;
    }

    public String getId() {
       return id;
    }

    public void setId(String id) {
       this.id = id;
    }

    public String getSecret() {
       return secret;
    }

    public void setSecret(String secret) {
       this.secret = secret;
    }
    @Override public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", secret='" + secret +
                "}";
    }
 }

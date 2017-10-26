package cat.gomez.authentication;

public class User {
    private String id;
    private String secret;

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

    public void setRollNo(String secret) {
       this.secret = secret;
    }
 }

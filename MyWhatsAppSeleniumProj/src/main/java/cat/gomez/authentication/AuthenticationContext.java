package cat.gomez.authentication;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationContext {
    private Authentication authentication;

    public AuthenticationContext(Authentication authentication){
       this.authentication = authentication;
    }

    public boolean executeVerify(HttpServletRequest req){
       return authentication.verify(req);
    }

    
 }

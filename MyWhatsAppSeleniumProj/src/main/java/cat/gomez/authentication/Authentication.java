package cat.gomez.authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Authentication interface to be implemented by diferent authentication schemas
 * 
 * 
*/

public interface Authentication {
    public boolean verify(HttpServletRequest req);
}

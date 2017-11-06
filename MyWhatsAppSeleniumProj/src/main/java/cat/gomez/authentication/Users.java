package cat.gomez.authentication;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Users {
    
    // XmlElement sets the name of the entities
    @XmlElement(name = "user")
    private List<User> userlist;
    
    public void setUserList(List<User> userList) {
        this.userlist = userList;
    }

    public List<User> getUsersList() {
        return userlist;
    }
}

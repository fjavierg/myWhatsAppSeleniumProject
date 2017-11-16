package cat.gomez.whatsapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Bulletin {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String message;
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Bulletin() {
        super();
    }
    public Bulletin(String message) {
        super();
        this.message = message;
    }
    @Override
    public String toString() {
        return "Bulletin [id=" + id + ", message=" + message + "]";
    }
}

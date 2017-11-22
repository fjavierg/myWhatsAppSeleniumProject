package cat.gomez.whatsapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(
            name="findUserByUserid",
            query="SELECT myuser FROM User myuser WHERE myuser.userid = :userid"
        ),
    @NamedQuery(
            name="findAll",
            query="SELECT myuser FROM User myuser"
        )
})
public class User implements Serializable {
    
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "userid", nullable = false, columnDefinition = "varchar(25)", unique = true)
    private String userid;
    @Column(name = "secret", nullable = false, columnDefinition = "varchar(255)")
    private String secret;
    private Date created;
    private Date updated;

    @PrePersist
    protected void onCreate() {
      created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }
    
    User(){
     }
    
    User(String userid, String secret){
       this.userid = userid;
       this.secret = secret;
    }
    
    public Long getId() {
       return id;
    }

    public void setId(Long id) {
       this.id = id;
    }
       
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
                "userid = " + userid +
                ", secret='" + secret +
                ", created='" + created +
                ", updated='" + updated +
                "}";
    }
 }

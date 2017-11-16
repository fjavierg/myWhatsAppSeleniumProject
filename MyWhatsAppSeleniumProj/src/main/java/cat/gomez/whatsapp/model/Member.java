package cat.gomez.whatsapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Member {

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;
    @Column(name = "mobilenb", nullable = false, columnDefinition = "varchar(255)")
    private String mobileNb;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobileNb() {
        return mobileNb;
    }
    public void setMobileNb(String mobileNb) {
        this.mobileNb = mobileNb;
    }
    public Member() {
        super();
    }
    public Member(String name, String mobileNb) {
        super();
        this.name = name;
        this.mobileNb = mobileNb;
    }
    @Override
    public String toString() {
        return "Member [id=" + id + ", name=" + name + ", mobileNb=" + mobileNb + "]";
    }
}

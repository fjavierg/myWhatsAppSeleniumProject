package cat.gomez.whatsapp.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class DistributionList {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;
    @ManyToOne
    private User owner;
    @ManyToMany
    private List<Member> destinations;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public User getOwner() {
        return owner;
    }
    public void setOwner(User owner) {
        this.owner = owner;
    }
    public List<Member> getDestinations() {
        return destinations;
    }
    public void setDestinations(List<Member> destinations) {
        this.destinations = destinations;
    }
    
    public DistributionList() {
        super();
    }
    public DistributionList(String name, User owner, List<Member> destinations) {
        super();
        this.name = name;
        this.owner = owner;
        this.destinations = destinations;
    }
    @Override
    public String toString() {
        return "DistributionList [id=" + id + ", name=" + name + ", owner=" + owner + ", destinations=" + destinations
                + "]";
    }

    
    
}

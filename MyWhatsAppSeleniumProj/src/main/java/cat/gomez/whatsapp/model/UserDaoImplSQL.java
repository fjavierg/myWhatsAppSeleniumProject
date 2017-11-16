package cat.gomez.whatsapp.model;

import java.io.FileNotFoundException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;

public class UserDaoImplSQL implements UserDao {

    @PersistenceContext
    EntityManager entitymanager;
    
    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUser(String id) {
        
        User user = entitymanager.find( User.class, id );
        return user;
    }

    @Override
    public void addUser(User user) {
        entitymanager.persist(user);
        // TODO Auto-generated method stub

    }
    
    @Override
    public void updateUser(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUser(User user) {
        // TODO Auto-generated method stub

    }

}

package cat.gomez.authentication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UserDaoImplXML implements UserDao {
    
    private static final String USERSTORE_XML = "/user-jaxb.xml";

    @Override
    public List<User> getAllUsers() throws JAXBException, FileNotFoundException {
     // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Unmarshaller um = context.createUnmarshaller();
        Users userstore2 = (Users) um.unmarshal( Users.class.getClassLoader().getResourceAsStream(USERSTORE_XML));
        return userstore2.getUsersList();
    }

    @Override
    public User getUser(String id) throws FileNotFoundException, JAXBException {
        List<User> list = this.getAllUsers();
        Optional<User> user = list.stream()
                .filter(myuser -> myuser.getId().equals(id))
                .findFirst();
        if (user.isPresent()) {
            return user.get();
          } else {
            return null;
          }
        //return new User(id,"changeit");
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

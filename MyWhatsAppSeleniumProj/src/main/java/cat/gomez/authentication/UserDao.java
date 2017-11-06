package cat.gomez.authentication;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface UserDao {
   public List<User> getAllUsers() throws JAXBException, FileNotFoundException;
   public User getUser(String id) throws FileNotFoundException, JAXBException;
   public void updateUser(User user);
   public void deleteUser(User user);
}

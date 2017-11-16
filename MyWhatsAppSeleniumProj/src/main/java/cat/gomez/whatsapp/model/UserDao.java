package cat.gomez.whatsapp.model;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface UserDao {
   public List<User> getAllUsers();
   public User getUser(String id);
   public void addUser(User user);
   public void updateUser(User user);
   public void deleteUser(User user);
}

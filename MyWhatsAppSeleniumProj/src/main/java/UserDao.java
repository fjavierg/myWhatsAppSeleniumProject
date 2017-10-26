import java.util.List;

import cat.gomez.authentication.User;

public interface UserDao {
   public List<User> getAllUsers();
   public User getUser(String id);
   public void updateUser(User user);
   public void deleteUser(User user);
}

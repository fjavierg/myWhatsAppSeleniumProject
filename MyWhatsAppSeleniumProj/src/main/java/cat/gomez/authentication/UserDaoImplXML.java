package cat.gomez.authentication;

import java.util.List;

public class UserDaoImplXML implements UserDao {

    @Override
    public List<User> getAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUser(String id) {
        // TODO Auto-generated method stub
        return new User(id,"changeit");
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

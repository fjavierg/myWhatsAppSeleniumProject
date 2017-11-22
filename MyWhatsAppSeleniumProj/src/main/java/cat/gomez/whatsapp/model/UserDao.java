package cat.gomez.whatsapp.model;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

public interface UserDao extends IGenericDao<User> {

    User getUser(String id);

}

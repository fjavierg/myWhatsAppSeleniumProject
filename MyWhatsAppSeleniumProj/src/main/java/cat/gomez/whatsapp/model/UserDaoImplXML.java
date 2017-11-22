package cat.gomez.whatsapp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cat.gomez.whatsapp.model.PersistenceImplementation.PersistenceType;

@PersistenceImplementation(PersistenceType.XML)
public class UserDaoImplXML implements UserDao {
    
    private static final String USERSTORE_XML = "C:\\Users\\Javier\\git\\myWhatsAppSeleniumProject\\MyWhatsAppSeleniumProj\\src\\main\\resources\\user-jaxb.xml";

    @Override
    public List<User> findAll() {
     // create JAXB context and instantiate marshaller
        try {
            JAXBContext context = JAXBContext.newInstance(Users.class);
            Unmarshaller um = context.createUnmarshaller();
            //Users userstore2 = (Users) um.unmarshal( Users.class.getClassLoader().getResourceAsStream(USERSTORE_XML));
            Users userstore2 = (Users) um.unmarshal(new FileReader(USERSTORE_XML));
            return userstore2.getUsersList();
        } catch  (FileNotFoundException | JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUser(String id) {
        List<User> list;
        list = this.findAll();

        Optional<User> user = list.stream()
                .filter(myuser -> myuser.getUserid().equals(id))
                .findFirst();
        if (user.isPresent()) {
            return user.get();
          } else {
            return null;
          }
    }

    @Override
    public void create(User user)  {
        
        List<User> users = this.findAll();
        users.add(user);
        
        try {
            JAXBContext context = JAXBContext.newInstance(Users.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            
            Users userstore = new Users();
            userstore.setUserList(users);
            
            m.marshal(userstore, new File(USERSTORE_XML));
        
        } catch  (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public User update(User user) {
        // TODO Auto-generated method stub
        return null;

    }

    @Override
    public void delete(User user) {
        // TODO Auto-generated method stub

    }

    @Override
    public User findOne(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setClazz(Class<User> clazzToSet) {
        // TODO Auto-generated method stub
        
    }


}

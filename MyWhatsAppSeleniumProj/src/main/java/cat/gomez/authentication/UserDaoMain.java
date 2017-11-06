package cat.gomez.authentication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class UserDaoMain {

    private static final String BOOKSTORE_XML = "./src/main/resources/user-jaxb.xml";

    public static void main(String[] args) throws JAXBException, FileNotFoundException {
 
        ArrayList<User> users = new ArrayList<User>();

        // create books
        User user1 = new User("12345678","changeit");
        users.add(user1);
        User user2 = new User("87654321","changeit");
        users.add(user2);


        // create bookstore, assigning book
        Users userstore = new Users();
        userstore.setUserList(users);

        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
        m.marshal(userstore, System.out);

        // Write to File
        m.marshal(userstore, new File(BOOKSTORE_XML));

        // get variables from our xml file, created before
        System.out.println();
        System.out.println("Output from our XML File: ");
        Unmarshaller um = context.createUnmarshaller();
        Users userstore2 = (Users) um.unmarshal(new FileReader(
                BOOKSTORE_XML));
        List<User> list = userstore2.getUsersList();
        for (User user : list) {
            System.out.println("User:: " + user.getId() + " secret "
                    + user.getSecret());
        }
    }

}

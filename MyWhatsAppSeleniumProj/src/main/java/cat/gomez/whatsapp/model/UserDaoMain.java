package cat.gomez.whatsapp.model;

import java.util.List;

public class UserDaoMain {

    public static void main(String[] args) {
        
        UserDao mydao = new UserDaoImplXML();
        
        List<User> mylist = mydao.getAllUsers();       
        System.out.println("Output from our XML File: " + mylist.toString());
        
        User user1 = new User("9999","xxxx");
        mydao.addUser(user1);
        
        mylist = mydao.getAllUsers();       
        System.out.println("Output from our XML File: " + mylist.toString());
 
/*        ArrayList<User> users = new ArrayList<User>();

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
*/
    }

}

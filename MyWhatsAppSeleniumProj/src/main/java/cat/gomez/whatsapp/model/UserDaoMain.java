package cat.gomez.whatsapp.model;

import java.util.List;

public class UserDaoMain {

    public static void main(String[] args) {
        
        UserDao mydao = new UserDaoImplXML();
        
        List<User> mylist = mydao.findAll();       
        System.out.println("Output from our XML File: " + mylist.toString());
        
        User user1 = new User("9999","xxxx");
        mydao.create(user1);
        
        mylist = mydao.findAll();       
        System.out.println("Output from our XML File after creating new Entity: " + mylist.toString());
 
        mydao = new UserDaoImplSQL();
        
        mylist = mydao.findAll();       
        System.out.println("Output from our SQL File: " + mylist.toString());
        
        mydao.create(user1);
        
        mylist = mydao.findAll();       
        System.out.println("Output from our SQL File after creating new Entity: " + mylist.toString());
    }

}

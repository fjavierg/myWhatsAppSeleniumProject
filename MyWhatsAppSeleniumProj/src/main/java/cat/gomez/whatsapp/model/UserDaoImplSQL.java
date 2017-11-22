package cat.gomez.whatsapp.model;

public class UserDaoImplSQL extends AbstractJpaDao<User> implements UserDao {

    @Override
    public User getUser(String id) {
        return (User)entityManager.createNamedQuery("findUserByUserid")
                .setParameter("userid", id)
                .getSingleResult();
    }

    public UserDaoImplSQL() {
        super();
        this.setClazz(User.class);
    }


}

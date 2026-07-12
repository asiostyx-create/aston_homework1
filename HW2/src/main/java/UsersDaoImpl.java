import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UsersDaoImpl implements UsersDao {
    @Override
    public void save(Users users) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.save(users);
        tr.commit();
        session.close();
    }

    @Override
    public void update(Users users) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.update(users);
        tr.commit();
        session.close();
    }

    @Override
    public Users findById(int id) {
        return HibernateUtil.getSessionFactory().openSession().get(Users.class, id);
    }

    @Override
    public List<Users> findAll() {
        List<Users> usersAll = (List<Users>) HibernateUtil.getSessionFactory().openSession().createQuery("From Users").list();
        return usersAll;
    }

    @Override
    public void delete(Users users) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.delete(users);
        tr.commit();
        session.close();

    }
}

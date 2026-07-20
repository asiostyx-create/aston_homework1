import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UsersDaoImpl implements UsersDao {
    private final SessionFactory sessionFactory;

    public UsersDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }


    public UsersDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void save(Users users) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.save(users);
            tr.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Users users) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.update(users);
            tr.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Users findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Users.class, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Users> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("From Users").list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    @Override
    public void delete(Users users) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.delete(users);
            tr.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

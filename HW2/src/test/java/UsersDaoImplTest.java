import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@Testcontainers
class UsersDaoImplTest {
    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static SessionFactory sessionFactory;
    private UsersDaoImpl usersDao;

    @BeforeAll
    static void initHibernate() {
        Configuration configuration = new Configuration().configure();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        sessionFactory = configuration.buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        usersDao = new UsersDaoImpl(sessionFactory);
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            session.createQuery("delete from Users").executeUpdate();
            tr.commit();
        }
    }
    @Test
    void testSaveAndFindById() {
        Users users = new Users("Тест", "test@test.test", 30);
        usersDao.save(users);
        Users retrievedUser = usersDao.findById(users.getId());
        assertNotNull(retrievedUser);
        assertEquals("Тест", retrievedUser.getUsername());
        assertEquals("test@test.test", retrievedUser.getEmail());
        assertEquals(30, retrievedUser.getAge());
    }
    @Test
    void testSaveAndFindAll() {
        Users users1 = new Users("Тест", "test@test.test", 30);
        Users users2 = new Users("Тест1", "test1@test.test", 50);
        usersDao.save(users1);
        usersDao.save(users2);
        List<Users> usersList = usersDao.findAll();
        assertEquals(2, usersList.size());
        List<String> usernames = new ArrayList<>();
        for (Users u : usersList) {
            usernames.add(u.getUsername());
        }
        usernames.sort(String::compareTo);
        assertEquals("Тест", usernames.get(0));
        assertEquals("Тест1", usernames.get(1));
    }

    @Test
    void testSaveAndUpdate() {
        Users users = new Users("Тест", "test@test.test", 30);
        usersDao.save(users);
        int id = users.getId();
        users.setEmail("хоба");
        users.setAge(50);
        usersDao.update(users);
        Users updatedUsers = usersDao.findById(id);
        assertNotNull(updatedUsers);
        assertEquals("Тест", updatedUsers.getUsername());
        assertEquals("хоба", updatedUsers.getEmail());
        assertEquals(50, updatedUsers.getAge());

    }
    @Test
    void testSaveAndDelete() {
        Users users = new Users("Тест", "test@test.test", 30);
        usersDao.save(users);
        int id = users.getId();
        usersDao.delete(users);
        Users updatedUsers = usersDao.findById(id);
        assertNull(updatedUsers);
    }
}

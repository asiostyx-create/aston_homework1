import java.util.List;
import java.util.Optional;

public interface UsersDao {
    void save(Users users);
    void update(Users users);
    Users findById(int id);
    List<Users> findAll();
    void delete(Users users);
}

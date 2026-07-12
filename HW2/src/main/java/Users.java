import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table (name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "age")
    private int age;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Users() {}

    public Users(String username, String email, int age){
        this.username = username;
        this.email = email;
        this.age = age;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

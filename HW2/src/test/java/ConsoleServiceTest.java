import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Scanner;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceTest {
    @Mock
    private UsersDao usersDao;

    @Test
    void testCreateUsersCorrect() {
        String input = "1\nТест\ntest@test.test\n30\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        consoleService.run();
        verify(usersDao).save(argThat(users ->
                users.getUsername().equals("Тест") &&
                        users.getEmail().equals("test@test.test") &&
                        users.getAge() == 30
        ));
    }

    @Test
    void testCreateUsersIncorrect() {
        String input = "1\nТест\ntest@test.test\nтридцать\n30\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        consoleService.run();
        verify(usersDao, times(1)).save(argThat(users ->
                users.getUsername().equals("Тест") &&
                        users.getEmail().equals("test@test.test") &&
                        users.getAge() == 30
        ));
    }

    @Test
    void testReadUsersCorrect() {
        String input = "2\n1\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        Users mockUsers = new Users("Тест", "test@test.test", 30);
        when(usersDao.findById(1)).thenReturn(mockUsers);
        consoleService.run();
        verify(usersDao).findById(1);
    }

    @Test
    void testReadUsersNotFound() {
        String input = "2\n1\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        when(usersDao.findById(1)).thenReturn(null);
        consoleService.run();
        verify(usersDao).findById(1);
    }

    @Test
    void testReadAllUsersCorrect() {
        String input = "3\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        Users mockUsers1 = new Users("Тест", "test@test.test", 30);
        Users mockUsers2 = new Users("Тест", "test@test.test", 30);
        when(usersDao.findAll()).thenReturn(List.of(mockUsers1, mockUsers2));
        consoleService.run();
        verify(usersDao).findAll();
    }
    @Test
    void testReadAllUsersEmpty() {
        String input = "3\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        when(usersDao.findAll()).thenReturn(List.of());
        consoleService.run();
        verify(usersDao).findAll();
    }

    @Test
    void testUpdateUsers() {
        String input = "4\n1\nТест\ntest@test.test\n30\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        Users mockUsers = new Users("Имя", "test1@test.test", 50);
        when(usersDao.findById(1)).thenReturn(mockUsers);
        consoleService.run();
        verify(usersDao).findById(1);
        verify(usersDao).update(argThat(users ->
                users.getUsername().equals("Тест") &&
                        users.getEmail().equals("test@test.test") &&
                        users.getAge() == 30
        ));
    }
    @Test
    void testUpdateUsersNotFound() {
        String input = "4\n1\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        when(usersDao.findById(1)).thenReturn(null);
        consoleService.run();
        verify(usersDao).findById(1);
        verify(usersDao, never()).update(any());
    }

    @Test
    void testDeleteUsersCorrect() {
        String input = "5\n1\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        Users mockUsers = new Users("Имя", "test1@test.test", 30);
        when(usersDao.findById(1)).thenReturn(mockUsers);
        consoleService.run();
        verify(usersDao).findById(1);
        verify(usersDao).delete(mockUsers);
    }
    @Test
    void testDeleteUsersNotFound() {
        String input = "5\n1\n0\n";
        Scanner scanner = new Scanner(input);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        when(usersDao.findById(1)).thenReturn(null);
        consoleService.run();
        verify(usersDao).findById(1);
        verify(usersDao, never()).delete(any());
    }
}
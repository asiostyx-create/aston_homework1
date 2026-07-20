import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UsersDao usersDao = new UsersDaoImpl();
        Scanner scanner = new Scanner(System.in);
        ConsoleService consoleService = new ConsoleService(usersDao, scanner);
        consoleService.run();
    }
}
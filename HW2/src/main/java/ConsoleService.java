import java.util.List;
import java.util.Scanner;

public class ConsoleService {
    private final UsersDaoImpl usersDao = new UsersDaoImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Запуск программы");

        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createUsers();
                case "2" -> readUsers();
                case "3" -> readAllUsers();
                case "4" -> updateUsers();
                case "5" -> deleteUsers();
                case "0" -> {
                    break;
                }
                default -> System.out.println("Неверный ввод.");
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Добавить запись");
        System.out.println("2. Найти запись по id");
        System.out.println("3. Показать все записи");
        System.out.println("4. Обновить запись по id");
        System.out.println("5. Удалить запись по id");
        System.out.println("0. Выйти");
        System.out.println("Выберите опцию: ");
    }

    private void createUsers() {
        System.out.println("Введите имя: ");
        String name = scanner.nextLine();
        System.out.println("Введите email: ");
        String email = scanner.nextLine();

        int age = readIntSafe("Введите возраст: ");

        Users user = new Users(name, email, age);
        usersDao.save(user);
    }

    private void readUsers() {
        int id = readIntSafe("Введите id записи: ");
        Users users = usersDao.findById(id);
        if (users != null) {
            System.out.println("Запись по id" + id + " найдена: " + users.getId() + ", " +
                    users.getUsername() +  ", " +
                    users.getEmail() + ", " +
                    users.getAge() + ", " +
                    users.getCreatedAt());
        } else {
            System.out.println("Такой записи не существуует.");
        }
    }

    private void readAllUsers() {
        List<Users> usersAll = usersDao.findAll();
        if (usersAll.isEmpty()) {
            System.out.println("Нет данных.");
        } else {
            System.out.println("Записи:");
            for(Users users : usersAll) {
                System.out.println(users.getId() + ", " +
                        users.getUsername() +  ", " +
                        users.getEmail() + ", " +
                        users.getAge() + ", " +
                        users.getCreatedAt());
            }
        }
    }

    private void updateUsers() {
        int id = readIntSafe("Введите id записи: ");
        Users users = usersDao.findById(id);
        if (users != null) {
            System.out.println("Введите новое имя: ");
            String username = scanner.nextLine();
            users.setUsername(username);
            System.out.println("Введите новый email: ");
            String email = scanner.nextLine();
            users.setEmail(email);
            int age = readIntSafe("Введите новый возраст: ");
            usersDao.update(users);
            users.setAge(age);
        } else System.out.println("Такой записи не существует.");
    }

    private void deleteUsers() {
        int id = readIntSafe("Введите id записи для удаления: ");
        Users users = usersDao.findById(id);

        if (users != null) {
            usersDao.delete(users);
            System.out.println("Успешно удалено.");
        } else {
            System.out.println("Такой записи не существует.");
        }
    }

    private int readIntSafe(String number) {
        while (true) {
            System.out.println(number);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод.");
            }
        }
    }
    }
public class Main {
    public static void main(String[] args) {

        CustomHashMap<Integer, String> test = new CustomHashMap<>();
        test.put(0, "text1");  // 0 после хэш-функции
        test.put(16, "text2"); // 0 после хэш-функции
        test.put(32, "text3"); // 0 после хэш-функции
        test.put(3, "text4");  // 3 после хэш-функции

        System.out.println(test.get(0));
        System.out.println(test.get(16));
        System.out.println(test.get(32));
        System.out.println(test.get(3));

        test.put(0, "altText1");
        test.remove(16);

        System.out.println(test.get(0));
        System.out.println(test.get(16));
        System.out.println(test.get(32));
        System.out.println(test.get(3));
    }
}
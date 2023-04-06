import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
        int[] prices = {35, 70, 50, 10, 80};
        Basket basket = new Basket(prices, products);
        File textFile = new File("basket.txt");
        textFile.createNewFile();
        if (textFile.length() > 0) {
            basket = Basket.loadFromTxtFile(textFile);
            basket.printCart();
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "Список возможных товаров для покупки");
            for (int i = 0; i < products.length; i++) {
                System.out.printf((i + 1) + ".  %S  %d руб/шт\n", products[i], prices[i]);
            }
            System.out.println("Выберите номер товар и количество + или - или введите `end`");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] parts = input.split(" ");

            if (parts.length != 2) {
                System.out.println(" Ввод должен состоять из двух частей через пробел");
                continue;
            }
            int productCount;
            int productNumber;
            try {
                productNumber = Integer.parseInt(parts[0]) - 1;

                productCount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println(" ошибка вводите только числа");
                continue;
            }
            if (productNumber > 4 || productNumber < 0) {
                System.out.println(" Не корректный ввод номера товара");
                continue;
            }
            basket.addToCart(productNumber, productCount);
            basket.saveTxt(textFile);
            basket.printCart();
            System.out.println(basket.toString());
        }
    }
}
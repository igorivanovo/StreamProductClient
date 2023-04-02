import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
        int[] prices = {35, 70, 50, 10, 80};
        ClientLog clientLog = new ClientLog(prices, products);
        File jsonFile = new File("basket.json");
        jsonFile.createNewFile();

        if (jsonFile.length() > 0) {
            Path path = Paths.get("basket.json");
            String contents = null;
            try {
                contents = Files.readString(path, StandardCharsets.UTF_8);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Basket basket = gson.fromJson(contents, Basket.class);
                clientLog.setBasket(basket);
                clientLog.getBasket().printCart();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                clientLog.exportAsCSV(new File("log.csv"));
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

            clientLog.getBasket().addToCart(productNumber, productCount);
            clientLog.log(productNumber, productCount);
            clientLog.getBasket().printCart();

            Basket basket = clientLog.getBasket();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            try (FileWriter file = new FileWriter("basket.json")) {
                file.write(gson.toJson(basket));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
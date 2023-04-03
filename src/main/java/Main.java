import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    static String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
    static int[] prices = {35, 70, 50, 10, 80};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        XMLSettingsReader settingsReader = new XMLSettingsReader(new File("shop.xml"));
        File loadFile = new File(settingsReader.loadFile);
        File saveFile = new File(settingsReader.saveFile);
        File logFile = new File(settingsReader.logFile);

        ClientLog clientLog = createClientLog(loadFile, settingsReader.isLoad, settingsReader.loadFormat);
        System.out.println("загрузка : " + settingsReader.isLoad+ "  из : " + settingsReader.loadFile);
        System.out.println("запись : " + settingsReader.isSave+  "  в : "+ settingsReader.saveFile);
        clientLog.getBasket().printCart();
        logFile.delete();
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
                if (settingsReader.isLog) {
                    clientLog.exportAsCSV(logFile);
                    System.out.println(logFile);
                }
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
            if (settingsReader.isLog) {
                clientLog.log(productNumber, productCount);
            }
            if (settingsReader.isSave) {
                switch (settingsReader.saveFormat) {
                    case "txt" -> clientLog.getBasket().saveTxt(saveFile);
                    case "json" -> {
                        Basket basket = clientLog.getBasket();
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        try (FileWriter file = new FileWriter(saveFile)) {
                            file.write(gson.toJson(basket));
                            file.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            clientLog.getBasket().printCart();
        }
    }

    private static ClientLog createClientLog(File loadFile, boolean isLoad, String loadFormat) {
        ClientLog clientLog = new ClientLog(prices, products);
        if (isLoad && loadFile.exists()) {
            switch (loadFormat) {
                case "json" -> clientLog = loadFromJCONFile(loadFile);
                case "txt" -> clientLog.setBasket(Basket.loadFromTxtFile(loadFile));
            }
            return clientLog;
        } else {
        }
        return clientLog;
    }

    private static ClientLog loadFromJCONFile(File loadFile) {
        ClientLog clientLog = new ClientLog(prices, products);
        Path path = Paths.get(String.valueOf(loadFile));
        String contents = null;
        try {
            contents = Files.readString(path, StandardCharsets.UTF_8);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Basket basket = gson.fromJson(contents, Basket.class);
            clientLog.setBasket(basket);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return clientLog;

    }
}
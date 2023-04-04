
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
    private static int[] prices = {35, 70, 50, 10, 80};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        XMLSettingsReader settingsReader = new XMLSettingsReader(new File("shop.xml"));
        File loadFile = new File(settingsReader.getLoadFile());
        File saveFile = new File(settingsReader.getSaveFile());
        File logFile = new File(settingsReader.getLogFile());
        ClientLog clientLog0 = new ClientLog(prices, products);
        ClientLog clientLog = clientLog0.createClientLog(loadFile, settingsReader.isLoad(), settingsReader.getLoadFormat());
        System.out.println("загрузка : " + settingsReader.isLoad() + "  из : " + settingsReader.getLoadFile());
        System.out.println("запись : " + settingsReader.isSave() + "  в : " + settingsReader.getSaveFile());
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
                if (settingsReader.isLog()) {
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
            if (settingsReader.isLog()) {
                clientLog.log(productNumber, productCount);
            }
            if (settingsReader.isSave()) {
                switch (settingsReader.getSaveFormat()) {
                    case "txt" -> clientLog.getBasket().saveTxt(saveFile);
                    case "json" -> {
                        clientLog.getBasket().saveJSON(saveFile);
                    }
                }
            }
            clientLog.getBasket().printCart();
        }
    }
}
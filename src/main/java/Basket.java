import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Basket {
    protected static String basket1 = "";
    protected static int[] prices;
    protected static String[] products;
    protected int sumProducts;
    protected static Map<String, Integer> map;

    protected Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        map = new HashMap<>();
    }

    public Basket() {
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public int[] getPrices() {
        return prices;
    }

    public String[] getProducts() {
        return products;
    }

    public static String getBasket1() {
        return basket1;
    }

    public static void setMap(Map<String, Integer> map) {
        Basket.map = map;
    }

    protected void addToCart(int productNum, int amount) {//добавление в корзину
        String k = products[productNum];
        if (map.isEmpty()) {
            map.put(products[productNum], amount);
        } else if (map.containsKey(k)) {
            int v = map.get(k) + amount;
            if (v <= 0) {
                v = 0;
                map.remove(k);
            } else {
                map.put(products[productNum], v);
            }
        } else {
            map.put(products[productNum], amount);
        }
    }

    protected void printCart() {//вывод корзины
        String[] output = new String[products.length];
        System.out.println("  <<<<<  Ваша корзина:  >>>>>");
        for (String k : map.keySet()) {
            int v = map.get(k);
            for (int j = 0; j < products.length; j++) {
                if (k.equals(products[j])) {
                    output[j] = k;
                    break;
                }
            }
        }
        for (int i = 0; i < output.length; i++) {
            if (output[i] != null) {
                System.out.printf(
                        (i + 1) + ".  %s  %dшт  %d руб/шт  %d руб в сумме\n",
                        output[i], map.get(output[i]), prices[i], prices[i] * map.get(output[i]));
                sumProducts += (prices[i] * map.get(output[i]));
            }
        }
        System.out.println("Итого " + sumProducts + " руб.");
        System.out.println();
        sumProducts = 0;

    }

    protected static void saveTxt(File textFile) throws IOException {

        StringBuilder baskett = new StringBuilder();
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (int price : prices) {
                baskett.append(price + " ");
                out.print(price + " ");


            }
            out.println();
            for (String product : products) {
                out.print(product + " ");
                baskett.append(product + " ");
            }
            out.println();
            for (Map.Entry<String, Integer> kv : map.entrySet()) {
                String text = kv.getKey() + " " + kv.getValue();
                baskett.append(text + " ");
                out.write(text);
                out.write("\n");
                out.flush();
            }
            basket1 = baskett.toString();
        }
    }

    protected static Basket loadFromTxtFile(File textFile) {
        String s;
        Basket basket = new Basket();
        basket.map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            //чтение построчно
            String priceStr = br.readLine();
            String productStr = br.readLine();
            basket.prices = Arrays.stream(priceStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            basket.products = productStr.split(" ");
            while ((s = br.readLine()) != null) {
                String[] parts = s.split(" ");
                String product = parts[0];
                int count = Integer.parseInt(parts[1]);
                basket.map.put(product, count);

            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return basket;
    }

}
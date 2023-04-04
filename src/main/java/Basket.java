import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Basket {
    private int[] prices;
    private String[] products;
    private static int sumProducts;
    private Map<String, Integer> map;

    protected Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        map = new HashMap<>();
    }

    public static int getSumProducts() {
        return sumProducts;
    }

    public int[] getPrices() {
        return prices;
    }

    public String[] getProducts() {
        return products;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public Basket() {
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

    protected void saveTxt(File saveFile) throws IOException {
        try (PrintWriter out = new PrintWriter(saveFile);) {
            for (int price : prices) {
                out.print(price + " ");
            }
            out.println();
            for (String product : products) {
                out.print(product + " ");
            }
            out.println();
            for (Map.Entry<String, Integer> kv : map.entrySet()) {
                String text = kv.getKey() + " " + kv.getValue();
                out.write(text);
                out.write("\n");
                out.flush();
            }
        }
    }

    protected void saveJSON(File saveFile) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter file = new FileWriter(saveFile)) {
            file.write(gson.toJson(this));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static Basket loadFromTxtFile(File loadFile) {
        String s;
        Basket basket = new Basket();
        basket.map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(loadFile))) {
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
            System.out.println(ex.getMessage());
        }
        return basket;
    }

    protected static Basket loadFromJCONFile(File loadFile) {
        Basket basket = null;
        Path path = Paths.get(String.valueOf(loadFile));
        String contents = null;
        try {
            contents = Files.readString(path, StandardCharsets.UTF_8);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            basket = gson.fromJson(contents, Basket.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basket;
    }

}
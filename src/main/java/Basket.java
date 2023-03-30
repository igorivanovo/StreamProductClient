import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int[] prices;
    protected String[] products;
    protected int sumProducts;
    protected Map<String, Integer> map;

    protected Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        map = new HashMap<>();
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

    protected void saveBin(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }//для сохранения в файл в бинарном формат

    protected static Basket loadFromBinFile(File file) {
        Basket basket = null;

// откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // десериализуем объект и скастим его в класс
            basket = (Basket) ois.readObject();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return basket;
    }
}
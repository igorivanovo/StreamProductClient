import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {

    @Test
    public void testAddToCart() {
        Basket basket ;
        int[] prices = {35, 70, 50, 10, 80};
        String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
        basket = new Basket(prices, products);

        basket.addToCart(0, 5);
        basket.addToCart(1, 4);

        String actual = basket.getMap().entrySet().toString();
        String expected = "[Хлеб=5, Молоко=4]";

        Assertions.assertEquals(expected, actual);


    }

    @Test
    public void testsaveTxt() throws IOException {
        int[] prices = {35, 70, 50, 10, 80};
        String[] products = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};

        Basket basket = new Basket(prices,products);

        basket.map.put("Хлеб",5);
        basket.map.put("Молоко",4);

        basket.saveTxt(new File("src/test/resources/test_basket.txt"));

        String expectedlbasket = Basket.getBasket1();
        String actualbasced = "35 70 50 10 80 Хлеб Молоко Сахар Соль Нагетсы Хлеб 5 Молоко 4 ";
        Assertions.assertEquals(expectedlbasket, actualbasced);

    }


    @Test
    void testLoadFromTxtFile() {
        Basket basket = Basket.loadFromTxtFile(new File("src/test/resources/test_basket.txt"));
        int[] expectedprices = {35, 70, 50, 10, 80};
        int[] actualprices = basket.getPrices();

        Assertions.assertArrayEquals(expectedprices, actualprices);

        String[] expectedproducts = {"Хлеб", "Молоко", "Сахар", "Соль", "Нагетсы"};
        String[] actualproducts = basket.getProducts();

        Assertions.assertArrayEquals(expectedproducts, actualproducts);

        String expected = "[Хлеб=5, Молоко=4]";
        String actual = basket.getMap().entrySet().toString();

        Assertions.assertEquals(expected, actual);
    }

}
import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    protected Basket basket;

    public ClientLog(int[] prices, String[] products) {
        basket = new Basket(prices, products);
    }
    private String log = "";
    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public void log(int productNum, int amount) {  // сохранение операций В журнал
        productNum ++;
        log += String.format("%d,%d%n", productNum, amount);

    }

    protected void exportAsCSV(File csvFile){     //  сохранение всего журнала действий в формате csv
        if (!csvFile.exists()) {
            log = "productNum,amount\n" + log;
        }
        try(FileWriter writer = new FileWriter(csvFile,true)){
            writer.write(log);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}


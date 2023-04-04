import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    protected Basket basket;
    private String log = "";

    public ClientLog(int[] prices, String[] products) {

        basket = new Basket(prices, products);
    }

    public ClientLog() {

    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    StringBuilder builder = new StringBuilder();

    public void log(int productNum, int amount) {  // сохранение операций В журнал
        productNum++;
        log = String.format("%d,%d%n", productNum, amount);
        log = String.valueOf(builder.append(log));
    }

    protected void exportAsCSV(File logFile) {     //  сохранение всего журнала действий в формате csv
        if (!logFile.exists()) {
            log = "productNum,amount\n" + log;
        }
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    protected ClientLog createClientLog(File loadFile, boolean isLoad, String loadFormat) {
        ClientLog clientLog = new ClientLog();
        if (isLoad && loadFile.exists()) {

            switch (loadFormat) {
                case "json" -> clientLog.setBasket(Basket.loadFromJCONFile(loadFile));
                case "txt" -> clientLog.setBasket(Basket.loadFromTxtFile(loadFile));


            }
            return clientLog;

        } else {
            clientLog = new ClientLog(basket.getPrices(), basket.getProducts());
        }
        return clientLog;
    }

}


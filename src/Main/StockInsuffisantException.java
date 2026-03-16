package Main;

// Exception métier
public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException(String message) { super(message); }
}


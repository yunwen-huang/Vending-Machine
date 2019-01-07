package vendingmachine.exception;

/**
 * Show exception message when there is not enough stock to buy
 */
public class NotEnoughInventoryException extends Exception {

    public NotEnoughInventoryException(String message) {
        super(message);
    }

    public NotEnoughInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}

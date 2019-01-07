package vendingmachine.exception;

/**
 * Show exception message when there is not enough money to buy
 */
public class NotEnoughFundException extends Exception {

    public NotEnoughFundException(String message) {
        super(message);
    }

    public NotEnoughFundException(String message, Throwable cause) {
        super(message, cause);
    }
}

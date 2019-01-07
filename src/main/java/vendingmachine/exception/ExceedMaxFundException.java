package vendingmachine.exception;

/**
 * Show exception message when funds input exceed the max allowance
 */
public class ExceedMaxFundException extends Exception {

    public ExceedMaxFundException(String message) {
        super(message);
    }

    public ExceedMaxFundException(String message, Throwable cause) {
        super(message, cause);
    }
}

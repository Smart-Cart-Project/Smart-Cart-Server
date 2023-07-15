package smart.cart.server.app.exception;

/**
 * Exception to indicate that the class static/normal initialization has failed.
 */
public class InitializationException extends RuntimeException {
    /**
     * Constructs an InitializationException with the specified detail message.
     *
     * @param message the detail message.
     */
    public InitializationException(String message) {
        super(message);
    }

    /**
     * Constructs an InitializationException with no detail message.
     */
    public InitializationException() {
        super("Class initialization failed!");
    }
}

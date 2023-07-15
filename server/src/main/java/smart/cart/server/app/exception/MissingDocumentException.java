package smart.cart.server.app.exception;

/**
 * Exception to indicate that the document is not found in the Firebase.
 */
public class MissingDocumentException extends RuntimeException {
    /**
     * Constructs a MissingDocumentException with the specified detail message.
     *
     * @param message the detail message.
     */
    public MissingDocumentException(String message) {
        super(message);
    }

    /**
     * Constructs an MissingDocumentException with no detail message.
     */
    public MissingDocumentException() {
        super();
    }
}

package src.Exceptions;

/**
 * Class representing the InvalidGenerationsParameter throwable
 */
public class InvalidGenerationsParameter extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidGenerationsParameter(String message, boolean success) {
        super(message, success);
    }
}

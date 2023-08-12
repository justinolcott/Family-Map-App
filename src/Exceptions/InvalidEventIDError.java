package src.Exceptions;

/**
 * Class representing the InvalidEventIDError throwable
 */
public class InvalidEventIDError extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidEventIDError(String message, boolean success) {
        super(message, success);
    }
}

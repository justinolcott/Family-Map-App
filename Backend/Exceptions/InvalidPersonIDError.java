package src.Exceptions;

/**
 * Class representing the InvalidPersonIDError throwable
 */
public class InvalidPersonIDError extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidPersonIDError(String message, boolean success) {
        super(message, success);
    }
}

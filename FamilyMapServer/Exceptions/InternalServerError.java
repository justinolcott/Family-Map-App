package src.Exceptions;

/**
 * Class representing the InternalServerError throwable
 */
public class InternalServerError extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InternalServerError(String message, boolean success) {
        super(message, success);
    }
}

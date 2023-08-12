package src.Exceptions;

/**
 * Class representing the RequestPropertyError throwable
 */
public class RequestPropertyError extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public RequestPropertyError(String message, boolean success) {
        super(message, success);
    }
}

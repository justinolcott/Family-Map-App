package src.Exceptions;

/**
 * Class representing the InvalidAuthToken throwable
 */
public class InvalidAuthToken extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidAuthToken(String message, boolean success) {
        super(message, success);
    }
}

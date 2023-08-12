package src.Exceptions;

/**
 * Class representing the InvalidUsername throwable
 */
public class InvalidUsername extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidUsername(String message, boolean success) {
        super(message, success);
    }
}

package src.Exceptions;

/**
 * Class representing the InvalidUserBelonging throwable
 */
public class InvalidUserBelonging extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidUserBelonging(String message, boolean success) {
        super(message, success);
    }
}

package src.Exceptions;

/**
 * Class representing the UsernameAlreadyTakenError throwable
 */
public class UsernameAlreadyTakenError extends HandlerError {

    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public UsernameAlreadyTakenError(String message, boolean success) {
        super(message, success);
    }
}

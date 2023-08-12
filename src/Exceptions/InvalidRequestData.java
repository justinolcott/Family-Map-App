package src.Exceptions;

/**
 * Class representing the InvalidRequestData throwable
 */
public class InvalidRequestData extends HandlerError {
    /**
     * Constructor that creates an error result
     *
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public InvalidRequestData(String message, boolean success) {
        super(message, success);
    }
}

package src.Exceptions;

import src.Result.Result;

/**
 * Parent class of Handler Errors
 */
public class HandlerError extends Exception {
    Result result;

    /**
     * Constructor that creates an error result
     * @param message the error message to add
     * @param success the boolean status of success
     */
    public HandlerError(String message, boolean success) {
        //super(message);
    }

    /**
     * Function that returns the result message
     * @return the result
     */
    public String getResult() {
        return null;
    }



}

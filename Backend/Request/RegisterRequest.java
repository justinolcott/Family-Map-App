package src.Request;

import src.Exceptions.InvalidRequestData;
import src.Exceptions.RequestPropertyError;
import src.Model.User;

public class RegisterRequest extends Request {
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) throws RequestPropertyError {

    }

    public RegisterRequest() {

    }

    /**
     * Function that checks whether a request is valid
     * @throws RequestPropertyError if the request is invalid
     */
    public void checkRequest() throws RequestPropertyError {

    }

    /**
     * Function that returns the user object of the request
     * @return the user
     */
    public User modelUser() {
        return null;
    }
}

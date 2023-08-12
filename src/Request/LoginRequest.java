package src.Request;

import src.Exceptions.InvalidRequestData;
import src.Exceptions.RequestPropertyError;
import src.Model.User;

public class LoginRequest extends Request {
    public LoginRequest(String username, String password) throws RequestPropertyError {

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

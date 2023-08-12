package src.Services;

import src.Exceptions.InternalServerError;
import src.Exceptions.RequestPropertyError;
import src.Model.AuthToken;
import src.Request.LoginRequest;

public class LoginService {

    /**
     * Function that will log in a user
     * @param r the login request
     * @return the auth token returned from the login
     * @throws RequestPropertyError if there is a request property error
     * @throws InternalServerError if there is an internal server error
     */
    public AuthToken login(LoginRequest r) throws RequestPropertyError, InternalServerError {
        return null;
    }
}

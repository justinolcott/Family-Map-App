package src.Services;
import src.Exceptions.InternalServerError;
import src.Exceptions.RequestPropertyError;
import src.Exceptions.UsernameAlreadyTakenError;
import src.Model.AuthToken;
import src.Model.User;
import src.Request.RegisterRequest;
import src.Result.RegisterResult;

public class RegisterService extends Service {
    User user;
    AuthToken token;

    /**
     * Function that registers a user
     * @param r the request body
     * @return the user object
     * @throws UsernameAlreadyTakenError if the username is already taken
     * @throws RequestPropertyError if there is an error in the request's properties
     * @throws InternalServerError if there is an internal server error
     */
    public User registerUser(RegisterRequest r) throws UsernameAlreadyTakenError, RequestPropertyError, InternalServerError {
        return null;
    }

    /**
     * Function that creates a user
     * @throws UsernameAlreadyTakenError if the username is already taken
     * @throws RequestPropertyError if there is an error in the request's properties
     * @throws InternalServerError if there is an internal server error
     */
    private void createUser() throws UsernameAlreadyTakenError, RequestPropertyError, InternalServerError {

    }

    /**
     * Creates four generations for the user
     * @throws InternalServerError if there is an internal server error
     */
    private void createGen() throws InternalServerError {

    }

    /**
     * Logs the user in
     * @throws InternalServerError if there is an internal server error
     */
    private void loginUser() throws InternalServerError {
    }


    public AuthToken getAuthToken() {
        return token;
    }


}

package src.Handlers;
import src.Request.RegisterRequest;
import src.Result.RegisterResult;
import src.Request.Request;
import src.Services.RegisterService;

/**
 * Class that represents the handler of the register service
 */
public class RegisterHandler extends Handler {

    /**
     * Function that will call the register Service, handle the errors, and create a result
     * @param body the body of the request
     * @return the result of the register call
     */
    public RegisterResult registerUser(Request body) {
        return null;
    }
}

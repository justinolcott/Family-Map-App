package src.Services;

import src.Request.Request;
import src.Model.AuthToken;

/**
 *  Services will take the input data, confirm the input, perform the action, and then
 *  return the data in object form
 */
public class Service {
    Request request;
    String username;

    AuthToken authtoken;

    public AuthToken getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(AuthToken authtoken) {
        this.authtoken = authtoken;
    }

    /**
     * Function that checks whether the auth token is valid
     * @return the validity of the authtoken
     */
    private boolean checkAuthtoken() {
        return true;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}

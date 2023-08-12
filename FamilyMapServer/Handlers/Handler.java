package src.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import src.Model.User;
import src.Result.Result;

import java.io.IOException;


/*
 * All handlers will call the respective service and then handle errors.
 * They will simply pass on the Request Body and other input
 * They will then HANDLE all the errors and create the response body
 */

/**
 * Parent Class of all Handlers
 */
public class Handler implements HttpHandler {
    User user;
    /**
     * Function that gets the user which will be used by other handlers
     * @return returns the user
     */
    private User getUser() {
        return user;
    }
    /**
     * Function that sets the user of the handler
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

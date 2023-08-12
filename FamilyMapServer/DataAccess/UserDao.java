package src.DataAccess;

import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidUsername;
import src.Model.*;

public class UserDao extends Dao {
    /**
     * Constructor that loads the server object to then use later
     *
     * @param server an object that represents the server
     * @throws InternalServerError if there is an internal server error
     */
    public UserDao(Object server) throws InternalServerError {
        super(server);
    }

    /**
     * Function that gets a user from the database
     * @param username the user to add
     * @return returns the user object
     * @throws InternalServerError  if there is an internal server error
     * @throws InvalidUsername if the username is invalid
     */
    public User getUser(String username) throws InternalServerError, InvalidUsername {
        return null;
    }

    /**
     * Function that adds a user to the database
     * @param user the user to add
     * @throws InternalServerError  if there is an internal server error
     */
    public void addUser(User user) throws InternalServerError {
    }
}

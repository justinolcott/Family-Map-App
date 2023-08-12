package src.DataAccess;

import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidAuthToken;
import src.Model.AuthToken;

/**
 * Class that accesses the database's auth token table
 */
public class AuthTokenDao extends Dao {


    /**
     * Constructor that loads the server object to then use later
     *
     * @param server an object that represents the server
     * @throws InternalServerError if there is an internal server error
     */
    public AuthTokenDao(Object server) throws InternalServerError {
        super(server);
    }

    /**
     * Function that gets an auth token from the database
     * @param token the token as a string
     * @return the token as a model
     * @throws InternalServerError if there is an internal server error
     * @throws InvalidAuthToken if it is an invalid auth token
     */
    public AuthToken getAuthToken(String token) throws InternalServerError, InvalidAuthToken {
        return null;
    }

    /**
     * Function that adds an auth token to the database
     * @param authToken the auth token as a model to add to the database
     * @throws InternalServerError if there is an internal server errror
     */
    public void addAuthToken(AuthToken authToken) throws InternalServerError {

    }

    /**
     * Function that can delete an auth token from the database
     * @param token the token to delete as a string
     * @throws InternalServerError if there is an internal server error
     * @throws InvalidAuthToken if it is an invalid auth token
     */
    public void deleteAuthToken(String token) throws InternalServerError, InvalidAuthToken {

    }
}

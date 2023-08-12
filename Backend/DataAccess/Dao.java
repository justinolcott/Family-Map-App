package src.DataAccess;

import src.Exceptions.InternalServerError;

/**
 * Parent Class to other Data Access Classes
 */
public class Dao {
    Object server;

    /**
     * Constructor that loads the server object to then use later
     * @param server an object that represents the server
     * @throws InternalServerError if there is an internal server error
     */
    public Dao(Object server) throws InternalServerError {
        this.server = server;
    }

    public Dao() {

    }

    /**
     * Function that sends a query to the database or gets
     * @param query the SQL query
     * @return the return string from the database
     * @throws InternalServerError if there is an internal server error
     */
    private String query(String query) throws InternalServerError {
        return null;
    }

    /**
     * Function that adds data to the database
     * @param set the SQL statement to add to the database
     * @throws InternalServerError if there is an internal server error
     */
    private void add(String set) throws InternalServerError {

    }
    /**
     * Function that can create the table in the database
     * @throws InternalServerError if there is an internal server error
     */
    public void createTable() throws InternalServerError {

    }

    /**
     * Function that can delete the table from the database
     * @throws InternalServerError if there is an internal server error
     */
    public void deleteTable() throws InternalServerError {

    }
}

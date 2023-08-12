package src.DataAccess;
import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidPersonIDError;
import src.Model.*;
public class PersonDao extends Dao {

    /**
     * Constructor that loads the server object to then use later
     *
     * @param server an object that represents the server
     * @throws InternalServerError if there is an internal server error
     */
    public PersonDao(Object server) throws InternalServerError {
        super(server);
    }

    /**
     * Function that gets a person from the database
     * @param personID the person to add by personID
     * @return returns the person object
     * @throws InternalServerError if there is an internal server error
     * @throws InvalidPersonIDError if the personID is invalid
     */
    public Person getPerson(String personID) throws InternalServerError, InvalidPersonIDError {
        return null;
    }

    /**
     * Function that adds a person to the database
     * @param person the person to add
     * @throws InternalServerError if there is an internal server error
     */
    public void addPerson(Person person) throws InternalServerError {
    }

    /**
     * Function that deletes a person from the database
     * @param personID the person to delete by personID
     * @throws InternalServerError if there is an internal server error
     */
    public void deletePerson(String personID) throws InternalServerError {

    }
}

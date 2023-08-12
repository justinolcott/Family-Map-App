package src.Services;

import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidGenerationsParameter;
import src.Exceptions.InvalidUsername;
import src.Model.Person;

import java.util.ArrayList;

public class FillService extends Service {
    /**
     * Function that will create multiple generations
     * @param username the username of the user to create generations for
     * @param i the number of generations
     * @throws InvalidUsername if the username is invalid
     * @throws InvalidGenerationsParameter if the generations parameter is invalid
     */
    public void createGenerations(String username, int i) throws InvalidUsername, InvalidGenerationsParameter {

    }

    /**
     * Function that creates a generation for a given person to help the createGenerations function
     * @param person the person to create a generation for
     * @return the list of persons created
     * @throws InternalServerError if there is an internal server error
     */
    public ArrayList<Person> createGen(Person person) throws InternalServerError {
        return null;
    }
}

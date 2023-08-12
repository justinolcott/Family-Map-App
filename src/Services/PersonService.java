package src.Services;

import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidAuthToken;
import src.Exceptions.InvalidUserBelonging;
import src.Exceptions.InvalidPersonIDError;
import src.Model.AuthToken;
import src.Model.Person;

import java.util.ArrayList;

public class PersonService {
    /**
     * Function that will get all persons connected to a user
     * @param authToken the auth token of the user
     * @return all persons connected to the user
     * @throws InvalidAuthToken if the auth token is invalid
     * @throws InternalServerError if there is an invalid server error
     */
    public ArrayList<Person> getPersons(AuthToken authToken) throws InvalidAuthToken, InternalServerError {
        return null;
    }

    /**
     * Function that will get a person given their personID
     * @param personID the personID of the person to get
     * @param authtoken the auth token of the user
     * @return the person
     * @throws InvalidAuthToken if the auth token is invalid
     * @throws InvalidUserBelonging if the person does not belong to the user
     * @throws InvalidPersonIDError if the person ID is invalid
     */
    public Person getPerson(String personID, AuthToken authtoken) throws InvalidAuthToken, InvalidUserBelonging, InvalidPersonIDError {
        return null;
    }
}

package src.Request;

import src.Exceptions.InvalidRequestData;
import src.Model.Event;
import src.JSON.JSONArray;
import src.Model.Person;
import src.Model.User;

import java.util.ArrayList;

public class LoadRequest extends Request {
    public LoadRequest(JSONArray users, JSONArray persons, JSONArray events) throws InvalidRequestData {

    }

    /**
     * Function that checks whether a request is valid
     * @throws InvalidRequestData if the request is invalid
     */
    public void checkRequest() throws InvalidRequestData {

    }

    /**
     * Function that returns the users
     * @return the users
     */
    public ArrayList<User> modelUsers() {
        return null;
    }

    /**
     * Function that returns the persons
     * @return the persons
     */
    public ArrayList<Person> modelPersons() {
        return null;
    }

    /**
     * Function that returns the events
     * @return the events
     */
    public ArrayList<Event> modelEvents() {
        return null;
    }

}

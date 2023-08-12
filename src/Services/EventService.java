package src.Services;

import src.Exceptions.InternalServerError;
import src.Exceptions.InvalidAuthToken;
import src.Exceptions.InvalidUserBelonging;
import src.Model.AuthToken;
import src.Model.Event;
import src.Model.User;

import java.util.ArrayList;

public class EventService {

    /**
     * Function that gets all events connected to the user
     * @param authtoken the auth token of the user
     * @return a list of all events
     * @throws InvalidAuthToken if it is an invalid auth token
     * @throws InternalServerError if there is an internal service error
     */
    public ArrayList<Event> getEvents(AuthToken authtoken) throws InvalidAuthToken, InternalServerError {
        return null;
    }

    /**
     * Function that gets an event given an event id
     * @param eventID the event id of the event to get
     * @param authtoken the auth token of the user
     * @return the event requested
     * @throws InvalidAuthToken if the auth token is invalid
     * @throws InternalServerError if there is an internal server error
     * @throws InvalidUserBelonging if the event does not belong to this user
     */
    public Event getEvent(String eventID, AuthToken authtoken) throws InvalidAuthToken, InternalServerError, InvalidUserBelonging {
        return null;
    }
}

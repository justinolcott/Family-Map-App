package src.Handlers;

import src.Model.AuthToken;
import src.Result.EventIDResult;
import src.Result.EventResult;

/**
 * The class representing the handler for event calls
 */
public class EventHandler extends Handler {

    /**
     * Function that will call the event Service, handle the errors, and create a result
     * @param authtoken the authtoken of the user
     * @return the result of the service
     */
    public EventResult getEvents(AuthToken authtoken) {
        return null;
    }

    /**
     * Function that will call the event ID Service, handle the errors, and create a result
     * @param eventID the eventID of the event to get
     * @return the result of the service
     */
    public EventIDResult getEvent(String eventID) {
        return null;
    }

}

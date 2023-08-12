package src.Result;

public class EventIDResult extends Result {

    public EventIDResult(String associatedUsername, String eventID, String personID, float latitude, float longitude, String country, String city, String eventType, int year, boolean success) {

    }
    public EventIDResult(String message, boolean success) {
        super(message, success);
    }
}

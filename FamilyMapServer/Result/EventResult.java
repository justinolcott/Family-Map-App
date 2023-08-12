package src.Result;

import src.JSON.JSONArray;

public class EventResult extends Result {
    public EventResult(JSONArray events, boolean success) {

    }
    public EventResult(String message, boolean success) {
        super(message, success);
    }
}

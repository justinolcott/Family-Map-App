package src.Result;

import src.JSON.JSONArray;

public class PersonResult extends Result {

    public PersonResult(JSONArray data, boolean success) {

    }

    public PersonResult(String message, boolean success) {
        super(message, success);
    }
}

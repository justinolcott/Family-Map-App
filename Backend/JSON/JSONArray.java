package src.JSON;

import java.util.ArrayList;

/**
 * Class representing an array of JSON bodies
 */
public class JSONArray {
    ArrayList<JSONBody> jsons;

    /**
     * function to return the arraylist of jsons
     * @return the arraylist of jsons
     */
    public ArrayList<JSONBody> getJsons() {
        return jsons;
    }

    /**
     * a function to set the json array
     * @param jsons takes the json array
     */
    public void setJsons(ArrayList<JSONBody> jsons) {
        this.jsons = jsons;
    }

    /**
     * a constructor for json array
     * @param jsons takes the json array
     */
    public JSONArray(ArrayList<JSONBody> jsons) {
        this.jsons = jsons;
    }
}

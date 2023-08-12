package src.JSON;

/**
 * Class that represents a JSON body
 */
public class JSONBody {
    StringBuilder body;

    /**
     * Constructor for JSON body
     * @param body takes the JSON as a string
     */
    public JSONBody(String body) {
        this.body = new StringBuilder(body);
    }

    /**
     * Constructor for a json body given an object
     * @param o the object to create a json for
     */
    public JSONBody(Object o) {
        this.body = new StringBuilder();
    }

    /**
     * Constructor for JSON body given an empty
     */
    public JSONBody() {
        this.body = new StringBuilder();
    }

    /**
     * Function for adding a new line if being done manually
     * @param key the key string to add
     * @param value the value string to add
     */
    public void addLine(String key, String value) {
        return;
    }

    /**
     * Function to return the body, adds a } to the end if it is not already there
     * @return the JSON string
     */
    public String getBody() {
        if (this.body.charAt(this.body.length() - 1) == '}') {
            this.body.append("\n");
            this.body.append("}");
        }
        return body.toString();
    }
}

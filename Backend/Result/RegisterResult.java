package src.Result;

public class RegisterResult extends Result {
    /*public RegisterResult(String message, boolean success) {
        super(message, success);
    }*/

    public RegisterResult(String authtoken, String username, String personID, boolean success) {
        super();
    }

    public RegisterResult(String message, boolean b) {
    }
}

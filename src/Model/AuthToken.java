package src.Model;

/**
 * Class that models an auth token
 */
public class AuthToken {
    private String authtoken;
    private String username;
    public String getAuthtoken() {
        return authtoken;
    }
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }
}

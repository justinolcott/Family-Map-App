package src.Handlers;

import src.Result.PersonResult;
import src.Model.AuthToken;
import src.Result.PersonIDResult;


/**
 * Class that represents the handler of the person services
 */
public class PersonHandler extends Handler {

    /**
     * Function that will call the persons Service, handle the errors, and create a result
     * @param authtoken the auth token of the current user
     * @return the result of the persons call
     */
    public PersonResult getPersons(AuthToken authtoken) {
        return null;
    }

    /**
     * Function that will call the person service, handle the errors, and create a result
     * @param personID the personID of the person to get
     * @return the result of the person call
     */
    public PersonIDResult getPerson(String personID) {
        return null;
    }
}

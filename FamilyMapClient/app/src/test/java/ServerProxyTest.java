import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.familymapclient.backend.DataCache;
import com.example.familymapclient.backend.ServerProxy;

import java.util.Arrays;
import java.util.UUID;

import Model.AuthToken;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import Result.RegisterResult;

/*
Login method
Registering a new user
Retrieving people related to a logged in/registered user
Retrieving events related to a logged in/registered user
*/

public class ServerProxyTest {
    @Test
    public void loginMethodPass() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(loginRequest);
        assertTrue(loginResult.isSuccess());
    }
    @Test
    public void loginMethodFail() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "BAD_PASSWORD");
        System.out.println(loginRequest);
        LoginResult loginResult = serverProxy.login(loginRequest);
        assertFalse(loginResult.isSuccess());
    }

    @Test
    public void registerMethodPass() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(UUID.randomUUID().toString()); //this username can't exist in the database
        registerRequest.setPassword("password");
        registerRequest.setEmail("email");
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("lastName");
        registerRequest.setGender("m");
        RegisterResult registerResult = serverProxy.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(registerResult.getAuthtoken(), registerResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        EventResult eventResult = serverProxy.getEvents();
        assertEquals(31, personResult.getPersons().length);
        assertEquals(153, eventResult.getData().length);
    }

    @Test
    public void registerMethodFail() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("sheila");
        registerRequest.setPassword("password");
        registerRequest.setEmail("email");
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("lastName");
        registerRequest.setGender("m");
        RegisterResult registerResult = serverProxy.register(registerRequest);
        assertFalse(registerResult.isSuccess());
    }

    @Test
    public void getPeoplePass() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        assertEquals(8, personResult.getPersons().length);
    }

    @Test
    public void getPeoplePassWithRegister() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(UUID.randomUUID().toString()); //this username can't exist in the database
        registerRequest.setPassword("password");
        registerRequest.setEmail("email");
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("lastName");
        registerRequest.setGender("m");
        RegisterResult registerResult = serverProxy.register(registerRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(registerResult.getAuthtoken(), registerResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        assertEquals(31, personResult.getPersons().length);
    }

    @Test
    public void getPeopleFail() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "BAD PASSWORD");
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        assertFalse(personResult.isSuccess());
    }

    @Test
    public void getEventPass() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
        EventResult eventResult = serverProxy.getEvents();
        assertEquals(16, eventResult.getData().length);
    }

    @Test
    public void getEventPassWithRegister() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(UUID.randomUUID().toString()); //this username can't exist in the database
        registerRequest.setPassword("password");
        registerRequest.setEmail("email");
        registerRequest.setFirstName("firstName");
        registerRequest.setLastName("lastName");
        registerRequest.setGender("m");
        RegisterResult registerResult = serverProxy.register(registerRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(registerResult.getAuthtoken(), registerResult.getUsername()));
        EventResult eventResult = serverProxy.getEvents();
        assertEquals(153, eventResult.getData().length);
    }

    @Test
    public void getEventFail() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "BAD_PASSWORD");
        LoginResult loginResult = serverProxy.login(loginRequest);
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
        EventResult eventResult = serverProxy.getEvents();
        assertFalse(eventResult.isSuccess());
    }





}

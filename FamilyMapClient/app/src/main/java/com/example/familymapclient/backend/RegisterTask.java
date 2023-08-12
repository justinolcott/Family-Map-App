package com.example.familymapclient.backend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import Model.AuthToken;
import Request.RegisterRequest;
import Result.EventResult;
import Result.PersonResult;
import Result.RegisterResult;
import Result.PersonIDResult;
import Result.RegisterResult;

public class RegisterTask implements Runnable {

    private final Handler messageHandler;
    private final RegisterRequest registerRequest;
    private final String serverHost;
    private final String serverPort;

    private static final String FIRSTNAME_KEY = "FirstnameKey";
    private static final String LASTNAME_KEY = "LastnameKey";
    private static final String SUCCESS_KEY = "ErrorKey";


    public RegisterTask(Handler messageHandler, RegisterRequest registerRequest, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.registerRequest = registerRequest;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Log.d("register TASK", "Starting run()");
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost(this.serverHost);
        serverProxy.setServerPort(this.serverPort);
        RegisterResult registerResult = serverProxy.register(this.registerRequest);

        if (registerResult == null) {
            Log.d("REGISTER TASK", "registerResult == null");
            sendMessage();
            return;
        }

        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(new AuthToken(registerResult.getAuthtoken(), registerResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        EventResult eventResult = serverProxy.getEvents();

        dataCache.setPeople(personResult.getPersons());
        dataCache.setUserId(registerResult.getPersonID());
        dataCache.setEvents(eventResult.getData());
        dataCache.setPersonEvents();
        dataCache.setMaternalAncestors();
        dataCache.setPaternalAncestors();
        dataCache.setLoggedIn(true);


        Log.d("REGISTER TASK", "Starting get user");
        PersonIDResult personIDResult = serverProxy.getUser(registerResult.getPersonID(), registerResult.getAuthtoken());
        sendMessage(personIDResult.getFirstName(), personIDResult.getLastName());
        Log.d("register TASK RUN", "Completed Run");
    }

    private void sendMessage(String firstName, String lastName) {
        Log.d("register TASK", "Sending Message: " + firstName);
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString(FIRSTNAME_KEY, firstName);
        messageBundle.putString(LASTNAME_KEY, lastName);
        message.setData(messageBundle);

        this.messageHandler.sendMessage(message);
    }


    //Error send message
    private void sendMessage() {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(SUCCESS_KEY, false);
        message.setData(messageBundle);

        this.messageHandler.sendMessage(message);
    }
}

package com.example.familymapclient.backend;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import android.os.Handler;

import Model.AuthToken;
import Model.Person;
import Model.User;
import Request.LoginRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonIDResult;
import Result.PersonResult;

public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private final LoginRequest loginRequest;
    private final String serverHost;
    private final String serverPort;

    private static final String FIRSTNAME_KEY = "FirstnameKey";
    private static final String LASTNAME_KEY = "LastnameKey";
    private static final String SUCCESS_KEY = "ErrorKey";


    public LoginTask(Handler messageHandler, LoginRequest loginRequest, String serverHost, String serverPort) {
        this.messageHandler = messageHandler;
        this.loginRequest = loginRequest;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Log.d("LOGIN TASK", "Starting run()");
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost(this.serverHost);
        serverProxy.setServerPort(this.serverPort);
        LoginResult loginResult = serverProxy.login(this.loginRequest);

        if (loginResult == null || !loginResult.isSuccess()) {
            Log.d("LOGIN TASK", "LoginResult == null");
            sendMessage(false);
            return;
        }

        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setAuthToken(new AuthToken(loginResult.getAuthtoken(), loginResult.getUsername()));
        PersonResult personResult = serverProxy.getPeople();
        EventResult eventResult = serverProxy.getEvents();

        dataCache.setPeople(personResult.getPersons());
        dataCache.setUserId(loginResult.getPersonID());
        dataCache.setEvents(eventResult.getData());
        dataCache.setPersonEvents();
        dataCache.setMaternalAncestors();
        dataCache.setPaternalAncestors();
        dataCache.setLoggedIn(true);

        PersonIDResult personIDResult = serverProxy.getUser(loginResult.getPersonID(), loginResult.getAuthtoken());

        sendMessage(personIDResult.getFirstName(), personIDResult.getLastName());
        Log.d("LOGIN TASK RUN", "Completed Run");
    }

    private void sendMessage(String firstName, String lastName) {
        Log.d("LOGIN TASK", "Sending Message: " + firstName);
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString(FIRSTNAME_KEY, firstName);
        messageBundle.putString(LASTNAME_KEY, lastName);
        message.setData(messageBundle);

        this.messageHandler.sendMessage(message);
    }

    private void sendMessage(boolean success) {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean(SUCCESS_KEY, success);
        message.setData(messageBundle);

        this.messageHandler.sendMessage(message);
    }


}

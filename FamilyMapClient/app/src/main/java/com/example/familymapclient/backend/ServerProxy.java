package com.example.familymapclient.backend;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import Model.AuthToken;
import Model.Person;
import Request.*;
import Result.*;

public class ServerProxy { //ServerFacade
    private String serverHost;
    private String serverPort;

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    protected <T> String serialize(T o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    protected Object deserialize(Type t, InputStream s) {
        Reader json = new InputStreamReader(s);
        Gson gson = new Gson();
        Object o = gson.fromJson(json, t);
        return o;
    }


    public LoginResult login(LoginRequest request) {
        String respData = "";
        InputStream respBody = null;
        try {
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.setConnectTimeout(5000); // Set a timeout of 5 seconds
            http.connect();

            String reqData = serialize(request); //request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            }
            else {
                respBody = http.getErrorStream();
                return (LoginResult) deserialize(LoginResult.class, respBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return (LoginResult) deserialize(LoginResult.class, respBody);
    }

    public RegisterResult register(RegisterRequest request) {
        String respData = "";
        InputStream respBody = null;
        try {
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            http.setConnectTimeout(5000); // Set a timeout of 5 seconds
            http.connect();

            String reqData = serialize(request); //request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            }
            else {
                respBody = http.getErrorStream();
                return (RegisterResult) deserialize(RegisterResult.class, respBody);
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return (RegisterResult) deserialize(RegisterResult.class, respBody);
    }

    public PersonIDResult getUser(String personId, String authToken) {
        String respData = "";
        InputStream respBody = null;
        try {
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/person/" + personId);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                respBody = http.getErrorStream();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return (PersonIDResult) deserialize(PersonIDResult.class, respBody);
    }



    public EventResult getEvents() {
        String respData = "";
        InputStream respBody = null;
        DataCache dataCache = DataCache.getInstance();

        try {
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.getAuthToken().getAuthtoken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
                //respData = readString(respBody);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
                respBody = http.getErrorStream();
                return (EventResult) deserialize(EventResult.class, respBody);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return (EventResult) deserialize(EventResult.class, respBody);
    }

    public PersonResult getPeople() {
        String respData = "";
        InputStream respBody = null;
        DataCache dataCache = DataCache.getInstance();

        try {
            URL url = new URL("http://" + this.serverHost + ":" + this.serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", dataCache.getAuthToken().getAuthtoken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                respBody = http.getInputStream();
                //respData = readString(respBody);
            }
            else {
                respBody = http.getErrorStream();
                return (PersonResult) deserialize(PersonResult.class, respBody);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return (PersonResult) deserialize(PersonResult.class, respBody);
    }

    /*
		The readString method shows how to read a String from an InputStream.
	*/
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }



    /*
    login
    register
    getPeople
    getEvents

    clear
    fill
    getPerson
    getEvent
     */

}

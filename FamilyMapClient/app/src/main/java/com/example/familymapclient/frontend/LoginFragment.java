package com.example.familymapclient.frontend;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;
import com.example.familymapclient.backend.LoginTask;
import com.example.familymapclient.backend.RegisterTask;

import java.nio.channels.DatagramChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;


public class LoginFragment extends Fragment {

    private static final String FIRSTNAME_KEY = "FirstnameKey";
    private static final String LASTNAME_KEY = "LastnameKey";
    private static final String SUCCESS_KEY = "ErrorKey";

    //LISTENER
    private Listener listener;

    public interface Listener {
        void notifyLogin();
        void notifyRegister();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
        Log.d("LOGINFRAGMENT", "registerListener()");
    }


    public LoginFragment() {
        // Required empty public constructor
        Log.d("LOGINFRAGMENT", "LoginFragment Constructor");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText serverHostField = (EditText) view.findViewById(R.id.serverHostField);
        EditText serverPortField = (EditText) view.findViewById(R.id.serverPortField);
        EditText usernameField = (EditText) view.findViewById(R.id.usernameField);
        EditText passwordField = (EditText) view.findViewById(R.id.passwordField);
        EditText firstNameField = (EditText) view.findViewById(R.id.firstNameField);
        EditText lastNameField = (EditText) view.findViewById(R.id.lastNameField);
        EditText emailAddressField = (EditText) view.findViewById(R.id.emailAddressField);
        RadioGroup radioGroup = view.findViewById(R.id.radioSex);

        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        TextWatcher fieldWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // login fields
                Log.d("LOGIN FRAGMENT", "Text Changed");
                boolean loginFieldsFilled = !TextUtils.isEmpty(serverHostField.getText())
                        && !TextUtils.isEmpty(serverPortField.getText())
                        && !TextUtils.isEmpty(usernameField.getText())
                        && !TextUtils.isEmpty(passwordField.getText());

                //registerFields
                boolean registerFieldsFilled = !TextUtils.isEmpty(firstNameField.getText())
                        && !TextUtils.isEmpty(lastNameField.getText())
                        && !TextUtils.isEmpty(emailAddressField.getText())
                        && loginFieldsFilled;

                // Enable the login button if all required fields are filled
                loginButton.setEnabled(loginFieldsFilled);

                RadioGroup radioGroup = view.findViewById(R.id.radioSex);
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    registerButton.setEnabled(registerFieldsFilled);
                } else {
                    registerButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean loginFieldsFilled = !TextUtils.isEmpty(serverHostField.getText())
                        && !TextUtils.isEmpty(serverPortField.getText())
                        && !TextUtils.isEmpty(usernameField.getText())
                        && !TextUtils.isEmpty(passwordField.getText());

                //registerFields
                boolean registerFieldsFilled = !TextUtils.isEmpty(firstNameField.getText())
                        && !TextUtils.isEmpty(lastNameField.getText())
                        && !TextUtils.isEmpty(emailAddressField.getText())
                        && loginFieldsFilled;

                // Enable the login button if all required fields are filled
                loginButton.setEnabled(loginFieldsFilled);

                RadioGroup radioGroup = view.findViewById(R.id.radioSex);
                if (radioGroup.getCheckedRadioButtonId() != -1) {
                    registerButton.setEnabled(registerFieldsFilled);
                } else {
                    registerButton.setEnabled(false);
                }
            }
        });

        serverHostField.addTextChangedListener(fieldWatcher);
        serverPortField.addTextChangedListener(fieldWatcher);
        usernameField.addTextChangedListener(fieldWatcher);
        passwordField.addTextChangedListener(fieldWatcher);
        firstNameField.addTextChangedListener(fieldWatcher);
        lastNameField.addTextChangedListener(fieldWatcher);
        emailAddressField.addTextChangedListener(fieldWatcher);





        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOGINFRAGMENT", "onClick() for login");


                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();

                        if (!bundle.getBoolean(SUCCESS_KEY, true)) {
                            String msg = "Error Logging In";
                            //Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                            //toast.show();
                        }
                        else {
                            String firstName = bundle.getString(FIRSTNAME_KEY, "Firstname");
                            String lastName = bundle.getString(LASTNAME_KEY, "Lastname");
                            String msg = firstName + " " + lastName;
                            //Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                            //toast.show();
//                        DataCache dataCache = DataCache.getInstance();
//                        dataCache.setFirstName(firstName);
//                        dataCache.setLastName(lastName);
                            if (listener != null) {
                                Log.d("LOGINFRAGMENT", "onClick() not null");
                                listener.notifyLogin();
                            }
                        }
                    }
                };


                String serverHost = serverHostField.getText().toString();
                String serverPort = serverPortField.getText().toString();
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();


                LoginRequest loginRequest = new LoginRequest(username, password);
                LoginTask loginTask = new LoginTask(uiThreadMessageHandler, loginRequest, serverHost, serverPort);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(loginTask);



            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOGINFRAGMENT", "onClick() for register");

                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();

                        if (!bundle.getBoolean(SUCCESS_KEY, true)) {
                            String msg = "Error Registering";
                            //Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
                            //toast.show();
                            Log.d("LOGIN", msg);
                        }
                        else {
                            String firstName = bundle.getString(FIRSTNAME_KEY, "Firstname");
                            String lastName = bundle.getString(LASTNAME_KEY, "Lastname");
                            String msg = firstName + " " + lastName;
//                        Toast toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
//                        toast.show();
//                        DataCache dataCache = DataCache.getInstance();
//                        dataCache.setFirstName(firstName);
//                        dataCache.setLastName(lastName);
                            Log.d("LOGIN", msg);
                        }
                    }
                };


                String serverHost = serverHostField.getText().toString();
                String serverPort = serverPortField.getText().toString();
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                String firstName = firstNameField.getText().toString();
                String lastName = lastNameField.getText().toString();
                String email = emailAddressField.getText().toString();

                // Get the radio group and selected radio button
                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioSex);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String selectedValue = null;
                if (selectedId != -1) {
                    RadioButton selectedRadioButton = (RadioButton) view.findViewById(selectedId);
                    selectedValue = selectedRadioButton.getText().toString();
                    Log.d("Radio", selectedValue);
                }

                String sex = "";
                if (Objects.equals(selectedValue, "Male")) {
                    sex = "m";
                }
                else {
                    sex = "f";
                }

                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setUsername(username);
                registerRequest.setPassword(password);
                registerRequest.setEmail(email);
                registerRequest.setFirstName(firstName);
                registerRequest.setLastName(lastName);
                registerRequest.setGender(sex);
                RegisterTask registerTask = new RegisterTask(uiThreadMessageHandler, registerRequest, serverHost, serverPort);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(registerTask);


                if (listener != null) {
                    Log.d("LOGINFRAGMENT", "onClick() not null");
                    listener.notifyLogin();
                }
            }
        });

        return view;
    }
}
package com.example.familymapclient.frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN ACTIVITY", "onCreate()");
        DataCache dataCache = DataCache.getInstance();

        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        Log.d("MAIN ACTIVITY", "Fragment is null");
        if (dataCache.isLoggedIn()) {
            Log.d("MA", "Starting map");
            MapFragment fragment = (MapFragment) fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
            if (fragment == null) {
                fragment = new MapFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragmentFrameLayout, fragment)
                        .commit();

            }
        }
        else {
            Log.d("MA", "Starting login");
            LoginFragment fragment = (LoginFragment) fragmentManager.findFragmentById(R.id.fragmentFrameLayout);
            if (fragment == null) {
                fragment = new LoginFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragmentFrameLayout, fragment)
                        .commit();

            }
            fragment.registerListener(this);
        }

        //TODO Swap Menus

    }

    private Fragment createLoginFragment() {
        LoginFragment fragment = new LoginFragment();
        //TODO
        return fragment;
    }

    //Only UI threads can add to the UI
    @Override
    public void notifyLogin() {
        //Code for when Login Button is clicked

        Log.d("LOGINFRAGMENT", "notifyLogin()");



        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();
    }



    @Override
    public void notifyRegister() {
        Log.d("LOGIN FRAGMENT", "notifyRegister()");
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment fragment = new MapFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();

        EditText usernameField = findViewById(R.id.usernameField);
        String username = usernameField.getText().toString();
        Log.d("Register Button", username);
    }
}
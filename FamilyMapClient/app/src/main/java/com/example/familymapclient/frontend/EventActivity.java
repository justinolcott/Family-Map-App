package com.example.familymapclient.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;

import Model.Event;

public class EventActivity extends AppCompatActivity {
    public static String EVENT_KEY = "event";
    private Event event;
    private DataCache dataCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        this.dataCache = DataCache.getInstance();
        this.event = dataCache.getEventById(intent.getStringExtra(EVENT_KEY));

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        MapFragment fragment = new MapFragment();
        fragment.setEvent(this.event.getEventID());
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentFrameLayout, fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
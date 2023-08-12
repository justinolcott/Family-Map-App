package com.example.familymapclient.frontend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import Model.Event;
import Model.Person;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private GoogleMap.OnMarkerClickListener listener;
    private ArrayList<String> eventColorList;
    private ArrayList<Marker> markerList;
    private ArrayList<Polyline> polylinesList;
    private HashMap<String, Event> events;
    private String event;
    private final int LIFE_STORY_COLOR = Color.BLUE;
    private final int FAMILY_TREE_COLOR = Color.GREEN;
    private final int SPOUSE_COLOR = Color.RED;

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (this.event == null) inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        Intent intent = null;
        switch (menu.getItemId()) {
            case R.id.action_search:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(menu);
        }
        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MF","Started OnMapReady");
        this.map = googleMap;
        this.map.setOnMapLoadedCallback(this);
        this.eventColorList = new ArrayList<>();
        this.markerList = new ArrayList<>();
        this.polylinesList = new ArrayList<>();
        this.events = new HashMap<>();
        Log.d("MF","Finished OnMapReady");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMapLoaded() {
        Log.d("MF","Started OnMapLoaded");
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
        this.map.setOnMarkerClickListener(marker -> {
            // Get the marker's tag
            Event event = (Event) marker.getTag();
            setEvent(event.getEventID());
            selectEvent(this.event);
            // Return true to consume the event and prevent the default behavior
            return true;
        });

        Log.d("MF", "Finished OnMapLoaded");
        applySettings();
        addMarkers();
    }

    private void clear() {
        Log.d("MF","started clear");
        if (this.map == null) return;
        this.map.clear();
        this.eventColorList = new ArrayList<>();
        this.polylinesList = new ArrayList<>();

        this.markerList = new ArrayList<>();
        this.events = new HashMap<>();
        Log.d("MF","Finished Clear");
    }


    @Override
    public void onResume() {
        Log.d("MF","started resume");
        super.onResume();
        //this.clear();
        if (this.map == null) return;

        this.map.clear();
        this.eventColorList = new ArrayList<>();
        this.polylinesList = new ArrayList<>();
        this.markerList = new ArrayList<>();
        this.events = new HashMap<>();
        this.map.getUiSettings().setZoomControlsEnabled(true);
        this.applySettings();


        this.addMarkers();
        Log.d("MF","started OnMapReady");
    }

    private void applySettings() {
        DataCache dc = DataCache.getInstance();
        dc.applySettings();
        this.events = dc.getFilteredEvents();

        Log.d("Filtered Event Size: ", String.valueOf(this.events.size()));
        Log.d("Total Event Size: ", String.valueOf(dc.getEvents().size()));
    }


    public void addMarkers() {
        HashMap<String, Event> events = this.events;
        ArrayList<Marker> markers = new ArrayList<>();

        for (Event e : events.values()) {
            markers.add(addMarker(e));
        }

        this.markerList = markers;

        if (this.event != null && this.events.get(this.event) != null) {
            this.selectEvent(this.event);
            this.centerEvent(this.event);
        }
        else {
            ImageView imageViewEventIcon = getView().findViewById(R.id.map_icon);
            TextView textView1 = getView().findViewById(R.id.map_text_1);
            TextView textView2 = getView().findViewById(R.id.map_text_2);
            imageViewEventIcon.setImageResource(R.drawable.ic_family);
            imageViewEventIcon.setColorFilter(getContext().getResources().getColor(R.color.black));
            textView1.setText(R.string.click_on_a_marker);
            textView2.setText(R.string.to_see_event_details);
        }
    }

    private Marker addMarker(Event event) {
        float color = getColor(event.getEventType());
        Marker marker = this.map.addMarker(new MarkerOptions()
                .position(new LatLng(event.getLatitude(), event.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(color)));
        marker.setTag(event);
        marker.setVisible(true);
        return marker;
    }

    public void centerEvent(String eventId) {
        Event event = DataCache.getInstance().getEventById(eventId);
        LatLng center = new LatLng(event.getLatitude(), event.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(center);
        this.map.moveCamera(cameraUpdate);
    }

    public void selectEvent(String eventId) {
        Event event = DataCache.getInstance().getEventById(eventId);
        ImageView imageViewEventIcon = getView().findViewById(R.id.map_icon);
        TextView textView1 = getView().findViewById(R.id.map_text_1);
        TextView textView2 = getView().findViewById(R.id.map_text_2);
        DataCache dataCache = DataCache.getInstance();
        Person person = dataCache.getPersonById(event.getPersonID());
        String gender = person.getGender();

        if (Objects.equals(gender, "m")) {
            imageViewEventIcon.setImageResource(R.drawable.ic_baseline_man_24);
            imageViewEventIcon.setColorFilter(getContext().getResources().getColor(R.color.teal_700));
        }
        else if (Objects.equals(gender, "f")) {
            imageViewEventIcon.setImageResource(R.drawable.ic_baseline_woman_24);
            imageViewEventIcon.setColorFilter(getContext().getResources().getColor(R.color.purple_200));

        }

        textView1.setText(person.getFirstName() + " " + person.getLastName());
        String str = event.getEventType().toUpperCase(Locale.ROOT) +
                ": " + event.getCity() + ", " + event.getCountry() +
                " (" + event.getYear() + ")";
        textView2.setText(str);

        // Add OnClickListener to textView2 to start a new Person activity and pass the person object
        textView2.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra(PersonActivity.PERSON_KEY, person.getPersonID());
            startActivity(intent);
        });
        clearLines();
        addLines(event);
    }

    private void addLines(Event event) {
        DataCache dc = DataCache.getInstance();
        if (dc.isShowLifeStoryLines()) {
            showLifeStoryLines(event);
        }
        if (dc.isShowFamilyTreeLines()) {
            showFamilyTreeLines(event);
        }
        if (dc.isShowSpouseLines()) {
            showSpouseLines(event);
        }
    }

    private void showSpouseLines(Event event) {
        Person person = DataCache.getInstance().getPersonById(event.getPersonID());
        if (person.getSpouseID() == null) return;
        List<Event> spouseEvents = sortEvents(getPersonEvents(person.getSpouseID()));
        if (spouseEvents.size() == 0) return;

        LatLng start = new LatLng(event.getLatitude(), event.getLongitude());
        LatLng end = new LatLng(spouseEvents.get(0).getLatitude(), spouseEvents.get(0).getLongitude());
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(start, end)
                .color(SPOUSE_COLOR)
                .width(8);
        Polyline polyline = this.map.addPolyline(polylineOptions);
        this.polylinesList.add(polyline);
    }

    private void showLifeStoryLines(Event event) {
        List<Event> personEvents = getPersonEvents(event.getPersonID());
        personEvents = sortEvents(personEvents);

        LatLng start = null;
        for (Event e : personEvents) {
            LatLng next = new LatLng(e.getLatitude(), e.getLongitude());
            if (start != null) {
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(start, next)
                        .color(LIFE_STORY_COLOR);
                Polyline polyline = this.map.addPolyline(polylineOptions);
                this.polylinesList.add(polyline);
            }
            start = next;
        }
    }

    private void showFamilyTreeLines(Event event) {
        float width = 16;
        Person person = DataCache.getInstance().getPersonById(event.getPersonID());

        LatLng start = new LatLng(event.getLatitude(), event.getLongitude());
        List<Event> motherEvents = sortEvents(getPersonEvents(person.getMotherID()));
        if (motherEvents.size() >= 1) {
            LatLng end = new LatLng(motherEvents.get(0).getLatitude(), motherEvents.get(0).getLongitude());
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(start, end)
                    .color(FAMILY_TREE_COLOR)
                    .width(width);
            Polyline polyline = this.map.addPolyline(polylineOptions);
            this.polylinesList.add(polyline);
            drawGenerationLine(DataCache.getInstance().getPersonById(person.getMotherID()), (float) (width * 0.5));
        }

        List<Event> fatherEvents = sortEvents(getPersonEvents(person.getFatherID()));
        if (fatherEvents.size() >= 1) {
            LatLng end = new LatLng(fatherEvents.get(0).getLatitude(), fatherEvents.get(0).getLongitude());
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(start, end)
                    .color(FAMILY_TREE_COLOR)
                    .width(width);
            Polyline polyline = this.map.addPolyline(polylineOptions);
            this.polylinesList.add(polyline);
            drawGenerationLine(DataCache.getInstance().getPersonById(person.getFatherID()), (float) (width * 0.5));
        }
    }

    private void drawGenerationLine(Person p, float width) {
        Log.d("Person", p.getPersonID());
        List<Event> personEvents = sortEvents(getPersonEvents(p.getPersonID()));
        if (personEvents.size() >= 1) {
            LatLng start = new LatLng(personEvents.get(0).getLatitude(), personEvents.get(0).getLongitude());

            List<Event> motherEvents = sortEvents(getPersonEvents(p.getMotherID()));
            if (motherEvents.size() >= 1) {
                LatLng end = new LatLng(motherEvents.get(0).getLatitude(), motherEvents.get(0).getLongitude());
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(start, end)
                        .color(FAMILY_TREE_COLOR)
                        .width(width);
                Polyline polyline = this.map.addPolyline(polylineOptions);
                this.polylinesList.add(polyline);
                drawGenerationLine(DataCache.getInstance().getPersonById(p.getMotherID()), (float) (width * 0.5));
            }

        }

    }

    private List<Event> sortEvents(List<Event> eventList) {
        return DataCache.getInstance().sortEvents(eventList);
    }

    private List<Event> getPersonEvents(String personId) {
        List<Event> personEvents = new ArrayList<>();
        for (Event e : this.events.values()) {
            if (Objects.equals(e.getPersonID(), personId)) {
                personEvents.add(e);
            }
        }
        return personEvents;
    }

    private void clearLines() {
        for (Polyline p : this.polylinesList) {
            p.remove();
        }
        this.polylinesList = new ArrayList<>();
    }

    private float getColor(String type) {
        int index = 0;
        type = type.toLowerCase();
        if (!this.eventColorList.contains(type)) {
            this.eventColorList.add(type);
        }
        for (int i = 0; i < this.eventColorList.size(); i++) {
            if (Objects.equals(this.eventColorList.get(i), type)) {
                index = i;
            }
        }

        float color = 0;
        index = index % 10;
        switch (index) {
            case 0:
                color = BitmapDescriptorFactory.HUE_GREEN;
                break;
            case 1:
                color = BitmapDescriptorFactory.HUE_RED;
                break;
            case 2:
                color = BitmapDescriptorFactory.HUE_BLUE;
                break;
            case 3:
                color = BitmapDescriptorFactory.HUE_YELLOW;
                break;
            case 4:
                color = BitmapDescriptorFactory.HUE_ORANGE;
                break;
            case 5:
                color = BitmapDescriptorFactory.HUE_VIOLET;
                break;
            case 6:
                color = BitmapDescriptorFactory.HUE_ROSE;
                break;
            case 7:
                color = BitmapDescriptorFactory.HUE_CYAN;
                break;
            case 8:
                color = BitmapDescriptorFactory.HUE_AZURE;
                break;
            case 9:
                color = BitmapDescriptorFactory.HUE_MAGENTA;
                break;
            default:
                color = BitmapDescriptorFactory.HUE_MAGENTA;
                break;
        }
        return color;
    }


}
/*

Map Fragment
- support map fragment is the google map and is one widget
- you will want two textviews
- use a horizontal layout to get the emblem to the left

Person View
- Expandable List View
- relative layout to anchor things to the left

Google Map Fragment Notes:
-


 */


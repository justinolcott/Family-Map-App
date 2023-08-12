package com.example.familymapclient.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;

import java.util.ArrayList;
import java.util.Objects;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;
    private ArrayList<Event> filteredEvents;
    private ArrayList<Person> filteredPeople;
    private ArrayList<Event> matchedEvents;
    private ArrayList<Person> matchedPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.matchedEvents = new ArrayList<>();
        this.matchedPeople = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache dc = DataCache.getInstance();
        dc.applySettings();
        Log.d("Filtered Event Size: ", String.valueOf(dc.getFilteredEvents().size()));
        Log.d("Filtered People Size: ", String.valueOf(dc.getFilteredPeople().size()));
        this.filteredEvents = new ArrayList<>(dc.getFilteredEvents().values());
        this.filteredPeople = new ArrayList<>(dc.getFilteredPeople().values());


        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                SearchListAdapter adapter = new SearchListAdapter(matchedPeople, matchedEvents);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
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

    private void search(String s) {
        s = s.toLowerCase();
        this.matchedPeople = DataCache.getInstance().searchForPeople(s);
        this.matchedEvents = DataCache.getInstance().searchForEvents(s);
    }

    private class SearchListAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final ArrayList<Event> events;
        private final ArrayList<Person> people;

        SearchListAdapter(ArrayList<Person> people, ArrayList<Event> events) {
            this.events = events;
            this.people = people;
        }

        @Override
        public int getItemViewType(int position) {
            return position < this.people.size() ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            //Same View for now
            if (viewType == PERSON_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.list_item_body, parent, false);
            }
            else {
                view = getLayoutInflater().inflate(R.layout.list_item_body, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < people.size()) {
                holder.bind(people.get(position));
            }
            else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView lineOne;
        private final TextView lineTwo;
        private final ImageView icon;

        private final int viewType;
        private Person person;
        private Event event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            itemView.setOnClickListener(this);


            //They are the same for now...
            if (viewType == PERSON_VIEW_TYPE) {
                lineOne = itemView.findViewById(R.id.first_line_text_view);
                lineTwo = itemView.findViewById(R.id.second_line_text_view);
                icon = itemView.findViewById(R.id.icon_image_view);
            }
            else {
                lineOne = itemView.findViewById(R.id.first_line_text_view);
                lineTwo = itemView.findViewById(R.id.second_line_text_view);
                icon = itemView.findViewById(R.id.icon_image_view);
            }
        }

        private void bind(Person person) {
            this.person = person;
            this.lineOne.setText(person.getFirstName() + " " + person.getLastName());
            this.lineTwo.setText("");
            if (Objects.equals(person.getGender(), "m")) {
                this.icon.setImageResource(R.drawable.ic_baseline_man_24);
                this.icon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.teal_700));

            }
            else {
                this.icon.setImageResource(R.drawable.ic_baseline_woman_24);
                this.icon.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.purple_200));
            }
        }

        @SuppressLint("SetTextI18n")
        private void bind(Event event) {
            this.event = event;
            this.person = DataCache.getInstance().getPersonById(event.getPersonID());
            this.lineOne.setText(event.getEventType().toUpperCase() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")");
            this.lineTwo.setText(this.person.getFirstName() + " " + this.person.getLastName());
            icon.setImageResource(R.drawable.ic_marker);
        }

        @Override
        public void onClick(View view) {
            if (viewType == PERSON_VIEW_TYPE) {
                Intent intent = new Intent(view.getContext(), PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_KEY, this.person.getPersonID());
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                intent.putExtra(EventActivity.EVENT_KEY, this.event.getEventID());
                startActivity(intent);
            }
        }
    }


}
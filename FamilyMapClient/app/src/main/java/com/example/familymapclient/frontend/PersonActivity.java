package com.example.familymapclient.frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.backend.DataCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity {
    public static String PERSON_KEY = "person";
    private Person person;
    private DataCache dataCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent = getIntent();
        this.dataCache = DataCache.getInstance();
        this.person = dataCache.getPersonById(intent.getStringExtra(PERSON_KEY));

        TextView firstNameView = findViewById(R.id.firstNameField);
        TextView lastNameView = findViewById(R.id.lastNameField);
        TextView genderView = findViewById(R.id.genderField);
        firstNameView.setText(this.person.getFirstName());
        lastNameView.setText(this.person.getLastName());
        genderView.setText(this.person.getGender().equals("m") ? "Male" : "Female");

        ExpandableListView expandableListView = findViewById(R.id.expandable_list);

        this.dataCache.applySettings();
        List<Event> events = new ArrayList<>();
        HashMap<String, Event> filteredEvents = this.dataCache.getFilteredEvents();
        for (Event e : filteredEvents.values()) {
            if (Objects.equals(e.getPersonID(), this.person.getPersonID())) {
                events.add(e);
            }
        }

        this.dataCache.getPersonEvents(this.person.getPersonID());



        List<Person> children = this.dataCache.getChildren(this.person.getPersonID());
        Person father = this.dataCache.getPersonById(this.person.getFatherID());
        Person mother = this.dataCache.getPersonById(this.person.getMotherID());
        Person spouse = this.dataCache.getPersonById(this.person.getSpouseID());


        expandableListView.setAdapter(new ExpandableListAdapter(father, mother, spouse, children, events));
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

    private class ExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_POSITION = 0;
        private static final int FAMILY_POSITION = 1;

        private final List<Person> people;
        private final List<String> relationship;
        private final List<Event> events;

        ExpandableListAdapter(Person father, Person mother, Person spouse, List<Person> children, List<Event> events) {
            List<Event> events1;
            this.people = new ArrayList<>();
            this.relationship = new ArrayList<>();
            if (father != null) {
                this.people.add(father);
                this.relationship.add("Father");
            }

            if (mother != null) {
                this.people.add(mother);
                this.relationship.add("Mother");
            }

            if (spouse != null) {
                this.people.add(spouse);
                this.relationship.add("Spouse");
            }

            for (Person p : children) {
                this.people.add(p);
                this.relationship.add("Child");
            }

            Event[] eventsArray = events.toArray(new Event[0]);

            Arrays.sort(eventsArray, new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    int cmp = Integer.compare(e1.getYear(), e2.getYear());
                    if (cmp == 0) {
                        String eventType1 = e1.getEventType().toLowerCase();
                        String eventType2 = e2.getEventType().toLowerCase();
                        cmp = eventType1.compareTo(eventType2);
                    }
                    return cmp;
                }
            });

            this.events = Arrays.asList(eventsArray);
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return this.events.size();
                case FAMILY_POSITION:
                    return this.people.size();
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return getString(R.string.lifeEventsTitle);
                case FAMILY_POSITION:
                    return getString(R.string.familyTitle);
                default:
                    throw new IllegalArgumentException("Unrecognize Group Position");
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_POSITION:
                    return this.events.get(childPosition);
                case FAMILY_POSITION:
                    return this.people.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.list_item_group, viewGroup, false);
            }

            TextView titleView = view.findViewById(R.id.listTitle);

            switch (i) {
                case EVENT_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case FAMILY_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;
            switch (groupPosition) {
                case EVENT_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item_body, parent, false);
                    initEventView(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item_body, parent, false);
                    initPersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized Group Position");
            }
            return itemView;
        }

        @SuppressLint("SetTextI18n")
        private void initPersonView(View itemView, final int childPosition) {
            TextView firstLineView = itemView.findViewById(R.id.first_line_text_view);
            TextView secondLineView = itemView.findViewById(R.id.second_line_text_view);
            ImageView icon = itemView.findViewById(R.id.icon_image_view);

            firstLineView.setText(
                    this.people.get(childPosition).getFirstName() + " " +
                    this.people.get(childPosition).getLastName()
            );
            secondLineView.setText(this.relationship.get(childPosition));
            Log.d("GENDER", this.people.get(childPosition).getGender());
            if (Objects.equals(this.people.get(childPosition).getGender(), "m")) {
                icon.setImageResource(R.drawable.ic_baseline_man_24);
                icon.setColorFilter(R.color.teal_700);
            }
            else {
                icon.setImageResource(R.drawable.ic_baseline_woman_24);
                icon.setColorFilter(R.color.purple_200);
            }

            final Person person = this.people.get(childPosition);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_KEY, person.getPersonID());
                    startActivity(intent);
                }
            });

        }

        @SuppressLint("SetTextI18n")
        private void initEventView(View itemView, final int childPosition) {
            TextView firstLineView = itemView.findViewById(R.id.first_line_text_view);
            TextView secondLineView = itemView.findViewById(R.id.second_line_text_view);
            ImageView icon = itemView.findViewById(R.id.icon_image_view);
            Event event = this.events.get(childPosition);

            firstLineView.setText(
                    event.getEventType().toUpperCase() + ": " + event.getCity() + ", " +
                            event.getCountry() + " (" + event.getYear() + ")"
            );
            Person person = DataCache.getInstance().getPersonById(this.events.get(childPosition).getPersonID());
            secondLineView.setText(person.getFirstName() + " " + person.getLastName());
            icon.setImageResource(R.drawable.ic_marker);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_KEY, event.getEventID());
                    startActivity(intent);
                }
            });

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
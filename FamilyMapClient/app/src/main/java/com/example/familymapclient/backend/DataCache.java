package com.example.familymapclient.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;

public class DataCache {
    private static DataCache instance = new DataCache();
    public static DataCache getInstance() {
        return instance;
    }

    private DataCache() {
        this.people = new HashMap<>();
        this.events = new HashMap<>();
        this.personEvents = new HashMap<String, List<Event>>();
        this.maternalAncestors = new HashSet<>();
        this.paternalAncestors = new HashSet<>();
        this.maternalEvents = new HashMap<>();
        this.paternalEvents = new HashMap<>();
        this.userEvents = new HashMap<>();
        this.filteredEvents = new HashMap<>();
        this.filteredPeople = new HashMap<>();
        this.selectedEvent = null;

        this.showLifeStoryLines  = true;
        this.showFamilyTreeLines = true;
        this.showSpouseLines = true;
        this.showFatherSide  = true;
        this.showMotherSide  = true;
        this.showMaleEvents  = true;
        this.showFemaleEvents = true;
    }

    private boolean showLifeStoryLines;
    private boolean showFamilyTreeLines;
    private boolean showSpouseLines;
    private boolean showFatherSide;
    private boolean showMotherSide;
    private boolean showMaleEvents;
    private boolean showFemaleEvents;

    private boolean loggedIn;
    private AuthToken authToken;
    private String firstName;
    private String lastName;
    private String userId;
    private String selectedEvent;

    private HashMap<String, Person> people;
    private HashMap<String, Event> events;
    private HashMap<String, List<Event>> personEvents;
    private HashSet<Person> maternalAncestors;
    private HashSet<Person> paternalAncestors;
    private HashMap<String, Event> maternalEvents;
    private HashMap<String, Event> paternalEvents;
    private HashMap<String, Event> userEvents;

    private HashMap<String, Event> filteredEvents;
    private HashMap<String, Person> filteredPeople;

    //Settings settings
    //eventType color

    public void clear() {
        this.people = new HashMap<>();
        this.events = new HashMap<>();
        this.personEvents = new HashMap<String, List<Event>>();
        this.maternalAncestors = new HashSet<>(); //TODO
        this.paternalAncestors = new HashSet<>(); //TODO
        this.maternalEvents = new HashMap<>();
        this.paternalEvents = new HashMap<>();
        this.userEvents = new HashMap<>();
        this.filteredEvents = new HashMap<>();
        this.filteredPeople = new HashMap<>();
        this.selectedEvent = null;

        this.authToken = null;
        this.firstName = null;
        this.lastName = null;
        this.userId = null;

        this.showLifeStoryLines  = true;
        this.showFamilyTreeLines = true;
        this.showSpouseLines     = true;
        this.showFatherSide      = true;
        this.showMotherSide      = true;
        this.showMaleEvents      = true;
        this.showFemaleEvents    = true;
        this.loggedIn = false;
    }

    public HashMap<String, Event> getFilteredEvents() {
        return filteredEvents;
    }

    public HashMap<String, Person> getFilteredPeople() {
        return filteredPeople;
    }

    public List<Event> sortEvents(List<Event> eventList) {
        Event[] eventsArray = eventList.toArray(new Event[0]);

        Arrays.sort(eventsArray, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Integer.compare(e1.getYear(), e2.getYear());
            }
        });

        return Arrays.asList(eventsArray);
    }

    public ArrayList<Person> searchForPeople(String s) {
        s = s.toLowerCase();
        ArrayList<Person> matchedPeople = new ArrayList<>();
        for (Person p : this.filteredPeople.values()) {
            if (p.getFirstName().toLowerCase().contains(s)
                    || p.getLastName().toLowerCase().contains(s)) {
                matchedPeople.add(p);
            }
        }
        return matchedPeople;
    }

    public Event getSpouseEvent(String personId) {
        Person person = this.getPersonById(personId);
        if (person.getSpouseID() == null) return null;
        List<Event> spouseEvents = this.sortEvents(getPersonEvents(person.getSpouseID()));
        if (spouseEvents.size() == 0) return null;
        return spouseEvents.get(0);
    }

    public ArrayList<Event> searchForEvents(String s) {
        s = s.toLowerCase();
        ArrayList<Event> matchedEvents = new ArrayList<>();
        for (Event e : this.filteredEvents.values()) {
            if (e.getEventType().toLowerCase().contains(s)
                    || e.getCity().toLowerCase().contains(s)
                    || e.getCountry().toLowerCase().contains(s)
                    || Integer.toString(e.getYear()).toLowerCase().contains(s)) {
                matchedEvents.add(e);
            }
        }
        return matchedEvents;
    }

    public void applySettings() {
        DataCache dc = DataCache.getInstance();
        this.filteredEvents = new HashMap<>();
        this.filteredPeople = new HashMap<>();

        //User and their spouse will always be included
        Person user = this.people.get(this.userId);
        this.filteredPeople.put(this.userId, user);
        if (user.getSpouseID() != null) this.filteredPeople.put(user.getSpouseID(), this.people.get(user.getSpouseID()));
        this.filteredEvents.putAll(this.getUserEvents());

        //Maternal
        if (isShowMotherSide()) {
            this.filteredEvents.putAll(getMaternalEvents());
            this.filteredPeople.putAll(getParents(this.people.get(user.getMotherID())));
        }
        //Paternal
        if (isShowFatherSide()) {
            this.filteredEvents.putAll(dc.getPaternalEvents());
            this.filteredPeople.putAll(getParents(this.people.get(user.getFatherID())));
        }

        if (!isShowMaleEvents()) {
            Iterator<Event> iterator = this.filteredEvents.values().iterator();
            while (iterator.hasNext()) {
                Event e = iterator.next();
                if (Objects.equals(getPersonById(e.getPersonID()).getGender(), "m")) {
                    iterator.remove();
                }
            }
        }

        if (!isShowFemaleEvents()) {
            Iterator<Event> iterator = this.filteredEvents.values().iterator();
            while (iterator.hasNext()) {
                Event e = iterator.next();
                if (Objects.equals(getPersonById(e.getPersonID()).getGender(), "f")) {
                    iterator.remove();
                }
            }
        }
    }

    private HashMap<String, Person> getParents(Person p) {
        HashMap<String, Person> parents = new HashMap<>();
        parents.put(p.getPersonID(), p);
        if (p.getFatherID() != null) parents.putAll(getParents(this.people.get(p.getFatherID())));
        if (p.getMotherID() != null) parents.putAll(getParents(this.people.get(p.getMotherID())));
        return parents;
    }


    public HashMap<String, Event> getMaternalEvents() {
        return maternalEvents;
    }

    private void setUserEvents() {
        for (Event e : this.events.values()) {
            if (Objects.equals(e.getPersonID(), this.userId)) {
                this.userEvents.put(e.getEventID(), e);
            }
            else if (Objects.equals(e.getPersonID(), this.getPersonById(this.userId).getSpouseID())) {
                this.userEvents.put(e.getEventID(), e);
            }
        }
    }

    public HashMap<String, Event> getUserEvents() {
        return userEvents;
    }

    public void setAaternalEvents() {
        setUserEvents();
        //This is set when events are set
        HashMap<String, Event> maternalEvents = new HashMap<>();
        Person user = this.getPersonById(this.userId);
        Person mother = this.getPersonById(user.getMotherID());
        Person father = this.getPersonById(user.getFatherID());

        this.maternalEvents = getParentEvents(mother);
        this.paternalEvents = getParentEvents(father);

    }

    private HashMap<String, Event> getParentEvents(Person parent) {
        HashMap<String, Event> parentEvents = new HashMap<>();
        for (Event e : this.events.values()) {
            if (Objects.equals(e.getPersonID(), parent.getPersonID())) {
                parentEvents.put(e.getEventID(), e);
            }
        }

        if (parent.getMotherID() != null) {
            parentEvents.putAll(getParentEvents(this.getPersonById(parent.getMotherID())));
        }
        if (parent.getFatherID() != null) {
            parentEvents.putAll(getParentEvents(this.getPersonById(parent.getFatherID())));
        }
        return parentEvents;
    }

    public HashMap<String, Event> getPaternalEvents() {
        return paternalEvents;
    }

    public List<Event> getPersonEvents(String personId) {
        return this.personEvents.get(personId);
    }

    public List<Person> getChildren(String personId) {
        List<Person> children = new ArrayList<>();
        for (Person p : this.people.values()) {
            if (Objects.equals(p.getFatherID(), personId) || Objects.equals(p.getMotherID(), personId)) {
                children.add(p);
            }
        }
        return children;
    }

    public void setPeople(Person[] people) {
        for (int i = 0; i < people.length; i++) {
            this.people.put(people[i].getPersonID(), people[i]);
        }
    }

    public void setEvents(Event[] events) {
        int i = 0;
        for (Event event : events) {
            this.events.put(event.getEventID(), event);
            i++;
        }


        setAaternalEvents();
    }

    public void setPersonEvents() {
        for (String k : this.events.keySet()) {
            Event event = this.events.get(k);
            if (!this.personEvents.containsKey(event.getPersonID())) {
                this.personEvents.put(event.getPersonID(), new ArrayList<>());
            }
            this.personEvents.get(event.getPersonID()).add(event);
        }
    }

    public void setMaternalAncestors() {
        for (Person p : this.people.values() ) {
            if (Objects.equals(p.getGender(), "f") && !Objects.equals(p.getPersonID(), this.getUserId())) {
                this.maternalAncestors.add(p);
            }
        }
    }

    public void setPaternalAncestors() {
        for (Person p : this.people.values() ) {
            if (Objects.equals(p.getGender(), "m") && !Objects.equals(p.getPersonID(), this.getUserId())) {
                this.paternalAncestors.add(p);
            }
        }
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public Person getPersonById(String id) {
        return this.people.get(id);
    }
    public Event getEventById(String id) {
        return this.events.get(id);
    }


    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public boolean isShowFatherSide() {
        return showFatherSide;
    }

    public void setShowFatherSide(boolean showFatherSide) {
        this.showFatherSide = showFatherSide;
    }

    public boolean isShowMotherSide() {
        return showMotherSide;
    }

    public void setShowMotherSide(boolean showMotherSide) {
        this.showMotherSide = showMotherSide;
    }

    public boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public String getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(String selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}

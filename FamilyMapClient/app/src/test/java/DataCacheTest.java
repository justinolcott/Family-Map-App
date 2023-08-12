
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import Model.AuthToken;
import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.collection.ArraySet;

import com.example.familymapclient.backend.DataCache;
import com.example.familymapclient.backend.ServerProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/*
Calculates family relationships (i.e., spouses, parents, children)
Filters events according to the current filter settings
Chronologically sorts a person’s individual events (birth first, death last, etc.)
Correctly searches for people and events (for your Search Activity)
* */

public class DataCacheTest {

    @Before
    public void setUp() {
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setServerHost("localhost");
        serverProxy.setServerPort("8080");
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = serverProxy.login(loginRequest);

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
    }

    @Test
    public void testClearPass() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        assertNull(dataCache.getAuthToken());
        assertNull(dataCache.getFirstName());
        assertNull(dataCache.getLastName());
        assertNull(dataCache.getUserId());
        assertNull(dataCache.getSelectedEvent());
        assertFalse(dataCache.isLoggedIn());
        assertTrue(dataCache.getPeople().isEmpty());
        assertTrue(dataCache.getEvents().isEmpty());
        assertTrue(dataCache.getMaternalEvents().isEmpty());
        assertTrue(dataCache.getPaternalEvents().isEmpty());
        assertTrue(dataCache.getUserEvents().isEmpty());
        assertTrue(dataCache.getFilteredEvents().isEmpty());
        assertTrue(dataCache.getFilteredPeople().isEmpty());
        assertTrue(dataCache.isShowLifeStoryLines());
        assertTrue(dataCache.isShowFamilyTreeLines());
        assertTrue(dataCache.isShowSpouseLines());
        assertTrue(dataCache.isShowFatherSide());
        assertTrue(dataCache.isShowMotherSide());
        assertTrue(dataCache.isShowMaleEvents());
        assertTrue(dataCache.isShowFemaleEvents());
    }

    @Test
    public void testClearFail() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        dataCache.setLoggedIn(true);
        assertNotEquals(dataCache.isLoggedIn(), false);
    }

    @Test
    public void testFilterPass() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.applySettings();
        assertEquals(ALL_PEOPLE, dataCache.getFilteredPeople().keySet().toString());
        assertEquals(ALL_EVENTS, dataCache.getFilteredEvents().keySet().toString());
        dataCache.setShowFemaleEvents(false);
        dataCache.setShowFatherSide(false);
        dataCache.applySettings();
        assertEquals(FILTERED_PEOPLE, dataCache.getFilteredPeople().keySet().toString());
        assertEquals(FILTERED_EVENTS, dataCache.getFilteredEvents().keySet().toString());
    }

    @Test
    public void testFilterFail() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        assertThrows(Exception.class, dataCache::applySettings);
    }

    @Test
    public void testSortEventsPass() {
        DataCache dataCache = DataCache.getInstance();
        List<Event> events = dataCache.getPersonEvents(dataCache.getUserId());
        List<Event> sortedEvents = dataCache.sortEvents(events);
        assertTrue(sortedEvents.get(0).getEventID().toLowerCase().contains("birth"));
        assertTrue(sortedEvents.get(sortedEvents.size() - 1).getEventID().toLowerCase().contains("death"));
    }

    @Test
    public void testSortEventsAbnormal() {
        DataCache dataCache = DataCache.getInstance();
        List<Event> events = dataCache.getPersonEvents("Davis_Hyer");
        List<Event> sortedEvents = dataCache.sortEvents(events);
        assertTrue(sortedEvents.get(0).getEventID().toLowerCase().contains("birth"));
        //He only has one event
        assertTrue(sortedEvents.get(sortedEvents.size() - 1).getEventID().toLowerCase().contains("birth"));
    }

    @Test
    public void testSearchPeoplePass() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.applySettings();
        assertEquals(1, dataCache.searchForPeople("sheila").size());
    }

    @Test
    public void testSearchPeopleFail() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.applySettings();
        assertEquals(0, dataCache.searchForPeople("asdfasdf").size());
    }

    @Test
    public void testSearchEventPass() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.applySettings();
        //Only 1 Event has frog
        assertEquals(1, dataCache.searchForEvents("frog").size());
    }

    @Test
    public void testSearchEventFail() {
        DataCache dataCache = DataCache.getInstance();
        dataCache.applySettings();
        assertEquals(0, dataCache.searchForEvents("asdfasdf").size());
    }

    @Test
    public void testSpouseEventPass() {
        DataCache dataCache = DataCache.getInstance();
        assertEquals("Davis_Birth", dataCache.getSpouseEvent(dataCache.getUserId()).getEventID());
    }

    @Test
    public void testSpouseEventFail() {
        DataCache dataCache = DataCache.getInstance();
        assertThrows(NullPointerException.class, () -> {
            dataCache.getSpouseEvent("Patrick_Spencer");
        });
    }

    @Test
    public void testParentRelationshipPass() {
        DataCache dataCache = DataCache.getInstance();
        assertEquals("Betty_White", dataCache.getPersonById(dataCache.getUserId()).getMotherID());
        assertEquals("Blaine_McGary", dataCache.getPersonById(dataCache.getUserId()).getFatherID());
    }

    @Test
    public void testParentRelationshipAbnormal() {
        DataCache dataCache = DataCache.getInstance();
        assertNull(dataCache.getPersonById("Frank_Jones").getFatherID());
        assertNull(dataCache.getPersonById("Frank_Jones").getFatherID());
    }

    @Test
    public void testChildrenRelationshipPass() {
        DataCache dataCache = DataCache.getInstance();
        assertEquals("Sheila_Parker", dataCache.getChildren("Betty_White").get(0).getPersonID());
    }

    @Test
    public void testChildrenRelationshipAbnormal() {
        DataCache dataCache = DataCache.getInstance();
        assertEquals(0, dataCache.getChildren(dataCache.getUserId()).size());
    }






    @After
    public void tearDown() {
        DataCache.getInstance().clear();
    }


    private static final String ALL_PEOPLE = "[Frank_Jones, Betty_White, Blaine_McGary, Ken_Rodham, Sheila_Parker, Davis_Hyer, Mrs_Jones, Mrs_Rodham]";
    private static final String FILTERED_PEOPLE = "[Frank_Jones, Betty_White, Sheila_Parker, Davis_Hyer, Mrs_Jones]";
    private static final String ALL_EVENTS = "[Other_Asteroids, Jones_Marriage, Mrs_Rodham_Backflip, Mrs_Jones_Barbecue, Rodham_Marriage, Sheila_Marriage, Sheila_Death, Sheila_Asteroids, Davis_Birth, Mrs_Rodham_Java, Mrs_Jones_Surf, Sheila_Birth, Jones_Frog, Betty_Death, Blaine_Birth, BYU_graduation]";
    private static final String FILTERED_EVENTS = "[Davis_Birth, Jones_Marriage, Jones_Frog]";
}
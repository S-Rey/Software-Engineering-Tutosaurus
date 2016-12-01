package ch.epfl.sweng.tutosaurus.helper;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.espresso.core.deps.guava.primitives.Booleans;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by samuel on 29.11.16.
 */

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseHelperTest {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private User albert;
    private HashMap<String, Boolean> languages;
    private HashMap <String, Boolean>meetings;
    private HashMap <String, String>coursePresentation;

    @Before
    public void setup() {
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_COURSE);
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_USER);
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_LANGUAGE);
        dbHelper = new LocalDatabaseHelper(getTargetContext());
        database = dbHelper.getWritableDatabase();

        albert = new User("111111");
        // characteristics
        albert.setUsername("Albert");
        albert.setFullName("Albert Einstein");
        albert.setEmail("albert.einstein@epfl.ch");
        albert.setUid("uid");
        albert.setGlobalRating(1.0);
        albert.setPicture(0);
        // Course
        albert.addTeaching("Physics");
        albert.addTeaching("French");
        albert.setCourseRating("Physics", 1.0);
        albert.setCourseRating("French", 0.5);
        albert.addHoursTaught("Physics", 100);
        albert.addHoursTaught("French", 10);
        albert.addStudying("Java");
        // Course Presentation
        coursePresentation = new HashMap<>();
        coursePresentation.put("Physics", "Relativity");
        coursePresentation.put("French", "Chocolatine");
        albert.setCoursePresentation(coursePresentation);
        // Languages
        languages = new HashMap<String,Boolean>();
        languages.put("German", true);
        languages.put("French", true);
        languages.put("Chinese", false);
        albert.setSpeaking(languages);
        // Meetings
        meetings = new HashMap<>();
        meetings.put("meet1", true);
        meetings.put("meet2", true);
        albert.setMeetings(meetings);
    }

    @After
    public void tearDown() {
        LocalDatabaseHelper.clear(database);
    }

    @Test
    public void testGetDatabase() {
        dbHelper.getReadableDatabase();
        dbHelper.getWritableDatabase();
    }

    @Test(expected = NullPointerException.class)
    public void insertNullUserTest() {
        User user = null;
        LocalDatabaseHelper.insertUser(user,database);
    }

    @Test
    public void basicAttributeTest() {
        LocalDatabaseHelper.insertUser(albert,database);
        User dbAlbert = LocalDatabaseHelper.getUser(dbHelper.getReadableDatabase());

        assertTrue(albert.getSciper().equals(dbAlbert.getSciper()));
        assertTrue(albert.getUsername().equals(dbAlbert.getUsername()));
        assertTrue(albert.getFullName().equals(dbAlbert.getFullName()));
        assertTrue(albert.getEmail().equals(dbAlbert.getEmail()));
        assertTrue(albert.getUid().equals(dbAlbert.getUid()));
        assertEquals(albert.getGlobalRating(),dbAlbert.getGlobalRating());
        assertEquals(albert.getPicture(), dbAlbert.getPicture());
    }

    @Test
    public void userLanguagesTest() {
        LocalDatabaseHelper.insertUser(albert,database);
        User dbAlbert = LocalDatabaseHelper.getUser(dbHelper.getReadableDatabase());

        assertTrue(albert.getLanguages().equals(dbAlbert.getLanguages()));
    }



    @Test
    public void userMeetingTest() {
        LocalDatabaseHelper.insertUser(albert,database);
        User dbAlbert = LocalDatabaseHelper.getUser(dbHelper.getReadableDatabase());
        Map dbMeeting = dbAlbert.getMeetings();
        assertTrue(albert.getMeetings().equals(dbAlbert.getMeetings()));
    }
}

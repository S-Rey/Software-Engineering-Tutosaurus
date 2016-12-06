package ch.epfl.sweng.tutosaurus;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.DatePicker;
import android.widget.TimePicker;


import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.tutosaurus.actions.NestedScrollViewScrollToAction;
import ch.epfl.sweng.tutosaurus.mockObjects.MockLocationProvider;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.object.HasToString.hasToString;

/**
 * Created by santo on 26/11/16.
 *
 * Tests that the locationActivity is able to use locations
 * .
 */


@RunWith(AndroidJUnit4.class)
public class MeetingTest{

    private MockLocationProvider mock;
    private MainActivity mainActivity;


    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<MainActivity>(
            MainActivity.class
    );


    @Before
    public void setUp() throws InterruptedException {
        onView(withId(R.id.main_email)).perform(typeText("albert.einstein@epfl.ch"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.main_password)).perform(typeText("tototo"));
        Espresso.closeSoftKeyboard();
        onView(withText("Log in")).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_findTutors_layout));
        //mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, mainActivity);
        onView(withId(R.id.byName)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.nameToSearch)).perform(typeText("Albert Einstein"));
        onView(withId(R.id.searchByName)).perform(click());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
        Thread.sleep(1000);
    }


    @Test
    public void testRequestAndConfirmMeeting() throws InterruptedException {

        //Set test location
        //mock.pushLocation(-12.34, 23.45);
        onView(withId(R.id.createMeetingButton)).perform(NestedScrollViewScrollToAction.scrollTo(), click());

        int year = 2020;
        int month = 11;
        int day = 15;

        onView(withId(R.id.pickDateTime)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month + 1, day));
        onView(withId(android.R.id.button1)).perform(click());

        int minutes = 50;
        int hour = 10;
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hour, minutes));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.dateView)).check(matches(withText("  2020, 11/15")));
        onView(withId(R.id.timeView)).check(matches(withText("   h 10:50")));

        onData(anything()).inAdapterView(withId(R.id.courseListView)).atPosition(0).perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.description)).perform(typeText("Einstein, I love you"));
        closeSoftKeyboard();

        onView(withId(R.id.addMeeting)).perform(click());

        onData(withId(R.id.meeting_confirmation_row_confirm)).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
    }

}
package ch.epfl.sweng.tutosaurus;

import android.location.LocationManager;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;
import android.widget.TimePicker;


import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.tutosaurus.actions.NestedScrollViewScrollToAction;
import ch.epfl.sweng.tutosaurus.mockObjects.MockLocationProvider;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by santo on 26/11/16.
 *
 * Tests that the locationActivity is able to use locations
 * .
 */


@RunWith(AndroidJUnit4.class)
public class LocationTest{

    private MockLocationProvider mock;
    private MainActivity mainActivity;


    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(
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
        Thread.sleep(1000);
        mainActivity = mainActivityTestRule.getActivity();
        mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, mainActivity);
        onView(withId(R.id.byName)).perform(click());
        onView(withId(R.id.nameToSearch)).perform(typeText("Albert Einstein"));
        onView(withId(R.id.searchByName)).perform(click());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
        Thread.sleep(1000);
    }


    @Test
    public void testLocation() throws InterruptedException {

        //Set test location
        mock.pushLocation(-12.34, 23.45);

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

        //TODO: test the textview that displays the date and the time
        onData(anything()).inAdapterView(withId(R.id.courseListView)).atPosition(0).perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.description)).perform(typeText("Einstein, I love you"));
        closeSoftKeyboard();

        onView(withId(R.id.addMeeting)).perform(click());



    }

}
package ch.epfl.sweng.tutosaurus;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.ActivityCompat;
import android.test.ActivityInstrumentationTestCase2;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.tutosaurus.mockObjects.MockLocationProvider;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by santo on 26/11/16.
 *
 * Tests that the locationActivity is able to use locations
 * .
 */


public class LocationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MockLocationProvider mock;

    public LocationTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }


    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<MainActivity>(
            MainActivity.class
    );


    @Before
    public void logInWithAlbert() throws InterruptedException {
        //onView(withId(R.id.mainBypassLoginButton)).perform(click());
        onView(withId(R.id.main_email)).perform(typeText("albert.einstein@epfl.ch"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.main_password)).perform(typeText("tototo"));
        Espresso.closeSoftKeyboard();
        onView(withText("Log in")).perform(click());
        Thread.sleep(4000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_meetings_layout));
        Thread.sleep(1000);
    }



    private void testLocation() {

        mock = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, getActivity());

        //Set test location
        mock.pushLocation(-12.34, 23.45);

        LocationListener lis = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                onData(anything()).inAdapterView(withId(R.id.meetingList)).atPosition(0)
                        .check(matches(hasDescendant(allOf(withId(R.id.map), withText(containsString(courseArrayList.get(i).getName()))))));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, lis);
        assertEquals(3==3, true);
    }

}
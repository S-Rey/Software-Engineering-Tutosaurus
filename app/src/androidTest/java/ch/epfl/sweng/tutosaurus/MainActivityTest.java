package ch.epfl.sweng.tutosaurus;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by Stephane on 11/29/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private WifiManager wifi;
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                mActivityRule.getActivity());
        wifi = (WifiManager) mActivityRule.getActivity().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        wifi.setWifiEnabled(true);
    }

    @Test
    public void buttonsShouldNotBeEnabledOffline() {
        solo.assertCurrentActivity("correct activity", MainActivity.class);
        onView(withId(R.id.mainBypassLoginButton)).check(matches(not(isEnabled())));
        //onView(withId(R.id.rstPasswordButton)).check(matches(not(isClickable())));
        //onView(withId(R.id.registerButton)).check(matches(not(isClickable())));
        //onView(withId(R.id.connectionButton)).check(matches(not(isClickable())));
        solo.clickOnView(solo.getView(R.id.connectionButton));
    }

}

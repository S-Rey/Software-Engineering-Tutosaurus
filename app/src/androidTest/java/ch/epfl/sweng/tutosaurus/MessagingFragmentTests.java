package ch.epfl.sweng.tutosaurus;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.tutosaurus.HomeScreenActivity;
import ch.epfl.sweng.tutosaurus.R;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by ubervison on 16.11.16.
 */

public class MessagingFragmentTests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<MainActivity>(
            MainActivity.class
    );

    @Rule
    public IntentsTestRule<HomeScreenActivity> homeScreenActivityIntentsTestRule = new IntentsTestRule<HomeScreenActivity>(
            HomeScreenActivity.class
    );

    @Before
    public void logInWithAlbert(){
        onView(withId(R.id.mainBypassLoginButton)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChatWithAlbert() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_messaging_layout));
        onData(anything())
                .inAdapterView(withId(R.id.message_list))
                .atPosition(1)
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent("ch.epfl.sweng.tutosaurus.Chat"));
    }

}

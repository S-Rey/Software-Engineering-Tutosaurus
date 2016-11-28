package ch.epfl.sweng.tutosaurus;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.widget.AdapterView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.tutosaurus.R;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * Created by ubervison on 16.11.16.
 */

public class MessagingFragmentTests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(
            MainActivity.class
    );

    @Before
    public void logInWithAlbert(){
        onView(withId(R.id.mainBypassLoginButton)).perform(click());
        try {
            Thread.sleep(5000);
            Intents.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testChatWithAlbert() {
        Intents.init();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_messaging_layout));
        onData(anything())
                .inAdapterView(allOf(
                        isAssignableFrom(AdapterView.class),
                        isDisplayed()
                ))
                .atPosition(1)
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intending(hasComponent("ch.epfl.sweng.tutosaurus.ChatActivity"));
    }

}

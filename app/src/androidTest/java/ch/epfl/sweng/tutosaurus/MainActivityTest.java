package ch.epfl.sweng.tutosaurus;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Vincent on 14/10/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public IntentsTestRule<MainActivity> mainActivityRule = new IntentsTestRule<MainActivity>(
            MainActivity.class
    );

    @Test
    public void testLogIn() {
        onView(withId(R.id.usernameEntry)).perform(typeText("myUsername")).perform(closeSoftKeyboard());
        onView(withId(R.id.passwordEntry)).perform(typeText("myPassword")).perform(closeSoftKeyboard());
        onView(withId(R.id.connectionButton)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.drawer_layout)).perform(close());
        onView(withId(R.id.action_logOutButton)).perform(click());
    }

    @Test
    public void testSignUp() {
        onView(withId(R.id.registerButton)).perform(click());
        onView(withId(R.id.firstNameEntry)).perform(typeText("Vincent")).perform(closeSoftKeyboard());
        onView(withId(R.id.lastNameEntry)).perform(typeText("Rinaldi")).perform(closeSoftKeyboard());
        onView(withId(R.id.emailAddressEntry)).perform(typeText("vincent.rinaldi@epfl.ch")).perform(closeSoftKeyboard());
        onView(withId(R.id.sciperEntry)).perform(typeText("239759")).perform(closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());
        onView(withId(R.id.firstNameProvided)).check(matches(withText("First name : Vincent")));
        onView(withId(R.id.lastNameProvided)).check(matches(withText("Last name : Rinaldi")));
        onView(withId(R.id.emailAddressProvided)).check(matches(withText("Email address : vincent.rinaldi@epfl.ch")));
        onView(withId(R.id.sciperProvided)).check(matches(withText("Sciper : 239759")));
        onView(withId(R.id.backToLoginButton)).perform(click());
    }
}

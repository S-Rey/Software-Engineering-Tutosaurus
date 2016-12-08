package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HelpFragmentTest {

    @Rule
    public IntentsTestRule<HomeScreenActivity> activityRule = new IntentsTestRule<>(
            HomeScreenActivity.class,
            true,
            false
    );

    @Before
    public void logIn() {
        Task<AuthResult> login = FirebaseAuth.getInstance().signInWithEmailAndPassword("vincent.rinaldi@epfl.ch", "mrstvm95");
        try {
            Tasks.await(login);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        activityRule.launchActivity(new Intent().setAction("OPEN_TAB_PROFILE"));
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_help_layout));
    }

    @Test
    public void testCall() throws InterruptedException {
        //Can't test the ACTION_CALL intent sending on jenkins because it doesn't allow apps to use the cell phone functionality
        Thread.sleep(500);
        Matcher<Intent> expectedIntent = hasAction(Intent.ACTION_CALL);
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.phoneLogo)).perform(click());
        //intended(expectedIntent);
    }

    @Test
    public void testMessage() throws InterruptedException {
        Thread.sleep(500);
        Matcher<Intent> expectedIntent = hasAction(Intent.ACTION_SEND);
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.messageLogo)).perform(click());
        intended(expectedIntent);
    }

    @Test
    public void checkTextPhoneNumber() {
        onView(withId(R.id.phoneNumberHelp)).check(matches(withText("+41 21 000 0000")));
    }

    @Test
    public void checkTextEmail() {
        onView(withId(R.id.emailAddressHelp)).check(matches(withText("android.studio@epfl.ch")));
    }

    @Test
    public void checkTextAvailabilityPhone() {
        onView(withId(R.id.clockMessagePhone)).check(matches(withText("Monday to Friday : 06:00 to 18:00")));
    }

    @Test
    public void checkTextAvailabilityAddress() {
        onView(withId(R.id.clockMessageEmail)).check(matches(withText("Service available 24/7")));
    }

    @Test
    public void checkTextHelpMessagePhone() {
        onView(withId(R.id.helpMessagePhone)).check(matches(withText("Need some help urgently ? You can call our support line. " +
                "You will be put in interaction with one of our support agents.")));
    }

    @Test
    public void checkTextHelpMessageEmail() {
        onView(withId(R.id.helpMessageEmail)).check(matches(withText("You can also send us an email. " +
                "A member of the assistance service will answer to your request soon.")));
    }

    @After
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}

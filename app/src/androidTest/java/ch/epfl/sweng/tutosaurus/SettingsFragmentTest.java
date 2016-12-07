package ch.epfl.sweng.tutosaurus;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getTargetContext());

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
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
    }

    @Test
    public void testSettingTabNotificationsCheckbox() throws InterruptedException {
        if (sharedPreferences.getBoolean("checkbox_preference_notification", true)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_notification")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_notification", true), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_notification")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_notification", true), equalTo(true));
        }
    }

    @Test
    public void testSettingTabCalendarCheckbox() throws InterruptedException {
        if (sharedPreferences.getBoolean("checkbox_preference_calendar", true)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_calendar")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_calendar", true), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_calendar")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_calendar", true), equalTo(true));
        }
    }

    @Test
    public void testSettingTabRatingAppBrowser() throws InterruptedException {
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData("http://google.com/"));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onData(PreferenceMatchers.withKey("intent_preference_rating")).perform(click());
        intended(expectedIntent);
    }

    @After
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}

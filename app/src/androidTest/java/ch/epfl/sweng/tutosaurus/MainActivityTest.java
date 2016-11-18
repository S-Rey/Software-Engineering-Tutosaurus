package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityRule = new IntentsTestRule<MainActivity>(
            MainActivity.class
    );

    @Test
    public void testSettingTab() throws InterruptedException{
        onView(withId(R.id.main_email)).perform(typeText("vincent.rinaldi@epfl.ch"));
        closeSoftKeyboard();
        onView(withId(R.id.main_password)).perform(typeText("mrstvm95"));
        closeSoftKeyboard();
        onView(withId(R.id.connectionButton)).perform(click());
        Thread.sleep(5000);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getTargetContext());
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));

        if (sharedPreferences.getBoolean("checkbox_preference_notification", true)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_notification")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_notification", true), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_notification")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_notification", true), equalTo(true));
        }

        if (sharedPreferences.getBoolean("checkbox_preference_calendar", true)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_calendar")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_calendar", true), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_calendar")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_calendar", true), equalTo(true));
        }

    }
}

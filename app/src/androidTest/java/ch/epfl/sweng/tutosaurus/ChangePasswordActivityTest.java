package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ChangePasswordActivityTest {

    private Solo solo;

    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(
            MainActivity.class
    );

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                activityRule.getActivity());
    }

    @Test
    public void notMachingPassword() throws InterruptedException {
        solo.assertCurrentActivity("correct activity", MainActivity.class);
        solo.typeText(0, "albert.einstein@epfl.ch");
        solo.typeText(1, "tototo");
        solo.clickOnView(solo.getView(R.id.connectionButton));
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
        onData(PreferenceMatchers.withKey("intent_preference_password")).perform(click());
        Thread.sleep(1000);
        solo.typeText(0, "newPass");
        solo.typeText(1, "newPassword");
        solo.clickOnView(solo.getView(R.id.changeNewPass));
        boolean toastMessageDisplayedIsCorrect = solo.searchText("Passwords must match");
        assertTrue(toastMessageDisplayedIsCorrect);
        Espresso.pressBack();
        onView(withId(R.id.action_logOutButton)).perform(click());
    }

    @Test
    public void notBothBoxesFilled() throws InterruptedException {
        solo.assertCurrentActivity("correct activity", MainActivity.class);
        solo.typeText(0, "albert.einstein@epfl.ch");
        solo.typeText(1, "tototo");
        solo.clickOnView(solo.getView(R.id.connectionButton));
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
        onData(PreferenceMatchers.withKey("intent_preference_password")).perform(click());
        Thread.sleep(1000);
        solo.typeText(0, "newPass");
        solo.clickOnView(solo.getView(R.id.changeNewPass));
        boolean toastMessageDisplayedIsCorrect = solo.searchText("Please fill both boxes above");
        assertTrue(toastMessageDisplayedIsCorrect);
        Espresso.pressBack();
        onView(withId(R.id.action_logOutButton)).perform(click());
    }

    @Test
    public void changePassword() throws InterruptedException {
        solo.assertCurrentActivity("correct activity", MainActivity.class);
        solo.typeText(0, "albert.einstein@epfl.ch");
        solo.typeText(1, "tototo");
        solo.clickOnView(solo.getView(R.id.connectionButton));
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
        onData(PreferenceMatchers.withKey("intent_preference_password")).perform(click());
        Thread.sleep(1000);
        solo.typeText(0, "newPass");
        solo.typeText(1, "newPass");
        solo.clickOnView(solo.getView(R.id.changeNewPass));
        boolean toastMessageDisplayedIsCorrect = solo.searchText("Password changed successfully");
        assertTrue(toastMessageDisplayedIsCorrect);
        Espresso.pressBack();
        onView(withId(R.id.action_logOutButton)).perform(click());

        solo.typeText(0, "albert.einstein@epfl.ch");
        solo.typeText(1, "newPass");
        solo.clickOnView(solo.getView(R.id.connectionButton));
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
        onData(PreferenceMatchers.withKey("intent_preference_password")).perform(click());
        Thread.sleep(1000);
        solo.typeText(0, "tototo");
        solo.typeText(1, "tototo");
        solo.clickOnView(solo.getView(R.id.changeNewPass));
        boolean previousPasswordSetBack = solo.searchText("Password changed successfully");
        assertTrue(previousPasswordSetBack);
        Espresso.pressBack();
        onView(withId(R.id.action_logOutButton)).perform(click());
    }

    @Test
    public void testBackButton() throws InterruptedException {
        solo.assertCurrentActivity("correct activity", MainActivity.class);
        solo.typeText(0, "albert.einstein@epfl.ch");
        solo.typeText(1, "tototo");
        solo.clickOnView(solo.getView(R.id.connectionButton));
        Thread.sleep(2000);
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings_layout));
        onData(PreferenceMatchers.withKey("intent_preference_password")).perform(click());
        solo.clickOnActionBarHomeButton();
        Thread.sleep(1000);
        intended(hasAction("OPEN_TAB_SETTINGS"));
        onView(withId(R.id.action_logOutButton)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}

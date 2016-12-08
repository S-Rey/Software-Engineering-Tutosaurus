package ch.epfl.sweng.tutosaurus;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class BeATutorFragmentTest {

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
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_beATutor_layout));
    }

    @Test
    public void testBeATutorTabEnglishCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_english", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_english")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_english", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_english")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_english", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabFrenchCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_french", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_french")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_french", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_french")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_french", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabGermanCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_german", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_german")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_german", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_german")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_german", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabItalianCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_italian", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_italian")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_italian", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_italian")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_italian", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabChineseCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_chinese", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_chinese")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_chinese", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_chinese")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_chinese", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabRussianCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_russian", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_russian")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_russian", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_russian")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_russian", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabMathematicsCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_mathematics", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_mathematics")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_mathematics", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_mathematics")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_mathematics", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabMathematicsEditTextCustom() {
        if (sharedPreferences.getBoolean("checkbox_preference_mathematics", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).perform(click());
            onView(withId(16908291)).perform(clearText(), typeText("I love Mathematics"));
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_mathematics", "Enter your description."),
                    equalTo("I love Mathematics"));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabMathematicsEditTextDefault() {
        if (sharedPreferences.getBoolean("checkbox_preference_mathematics", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).perform(click());
            onView(withId(16908291)).perform(clearText());
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_mathematics", "Enter your description."),
                    equalTo("Enter your description."));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_mathematics")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabPhysicsCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_physics", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_physics")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_physics", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_physics")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_physics", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabPhysicsEditTextCustom() {
        if (sharedPreferences.getBoolean("checkbox_preference_physics", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).perform(click());
            onView(withId(16908291)).perform(clearText(), typeText("I love Physics"));
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_physics", "Enter your description."),
                    equalTo("I love Physics"));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabPhysicsEditTextDefault() {
        if (sharedPreferences.getBoolean("checkbox_preference_physics", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).perform(click());
            onView(withId(16908291)).perform(clearText());
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_physics", "Enter your description."),
                    equalTo("Enter your description."));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_physics")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabChemistryCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_chemistry", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_chemistry")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_chemistry", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_chemistry")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_chemistry", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabChemistryEditTextCustom() {
        if (sharedPreferences.getBoolean("checkbox_preference_chemistry", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).perform(click());
            onView(withId(16908291)).perform(clearText(), typeText("I love Chemistry"));
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_chemistry", "Enter your description."),
                    equalTo("I love Chemistry"));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabChemistryEditTextDefault() {
        if (sharedPreferences.getBoolean("checkbox_preference_chemistry", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).perform(click());
            onView(withId(16908291)).perform(clearText());
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_chemistry", "Enter your description."),
                    equalTo("Enter your description."));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_chemistry")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabComputerScienceCheckbox() {
        if (sharedPreferences.getBoolean("checkbox_preference_computer_science", false)) {
            onData(PreferenceMatchers.withKey("checkbox_preference_computer_science")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_computer_science", false), equalTo(false));
        } else {
            onData(PreferenceMatchers.withKey("checkbox_preference_computer_science")).perform(click());
            assertThat(sharedPreferences.getBoolean("checkbox_preference_computer_science", false), equalTo(true));
        }
    }

    @Test
    public void testBeATutorTabComputerScienceEditTextCustom() {
        if (sharedPreferences.getBoolean("checkbox_preference_computer_science", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).perform(click());
            onView(withId(16908291)).perform(clearText(), typeText("I love Computer Science"));
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_computer_science", "Enter your description."),
                    equalTo("I love Computer Science"));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void testBeATutorTabComputerScienceEditTextDefault() {
        if (sharedPreferences.getBoolean("checkbox_preference_computer_science", false)) {
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).check(matches(isEnabled()));
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).perform(click());
            onView(withId(16908291)).perform(clearText());
            closeSoftKeyboard();
            onView(withId(16908313)).perform(click());
            assertThat(sharedPreferences.getString("edit_text_preference_computer_science", "Enter your description."),
                    equalTo("Enter your description."));
        } else {
            onData(PreferenceMatchers.withKey("edit_text_preference_computer_science")).check(matches(not(isEnabled())));
        }
    }

    @After
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}

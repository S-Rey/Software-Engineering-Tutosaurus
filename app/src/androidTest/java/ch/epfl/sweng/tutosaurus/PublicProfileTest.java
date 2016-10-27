package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by albertochiappa on 14/10/16.
 */

public class PublicProfileTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public PublicProfileTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void testCanReachProfile() {
        getActivity();
        onView(withId(R.id.profileTestButton)).perform(click());
        onView(withId(R.id.profileName)).check(matches(withText("Alberto Chiappa")));
    }
    public void testCanOpenComments(){
        getActivity();
        onView(withId(R.id.profileTestButton)).perform(click());
        onView(withId(R.id.profileName)).check(matches(withText("Alberto Chiappa")));
        onView(withId(R.id.commentsButton)).perform(click());
        onView(withId(R.id.commentsButton)).check(matches(withText("Hide comments")));
    }
    public void testCanOpenSubjects(){
        getActivity();
        onView(withId(R.id.profileTestButton)).perform(click());
        onView(withId(R.id.profileName)).check(matches(withText("Alberto Chiappa")));
        onView(withId(R.id.mathsButton)).perform(click());
        onView(withId(R.id.subjectName)).check(matches(withText("Mathematics")));
        onView(withId(R.id.physicsButton)).perform(click());
        onView(withId(R.id.subjectName)).check(matches(withText("Physics")));
        onView(withId(R.id.computerButton)).perform(click());
        onView(withId(R.id.subjectName)).check(matches(withText("Computer Science")));
        onView(withId(R.id.chemistryButton)).perform(click());
        onView(withId(R.id.subjectName)).check(matches(withText("Chemistry")));
    }
}

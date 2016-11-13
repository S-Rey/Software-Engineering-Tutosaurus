package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.v4.view.NestedScrollingChild;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import ch.epfl.sweng.tutosaurus.actions.NestedScrollViewScrollToAction;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static ch.epfl.sweng.tutosaurus.actions.NestedScrollViewScrollToAction.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

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

    public void testCanSearchBySubject() throws InterruptedException{
        getActivity();
        onView(withId(R.id.mainBypassLoginButton)).perform(click());
        getActivity();
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_findTutors_layout));
        Thread.sleep(1000);
        onView(withId(R.id.bySubject)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.mathsButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
        onView(withId(R.id.profileName)).check(matches(isDisplayed()));
        Thread.sleep(1500);
        onView(withId(R.id.commentsButton)).perform(NestedScrollViewScrollToAction.scrollTo(), click());
        onView(withId(R.id.commentsView)).check(matches(isDisplayed()));
        onView(withId(R.id.commentsButton)).perform(NestedScrollViewScrollToAction.scrollTo(), click());
        onView(withId(R.id.commentsView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.mathsButton)).perform(NestedScrollViewScrollToAction.scrollTo(), click());
        onView(withId(R.id.subjectName)).check(matches(withText("Mathematics")));
        onView(withId(R.id.mathsButton)).perform(NestedScrollViewScrollToAction.scrollTo(), click());
        onView(withId(R.id.subjectName)).check(matches(not(isDisplayed())));
    }

}

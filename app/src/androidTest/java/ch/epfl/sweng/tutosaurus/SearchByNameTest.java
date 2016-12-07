package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

public class SearchByNameTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public SearchByNameTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void testCanSearchByName() throws InterruptedException {
        getActivity();
        onView(withId(R.id.main_email)).perform(typeText("albert.einstein@epfl.ch"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.main_password)).perform(typeText("tototo"));
        Espresso.closeSoftKeyboard();
        onView(withText("Log in")).perform(click());
        Thread.sleep(5000);
        getActivity();
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_findTutors_layout));
        Thread.sleep(1000);
        onView(withId(R.id.byName)).perform(click());
        onView(withId(R.id.nameToSearch)).perform(typeText("Albert Einstein"));
        onView(withId(R.id.searchByName)).perform(click());
        Thread.sleep(1000);
        onData(anything()).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.profileName)).check(matches(withText("Albert Einstein")));
        Espresso.pressBack();
        Espresso.pressBack();
        Thread.sleep(1000);
        onView(withId(R.id.byName)).perform(click());
        onView(withId(R.id.courseList)).check(matches(not(isDisplayed())));
        onView(withId(R.id.nameLayout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.byName)).perform(click());
        onView(withId(R.id.nameLayout)).check(matches(isDisplayed()));

    }
}
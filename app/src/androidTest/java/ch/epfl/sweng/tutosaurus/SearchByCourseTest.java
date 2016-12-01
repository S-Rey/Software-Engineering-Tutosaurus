package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.v4.view.NestedScrollingChild;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.regex.Matcher;

import ch.epfl.sweng.tutosaurus.actions.NestedScrollViewScrollToAction;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.anything;

/**
 * Created by albertochiappa on 14/10/16.
 *
 * Tests that all the available courses are displayed and work when clicked.
 */

public class SearchByCourseTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public SearchByCourseTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void testCanSearchBySubject() throws InterruptedException{
        getActivity();
        //onView(withId(R.id.mainBypassLoginButton)).perform(click());
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
        onView(withId(R.id.bySubject)).perform(click());
        onView(withId(R.id.courseList)).check(matches(isDisplayed()));
        Thread.sleep(1000);
        ArrayList<Course> courseArrayList = FullCourseList.getInstance().getListOfCourses();
        for(int i=0; i<courseArrayList.size(); i++){
            onData(anything()).inAdapterView(withId(R.id.courseList)).atPosition(i)
                    .check(matches(hasDescendant(allOf(withId(R.id.courseName), withText(containsString(courseArrayList.get(i).getName()))))));
            onData(anything()).inAdapterView(withId(R.id.courseList)).atPosition(i).perform(click());
            onData(anything()).inAdapterView(withId(R.id.tutorList)).atPosition(0).perform(click());
            onView(withText(courseArrayList.get(i).getName()))
                    .perform(NestedScrollViewScrollToAction.scrollTo(), click());
            Espresso.pressBack();
            Espresso.pressBack();
            Thread.sleep(1000);

        }
        onView(withId(R.id.bySubject)).perform(click());
        onView(withId(R.id.courseList)).check(matches(not(isDisplayed())));
    }

}

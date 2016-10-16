package ch.epfl.sweng.tutosaurus;

import android.content.Context;
import android.content.res.Resources;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceScreen;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.CheckBox;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ch.epfl.sweng.tutosaurus.matcher.CustomMatchers.noDrawable;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest extends ActivityInstrumentationTestCase2<HomeScreenActivity> {

    public SettingsFragmentTest() {
        super(HomeScreenActivity.class);
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @Before
    public void loadSettingPreferencesResources() {
        Resources resources = getInstrumentation().getTargetContext().getResources();
        resources.getXml(R.xml.settings_preferences);
    }

    @Test
    public void testCheckboxesClickWorks() throws InterruptedException {

        getActivity();
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_fourth_layout));
        Thread.sleep(1000);

        //onView(withId(R.id.checkbox_calendar)).perform(click());
        //onView(withId(R.id.checkbox_location)).perform(click());

        /**switch(view.getId()) {
            case R.id.checkbox_notifications:
                if (checked) {

                } else {

                }
                break;

            case R.id.checkbox_calendar:
                if (checked) {

                } else {

                }
                break;

            case R.id.checkbox_location:
                if (checked) {

                } else {

                }
        } **/

    }
}
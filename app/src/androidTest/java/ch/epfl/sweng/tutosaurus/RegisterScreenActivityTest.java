package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertTrue;

/**
 * Created by Stephane on 12/7/2016.
 */

public class RegisterScreenActivityTest {
    private Solo solo;
    private String validTequilaUrl = "https://tequila.epfl.ch/cgi-bin/tequila/login";

    @Rule
    public ActivityTestRule<RegisterScreenActivity> mActivityRule = new ActivityTestRule<>(
            RegisterScreenActivity.class);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                mActivityRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void ClickOnRegisterPopupTequilaLoginWebpage(){
        solo.assertCurrentActivity("correct activity", RegisterScreenActivity.class);
        solo.typeText(0, "albert");
        solo.typeText(1, "notThePassword");
        solo.clickOnView(solo.getView(R.id.registerLogin));
        //boolean tequilaWebPage = solo.searchText("tequila.epfl.ch");
        //assertTrue(tequilaWebPage);
        boolean teqWebPage = solo.searchText("Sign in");
        assertTrue(teqWebPage);
    }

    @Test
    public void testRegisterHomeUpGoesToMain() {
        solo.assertCurrentActivity("correct activity", RegisterScreenActivity.class);
        Intents.init();
        solo.clickOnActionBarHomeButton();
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

}

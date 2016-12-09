package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    public void ClickOnRegisterPopsupTequilaLoginWebpage(){
        solo.assertCurrentActivity("correct activity", RegisterScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.registerGaspar));
        solo.typeText(0, "albert");
        solo.clickOnView(solo.getView(R.id.registerPassword));
        solo.typeText(1, "notThePassword");
        solo.clickOnView(solo.getView(R.id.registerLogin));
        //boolean tequilaWebPage = solo.searchText("tequila.epfl.ch");
        //assertTrue(tequilaWebPage);
        boolean teqWebPage = solo.searchText("Sign in");
        assertTrue(teqWebPage);
    }

}

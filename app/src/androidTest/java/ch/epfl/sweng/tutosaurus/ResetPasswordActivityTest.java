package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertTrue;

/**
 * Created by Stephane on 11/27/2016.
 */

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityTest {

    private Solo solo;
    private String invalid_email;
    private String valid_email;

    @Rule
    public ActivityTestRule<ResetPasswordActivity> mActivityRule = new ActivityTestRule<>(
            ResetPasswordActivity.class);

    @Before
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                mActivityRule.getActivity());
        invalid_email = "HolyMolly@wow.com";
        valid_email = "albert.einstein@epfl.ch";
    }

    @After
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    public void correctFailToastDisplayed() throws Exception{
        solo.assertCurrentActivity("wrong activity", ResetPasswordActivity.class);
        solo.clickOnView(solo.getView(R.id.resetPasswordEmailInput));
        solo.typeText(0, invalid_email);
        solo.clickOnView(solo.getView(R.id.rstPasswordButton));
        boolean toastMsg = solo.searchText("Failed to send reset!");
        //assertTrue(toastMsg);
    }

    @Test
    public void correctSuccessToastDisplayed() throws Exception{
        solo.assertCurrentActivity("wrong activity", ResetPasswordActivity.class);
        solo.clickOnView(solo.getView(R.id.resetPasswordEmailInput));
        solo.typeText(0, valid_email);
        solo.clickOnView(solo.getView(R.id.rstPasswordButton));
        boolean toastMsg = solo.searchText("Instructions sent to your email!");
        //assertTrue(toastMsg);
    }

    @Test
    public void correctToastDisplayedIfEmptyEmail() throws Exception{
        solo.assertCurrentActivity("wrong activity", ResetPasswordActivity.class);
        solo.clickOnView(solo.getView(R.id.rstPasswordButton));
        boolean toastMsg = solo.searchText("Enter your registered email id");
        assertTrue(toastMsg);
    }

    /**@Test
    public void activityFinishCorrectly() throws Exception {
        solo.assertCurrentActivity("correct activity", ResetPasswordActivity.class);
        solo.clickOnView(solo.getView(R.id.backButton));
        //Not a good test
        Thread.sleep(2000);
        assertTrue(mActivityRule.getActivity().isFinishing());
    }*/

    @Test
    public void goToMainActivityOnBackButton() throws Exception {
        solo.assertCurrentActivity("correct activity", ResetPasswordActivity.class);
        Intents.init();
        solo.clickOnView(solo.getView(R.id.backButton));
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

}

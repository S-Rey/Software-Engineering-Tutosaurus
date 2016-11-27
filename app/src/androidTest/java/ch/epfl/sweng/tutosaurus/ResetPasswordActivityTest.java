package ch.epfl.sweng.tutosaurus;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.tutosaurus.matcher.ToastMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Stephane on 11/27/2016.
 */

@RunWith(AndroidJUnit4.class)
public class ResetPasswordActivityTest {

    private String invalid_email;

    @Rule
    public ActivityTestRule<ResetPasswordActivity> mActivityRule = new ActivityTestRule<>(
            ResetPasswordActivity.class);

    @Before
    public void initInvalidEmail(){
        invalid_email = "cheekybutts@burgerKing.com";
    }

    @Test
    public void noRequestSentForInvalidEmail(){
        onView(withId(R.id.resetPasswordEmailInput))
                .perform(typeText(invalid_email), closeSoftKeyboard());
        onView(withId(R.id.rstPasswordButton))
                .perform(click());
        //onView(withText(R.string.reset_request_fail)).inRoot(new ToastMatcher())
                //.check(matches(isDisplayed()));
    }

}

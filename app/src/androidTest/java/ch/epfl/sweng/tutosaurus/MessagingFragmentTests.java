package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.intent.IntentStubber;
import android.widget.AdapterView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sweng.tutosaurus.HomeScreenActivity;
import ch.epfl.sweng.tutosaurus.R;


import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MessagingFragmentTests {

    @Rule
    public IntentsTestRule<StartActivity> mainActivityIntentsTestRule = new IntentsTestRule<StartActivity>(
            StartActivity.class
    );

    @Mock FirebaseUser mockFirebaseUser;
    @Mock FirebaseAuth mockFirebaseAuth;
    @Mock Task<AuthResult> mockTask;


    @Before
    public void setup(){
        Intents.init();
        MockitoAnnotations.initMocks(this);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockFirebaseAuth.signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo")).thenReturn(mockTask);
        when(mockFirebaseUser.getEmail()).thenReturn("albert.einstein@epfl.ch");
        when(mockFirebaseUser.getUid()).thenReturn("TLL2vWfIytQUDidJbIy1hFv0mqC3");
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
    }

    @Test
    public void testChatWithAlbert() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_messaging_layout));
        onData(anything())
                .inAdapterView(allOf(
                        isAssignableFrom(AdapterView.class),
                        isDisplayed()
                ))
                .atPosition(1)
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(ChatActivity.class.getName()));
        Intents.release();
    }

}

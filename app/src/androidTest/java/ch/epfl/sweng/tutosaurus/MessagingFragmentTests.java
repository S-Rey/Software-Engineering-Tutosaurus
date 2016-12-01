package ch.epfl.sweng.tutosaurus;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.widget.AdapterView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.mockito.Mockito.when;

public class MessagingFragmentTests {

    @Rule
    public IntentsTestRule<StartActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(
            StartActivity .class
    );

    @Mock FirebaseUser mockFirebaseUser;
    @Mock FirebaseAuth mockFirebaseAuth;
    @Mock Task<AuthResult> mockTask;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockFirebaseAuth.signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo")).thenReturn(mockTask);
        when(mockFirebaseUser.getEmail()).thenReturn("albert.einstein@epfl.ch");
        when(mockFirebaseUser.getUid()).thenReturn("TLL2vWfIytQUDidJbIy1hFv0mqC3");
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        onView(withId(R.id.mainBypassLoginButton)).perform(click());
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
        //intended(hasComponent(hasClassName(ChatActivity.class.getName())));
    }

    @After
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }


}

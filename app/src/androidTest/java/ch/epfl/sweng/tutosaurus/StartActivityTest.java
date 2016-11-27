package ch.epfl.sweng.tutosaurus;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.espresso.intent.Intents.intended;import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ubervison on 28.11.16.
 */

@RunWith(AndroidJUnit4.class)
public class StartActivityTest {

    @Rule
    public IntentsTestRule<StartActivity> startActivityIntentsTestRule = new IntentsTestRule<>(
            StartActivity.class
    );

    @Mock FirebaseUser mockFirebaseUser;
    @Mock FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
    @Mock Task<AuthResult> mockTask;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockFirebaseAuth.signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo")).thenReturn(mockTask);
        when(mockFirebaseUser.getEmail()).thenReturn("albert.einstein@epfl.ch");
        when(mockFirebaseUser.getUid()).thenReturn("TLL2vWfIytQUDidJbIy1hFv0mqC3");
    }

    @Test
    public void mainActivityWhenNotLoggedIn() {
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(null);
        intended(hasComponent(hasClassName(MainActivity.class.getName())));
    }

    @Test
    public void HomeScreenActivityWhenLoggedIn() {
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        intended(hasComponent(hasClassName(HomeScreenActivity.class.getName())));
    }

}

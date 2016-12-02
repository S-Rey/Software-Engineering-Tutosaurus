package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
    public ActivityTestRule<StartActivity> rule = new ActivityTestRule<>(
            StartActivity.class,
            true,
            false
    );

    @Test
    public void mainActivityWhenNotLoggedIn() {
        Intents.init();
        rule.launchActivity(new Intent());
        intended(hasComponent(hasClassName(MainActivity.class.getName())));
        Intents.release();
    }

    @Test
    public void homeScreenActivityWhenLoggedIn() {
        Intents.init();
        Task<AuthResult> loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo");
        try {
            Tasks.await(loginTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        rule.launchActivity(new Intent());
        intended(hasComponent(hasClassName(HomeScreenActivity.class.getName())));
        Intents.release();
    }

    @After
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

}

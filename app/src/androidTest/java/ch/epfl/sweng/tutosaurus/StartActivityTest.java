package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.tutosaurus.activity.MainActivity;
import ch.epfl.sweng.tutosaurus.activity.StartActivity;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;

@RunWith(AndroidJUnit4.class)
public class StartActivityTest {

    private boolean logged_in = false;

    @Rule
    public ActivityTestRule<StartActivity> rule = new ActivityTestRule<>(
            StartActivity.class,
            true,
            false
    );

    @Before
    public void signOutBefore() {
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void mainActivityWhenNotLoggedIn() throws InterruptedException {
        Intents.init();
        Thread.sleep(200);
        rule.launchActivity(new Intent());
        Thread.sleep(200);
        intended(hasComponent(hasClassName(MainActivity.class.getName())));
        Intents.release();
    }

    /*@Test
    public void homeScreenActivityWhenLoggedIn() {
        Intents.init();
        Task<AuthResult> loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo");
        loginTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                logged_in = true;
            }
        });
        try {
            Tasks.await(loginTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (logged_in) {
            rule.launchActivity(new Intent());
            intended(hasComponent(hasClassName(HomeScreenActivity.class.getName())));
        }
        logged_in = false;
        Intents.release();
    }*/

    @After
    public void signOutAfter() {
        FirebaseAuth.getInstance().signOut();
    }

}

package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public IntentsTestRule<HomeScreenActivity> activityRule = new IntentsTestRule<>(
            HomeScreenActivity.class,
            true,
            false
    );

    @Before
    public void logIn() {
        Task<AuthResult> login = FirebaseAuth.getInstance().signInWithEmailAndPassword("vincent.rinaldi@epfl.ch", "mrstvm95");
        try {
            Tasks.await(login);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        activityRule.launchActivity(new Intent().setAction("OPEN_TAB_PROFILE"));
        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_profile_layout));
    }

    @Test
    public void testCamera() throws InterruptedException {
        Thread.sleep(500);
        Matcher<Intent> expectedIntent = allOf(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.picture_view)).perform(click());
        onView(withText("Take picture with camera")).perform(click());
        intended(expectedIntent);
    }

    @Test
    public void testGallery() throws InterruptedException {
        Thread.sleep(500);
        Matcher<Intent> expectedIntent = allOf(hasAction(Intent.ACTION_PICK));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));
        onView(withId(R.id.picture_view)).perform(click());
        onView(withText("Load picture from gallery")).perform(click());
        intended(expectedIntent);
    }

    @Test
    public void testImageCameraPicker() throws InterruptedException {
        Thread.sleep(500);
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.drawable.dino_logo);

        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(toPackage("com.android.camera")).respondWith(result);

        onView(withId(R.id.picture_view)).perform(click());
        onView(withText("Take picture with camera")).perform(click());

        intended(toPackage("com.android.camera"));
    }

    @Test
    public void testImageGalleryPicker() throws InterruptedException {
        Thread.sleep(500);
        Intent resultData = new Intent();
        Uri uri1 = Uri.parse("android.resource://ch.epfl.sweng.tutosaurus/" + R.drawable.dino_logo);
        resultData.setDataAndType(uri1, "image/*");

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(toPackage("com.android.gallery")).respondWith(result);

        onView(withId(R.id.picture_view)).perform(click());
        onView(withText("Load picture from gallery")).perform(click());

        // We can also validate that an intent resolving to the "camera" activity has been sent out by our app
        intended(toPackage("com.android.gallery"));

        // ... additional test steps and validation ...
    }


    @After
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}

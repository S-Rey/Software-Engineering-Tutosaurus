package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static ch.epfl.sweng.tutosaurus.matcher.CustomMatchers.noDrawable;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public IntentsTestRule<HomeScreenActivity> hsActivityRule = new IntentsTestRule<>(
            HomeScreenActivity.class
    );

    @Before()
    public void stubLoadProfilePictureIntent() {
        Intent intent = new Intent();
        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceTypeName(R.mipmap.ic_launcher) + '/' +
                resources.getResourceEntryName(R.mipmap.ic_launcher));
        intent.setType("image/*");
        intent.setData(imageUri);

        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcels = new ArrayList<>();
        Parcelable parcelable1 = (Parcelable) imageUri;
        parcels.add(parcelable1);
        bundle.putParcelableArrayList(Intent.EXTRA_STREAM, parcels);
        intent.putExtras(bundle);

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(anyOf(
                hasAction(Intent.ACTION_PICK),
                hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
        ).respondWith(result);
    }

    @Test
    public void testProfilePictureContainsBitmapAfterLoadingPicture() throws InterruptedException {

        onView(withId(R.id.drawer_layout)).perform(open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_first_layout));
        Thread.sleep(1000);
        onView(withId(R.id.buttonLoadPicture)).perform(click());
        onView(withId(R.id.pictureView)).check(matches(not(noDrawable())));
        Thread.sleep(1000);
        onView(withId(R.id.buttonTakePicture)).perform(click());
        onView(withId(R.id.pictureView)).check(matches(not(noDrawable())));
        Thread.sleep(1000);
    }
}
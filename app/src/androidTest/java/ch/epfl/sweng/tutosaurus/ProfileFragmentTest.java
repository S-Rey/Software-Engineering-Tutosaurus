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
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.matcher.CustomMatchers;

import static android.support.test.espresso.Espresso.onView;


@RunWith(AndroidJUnit4.class)
public class ProfileFragmentTest {

    @Rule
    public IntentsTestRule<HomeScreenActivity> hsActivityRule = new IntentsTestRule<>(
            HomeScreenActivity.class
    );

    @Before
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
        Intents.intending(CoreMatchers.anyOf(
                IntentMatchers.hasAction(Intent.ACTION_PICK),
                IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
        ).respondWith(result);
    }

    @Test
    public void testProfilePictureContainsBitmapAfterLoadingPicture() throws InterruptedException {

        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(ViewMatchers.withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_profile_layout));
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.buttonLoadPicture)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.picture_view)).check(ViewAssertions.matches(CoreMatchers.not(CustomMatchers.noDrawable())));
        Thread.sleep(1000);
        onView(ViewMatchers.withId(R.id.buttonTakePicture)).perform(ViewActions.click());
        onView(ViewMatchers.withId(R.id.picture_view)).check(ViewAssertions.matches(CoreMatchers.not(CustomMatchers.noDrawable())));
        Thread.sleep(1000);
    }
}
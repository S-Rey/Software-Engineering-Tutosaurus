package ch.epfl.sweng.tutosaurus.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.text.BidiFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.epfl.sweng.tutosaurus.MainActivity;
import ch.epfl.sweng.tutosaurus.R;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by samuel on 06.12.16.
 */


@RunWith(AndroidJUnit4.class)
public class PictureHelperTest {
    private Bitmap icon;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setup() {
        icon = BitmapFactory.decodeResource(
                mActivityRule.getActivity().getApplicationContext().getResources(),
                R.drawable.einstein);
        PictureHelper.storePicLocal(mActivityRule.getActivity(), "test", icon);
    }
    @After
    public void tearDown () {
        String dirPath = mActivityRule.getActivity().getFilesDir().getAbsolutePath()
                + File.separator + "pictures";
        File file = new File(dirPath + "/test.jpg");
        file.delete();
    }


    // The loaded picture == the stored one
    @Test
    public void loadLocalExistingPicTest() {
        Bitmap loadPic = PictureHelper.loadPictureLocal(mActivityRule.getActivity(), "test");
        assertNotNull(loadPic);
        assert(loadPic.sameAs(icon));
    }

    // Return Null if image does not exist
    @Test
    public void loadLocalFalsePicTest () {
        assertNull(PictureHelper.loadPictureLocal(mActivityRule.getActivity(), "wrongName"));
    }

    //@Test


}

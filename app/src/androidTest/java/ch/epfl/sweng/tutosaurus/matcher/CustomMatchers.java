package ch.epfl.sweng.tutosaurus.matcher;

import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by ubervison on 10/14/16.
 */

class CustomMatchers {

    public static Matcher<View> withDrawable(final int resourceId){
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }
}

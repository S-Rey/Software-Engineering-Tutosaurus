package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * Created by Vincent on 05/10/2016.
 */

public class FourthFragment extends PreferenceFragment {

    View myView;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_preferences);
    }



}

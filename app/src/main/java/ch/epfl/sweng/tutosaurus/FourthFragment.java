package ch.epfl.sweng.tutosaurus;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;

public class FourthFragment extends PreferenceFragment {

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");
        addPreferencesFromResource(R.xml.settings_preferences);
    }

}

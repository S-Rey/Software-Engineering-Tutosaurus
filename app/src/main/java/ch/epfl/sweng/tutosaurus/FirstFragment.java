package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstFragment extends PreferenceFragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Profile");
        return myView;
    }



}

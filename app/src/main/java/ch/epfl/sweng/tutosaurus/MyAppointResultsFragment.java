package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Vincent on 05/10/2016.
 */

public class MyAppointResultsFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.my_appoint_results_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("My Appoint Results");
        return myView;
    }
}
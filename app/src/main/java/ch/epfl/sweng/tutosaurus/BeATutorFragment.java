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

public class BeATutorFragment extends Fragment {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.be_a_tutor_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Be A Tutor");
        return myView;
    }
}
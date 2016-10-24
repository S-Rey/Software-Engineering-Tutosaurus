package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

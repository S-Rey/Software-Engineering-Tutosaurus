package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Meeting;

/**
 * Created by ubervison on 10/28/16.
 */

public class CustomAdapter extends FirebaseListAdapter<Meeting>{

    public CustomAdapter(Activity activity, java.lang.Class<Meeting> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, Meeting meeting, int position) {
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView date = (TextView) view.findViewById(R.id.date);
        location.setText(meeting.getLocation());
        date.setText(meeting.getDate().toString());
    }
}

package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;


public class MeetingConfirmationAdapter extends FirebaseListAdapter<MeetingRequest> {

    public static final String TAG = "MeetingConfAdapter";

    public MeetingConfirmationAdapter(Activity activity, Class<MeetingRequest> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View mainView, MeetingRequest request, int position) {
        TextView textView = (TextView) mainView.findViewById(R.id.meeting_confirmation_row_name);
        textView.setText(request.getMeeting().getDescription());
        Log.d(TAG, "description: " + request.getMeeting().getDescription());
    }
}

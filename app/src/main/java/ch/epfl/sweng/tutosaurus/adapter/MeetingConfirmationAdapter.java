package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;


public class MeetingConfirmationAdapter extends FirebaseListAdapter<MeetingRequest> {

    public static final String TAG = "MeetingConfAdapter";
    private String currentUserUid;
    private DatabaseHelper dbh;

    public MeetingConfirmationAdapter(Activity activity, Class<MeetingRequest> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserUid = currentUser.getUid();
        }
        dbh = DatabaseHelper.getInstance();
    }

    @Override
    protected void populateView(View mainView, final MeetingRequest request, int position) {
        TextView description = (TextView) mainView.findViewById(R.id.meeting_confirmation_row_description);
        final TextView name = (TextView) mainView.findViewById(R.id.meeting_confirmation_row_name);
        TextView location = (TextView) mainView.findViewById(R.id.meeting_confirmation_row_location);
        TextView date = (TextView) mainView.findViewById(R.id.meeting_confirmation_row_date);
        DatabaseReference fullNameRef = DatabaseHelper.getInstance().getUserRef().child(currentUserUid).child("fullName");
        fullNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, HH:mm");
        if (request.getMeeting().getNameLocation() != null) {
            location.setText(request.getMeeting().getNameLocation());
        }
        date.setText(dateFormat.format(request.getMeeting().getDate()));
        name.setText(request.getFrom());
        if (request.getMeeting().getDescription() != null) {
            description.setText(request.getMeeting().getDescription());
        }
        Button confirmMeetingButton = (Button) mainView.findViewById(R.id.meeting_confirmation_row_confirm);
        confirmMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.confirmMeeting(currentUserUid, request);
            }
        });
    }
}

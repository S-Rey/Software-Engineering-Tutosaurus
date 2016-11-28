package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import ch.epfl.sweng.tutosaurus.LocationActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by ubervison on 10/28/16.
 */

public class MeetingAdapter extends FirebaseListAdapter<Meeting>{

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    public MeetingAdapter(Activity activity, java.lang.Class<Meeting> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(final View mainView, final Meeting meeting, int position) {

        if (meeting.getCourse() != null) {
            TextView subject = (TextView) mainView.findViewById(R.id.subjectMeeting);
            subject.setText(meeting.getCourse().getName());
        }

        final TextView otherParticipantView = (TextView) mainView.findViewById(R.id.otherParticipantMeeting);

        Query ref = dbh.getUserRef();
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> participants = meeting.getParticipants();
                String displayParticipant = "";
                for (String participant: participants) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        if (!userSnapshot.getKey().equals(currentUser) && userSnapshot.getKey().equals(participant)) {
                            User user = userSnapshot.getValue(User.class);
                            if (displayParticipant == null) {
                                displayParticipant = user.getFullName();
                            } else {
                                displayParticipant = displayParticipant + "\n" + user.getFullName();
                            }
                            otherParticipantView.setText(displayParticipant);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView date = (TextView) mainView.findViewById(R.id.dateMeeting);
        if (meeting.getDate() != null) {
            date.setText(meeting.getDate().toString());
        }

        TextView descriptionMeeting = (TextView) mainView.findViewById(R.id.descriptionMeeting);
        if (meeting.getDescription() != null) {
            descriptionMeeting.setText(meeting.getDescription());
        }

        final double latitudeMeeting = meeting.getLatitudeLocation();
        final double longitudeMeeting = meeting.getLongitudeLocation();
        TextView locationMeeting = (TextView) mainView.findViewById(R.id.locationMeeting);
        if (meeting.getNameLocation() != null) {
            locationMeeting.setText(meeting.getNameLocation());
        }

        Button showLocationMeeting = (Button) mainView.findViewById(R.id.showLocationMeeting);
        showLocationMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainView.getContext(), LocationActivity.class);
                intent.putExtra("latitudeMeeting", latitudeMeeting);
                intent.putExtra("longitudeMeeting", longitudeMeeting);
                view.getContext().startActivity(intent);
            }
        });

        final Button detailsMeeting = (Button) mainView.findViewById(R.id.showDetailsMeeting);
        detailsMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout detailsLayout = (LinearLayout) mainView.findViewById(R.id.detailsMeeting);
                if (detailsLayout.getVisibility() == View.VISIBLE) {
                    detailsLayout.setVisibility(View.GONE);
                } else {
                    detailsLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}

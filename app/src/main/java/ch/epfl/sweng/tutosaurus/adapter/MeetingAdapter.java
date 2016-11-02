package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import java.util.List;

import ch.epfl.sweng.tutosaurus.LocationActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Meeting;

/**
 * Created by ubervison on 10/28/16.
 */

public class MeetingAdapter extends FirebaseListAdapter<Meeting>{

    private String currentUser = "456892";

    public MeetingAdapter(Activity activity, java.lang.Class<Meeting> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(final View mainView, Meeting meeting, int position) {

        TextView subject = (TextView) mainView.findViewById(R.id.subjectMeeting);
        subject.setText(meeting.getCourse().getName());

        TextView otherParticipantView = (TextView) mainView.findViewById(R.id.otherParticipantMeeting);
        List<String> participants = meeting.getParticipants();
        String content = new String();
        for (String participant : participants) {
            if (!participant.equals(currentUser)) {
                if (content.length() == 0) {
                    content = participant;
                }
                else {
                    content = content + "\n" + participant;
                }
            }
        }
        otherParticipantView.setText(content);

        TextView date = (TextView) mainView.findViewById(R.id.dateMeeting);
        date.setText(meeting.getDate().toString());

        TextView descriptionMeeting = (TextView) mainView.findViewById(R.id.descriptionMeeting);
        descriptionMeeting.setText(meeting.getDescription());

        final double latitudeMeeting = meeting.getLatitudeLocation();
        final double longitudeMeeting = meeting.getLongitudeLocation();
        TextView locationMeeting = (TextView) mainView.findViewById(R.id.locationMeeting);
        locationMeeting.setText(meeting.getNameLocation());

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
                detailsLayout.setVisibility(View.VISIBLE);
                //detailsMeeting.setOnClickListener();
            }
        });

    }

}

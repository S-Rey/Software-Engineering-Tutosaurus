package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.tutosaurus.LocationActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class MeetingAdapter extends FirebaseListAdapter<Meeting>{

    private String currentUserUid;
    private DatabaseHelper dbh = DatabaseHelper.getInstance();
    private float meetingRating;
    private User user;

    public MeetingAdapter(Activity activity, java.lang.Class<Meeting> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            currentUserUid = currentUser.getUid();
        }
    }

    @Override
    protected void populateView(final View mainView, final Meeting meeting, int position) {

        if (meeting.getCourse() != null) {
            FullCourseList allCourses = FullCourseList.getInstance();
            Course courseMeeting = allCourses.getCourse(meeting.getCourse().getId());

            TextView subject = (TextView) mainView.findViewById(R.id.courseName);
            subject.setText(meeting.getCourse().getName());
            ImageView coursePicture = (ImageView) mainView.findViewById(R.id.coursePicture);
            coursePicture.setImageResource(courseMeeting.getPictureId());
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
                        if (!userSnapshot.getKey().equals(currentUserUid) && userSnapshot.getKey().equals(participant)) {
                            user = userSnapshot.getValue(User.class);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, HH:mm", Locale.FRENCH);
            String dateNewFormat = dateFormat.format(meeting.getDate());
            date.setText(dateNewFormat);
        }

        TextView descriptionMeeting = (TextView) mainView.findViewById(R.id.descriptionMeeting);
        if (meeting.getDescription() != null) {
            descriptionMeeting.setText(meeting.getDescription());
        }

        final double latitudeMeeting = meeting.getLatitudeLocation();
        final double longitudeMeeting = meeting.getLongitudeLocation();
        TextView locationMeeting = (TextView) mainView.findViewById(R.id.locationMeeting);

        Button showLocationMeeting = (Button) mainView.findViewById(R.id.showLocationMeeting);
        if (meeting.getNameLocation() != null) {
            locationMeeting.setText(meeting.getNameLocation());
            showLocationMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mainView.getContext(), LocationActivity.class);
                    intent.putExtra("latitudeMeeting", latitudeMeeting);
                    intent.putExtra("longitudeMeeting", longitudeMeeting);
                    view.getContext().startActivity(intent);
                }
            });
        } else {
            showLocationMeeting.setVisibility(View.GONE);
        }

        final Button detailsMeeting = (Button) mainView.findViewById(R.id.showDetailsMeeting);
        if(meeting.getDate().getTime() > new Date().getTime() + 59958140730000L) {
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
        else if(meeting.isRated()) {
            detailsMeeting.setVisibility(View.GONE);
            RatingBar ratingBar = (RatingBar) mainView.findViewById(R.id.ratingBar);
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setRating(meetingRating);
        }
        else {
            detailsMeeting.setText("Rate");
            detailsMeeting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder ratingDialog = new AlertDialog.Builder(mainView.getContext());
                    final RatingBar rating = new RatingBar(mainView.getContext());
                    rating.setNumStars(5);
                    rating.setStepSize(1.0f);
                    rating.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                            ActionBar.LayoutParams.WRAP_CONTENT));
                    LinearLayout parent = new LinearLayout(mainView.getContext());
                    parent.setGravity(Gravity.CENTER);
                    parent.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT));
                    parent.addView(rating);

                    ratingDialog.setTitle("Rate this meeting");
                    ratingDialog.setView(parent);

                    // Button OK
                    ratingDialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    meetingRating = rating.getRating();
                                    meeting.setRated(true);
                                    if (user != null) {
                                        dbh.setMeetingRated(currentUserUid, user.getUid(), meeting.getId());
                                        int numRatings = user.getNumRatings();
                                        float globalRating = user.getGlobalRating();
                                        globalRating = (globalRating * numRatings + meetingRating) / (numRatings + 1);
                                        dbh.setRating(user.getUid(), globalRating);
                                        dbh.setNumRatings(user.getUid(), numRatings + 1);
                                    }

                                    //dbh.setRating(currentUserUid, meetingRating);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    ratingDialog.create();
                    ratingDialog.show();
                }
            });
        }


        final Button syncCalendar = (Button) mainView.findViewById(R.id.syncCalendar);
        syncCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.setTime(meeting.getDate());
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, "Switzerland/Lausanne");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.TITLE, meeting.getCourse().getName());
                if (user != null) {
                    intent.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
                }

                if (meeting.getDescription() != null) {
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, meeting.getDescription());
                }

                if (meeting.getNameLocation() != null) {
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, meeting.getNameLocation());
                }

                mainView.getContext().startActivity(intent);
            }
        });
    }

}
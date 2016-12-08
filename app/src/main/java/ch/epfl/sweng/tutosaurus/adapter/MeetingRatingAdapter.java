package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class MeetingRatingAdapter extends MeetingAdapter {

    private float ratingToSubmit;

    public MeetingRatingAdapter(Activity activity, Class<Meeting> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }


    @Override
    protected void populateView(final View mainView, final Meeting meeting, int position) {

        if (meeting.getCourse() != null) {
            //TextView subject = (TextView) mainView.findViewById(R.id.subjectMeeting);
            //subject.setText(meeting.getCourse().getName());
            final LinearLayout subjectMeeting = (LinearLayout) mainView.findViewById(R.id.subjectMeeting);
            FullCourseList allCourses = FullCourseList.getInstance();
            Course courseMeeting = allCourses.getCourse(meeting.getCourse().getId());
            //setRatingDialogListener(subjectMeeting, mainView);
            Button showDetails = (Button) mainView.findViewById(R.id.showDetailsMeeting);
            showDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog rankDialog = new Dialog(mainView.getContext(), R.style.RatingDialog);
                    rankDialog.setContentView(R.layout.rank_dialog);
                    rankDialog.setCancelable(true);
                    RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                    ratingBar.setStepSize(1.0F);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            ratingToSubmit = rating;
                            TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                            text.setText("ciao");

                            Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                            updateButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rankDialog.dismiss();
                                }
                            });
                            rankDialog.show();
                        }
                    });
                }
            });

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
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, HH:mm", Locale.FRENCH);
            String dateNewFormat = dateFormat.format(meeting.getDate());
            date.setText(dateNewFormat);
        }


    }


    private void setRatingDialogListener(LinearLayout clickable, final View mainView) {
        clickable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog rankDialog = new Dialog(mainView.getContext(), R.style.RatingDialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setStepSize(1.0F);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        ratingToSubmit = rating;
                    }
                });

                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText("ciao");

                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankDialog.dismiss();
                    }
                });
                rankDialog.show();
            }
        });
    }
}

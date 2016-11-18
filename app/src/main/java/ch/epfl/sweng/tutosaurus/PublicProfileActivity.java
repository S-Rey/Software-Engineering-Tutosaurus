package ch.epfl.sweng.tutosaurus;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.User;


public class PublicProfileActivity extends AppCompatActivity {

    DatabaseHelper dbh = DatabaseHelper.getInstance();
    public String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        final String userId = intent.getStringExtra("USER_ID");

        final Button createMeeting = (Button) findViewById(R.id.createMeetingButton);
        createMeeting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent createMeetingIntent = new Intent(getBaseContext(), CreateMeetingActivity.class);
                createMeetingIntent.putExtra("TEACHER", userId);
                startActivity(createMeetingIntent);
            }
        });


        DatabaseReference ref = dbh.getReference();
        ref.child("user/" + userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User matchingTutor = dataSnapshot.getValue(User.class);

                // Set profile name
                TextView profileName= (TextView) findViewById(R.id.profileName);
                profileName.setText(matchingTutor.getFullName());
                /*
                // Set profile picture
                ImageView profilePicture=(ImageView) findViewById(R.id.profilePicture);
                profilePicture.setImageResource(user.getPicture());
                */

                // Set email
                TextView email = (TextView) findViewById(R.id.emailView);
                email.setText(matchingTutor.getEmail());

                // Set the ratings
                RatingBar professorRate=(RatingBar) findViewById(R.id.ratingBarProfessor);
                professorRate.setRating((float) matchingTutor.getGlobalRating());
                professorRate.setVisibility(View.VISIBLE);
                TextView professorView = (TextView) findViewById(R.id.professorView);
                professorView.setVisibility(View.VISIBLE);

                //RatingBar studentRate=(RatingBar) findViewById(R.id.ratingBarStudent);
                //studentRate.setRating(4f);

                // Set the level TODO: get total progress!!!
                ProgressBar level=(ProgressBar) findViewById(R.id.levelBar);
                level.setProgress(88);

                // Set the taught subjects.
                // TODO: get subject list from database
                boolean isMathsTeacher=matchingTutor.isTeacher("Maths");
                boolean isPhysicsTeacher=matchingTutor.isTeacher("Physics");
                boolean isChemistryTeacher=matchingTutor.isTeacher("Chemistry");
                boolean isComputerTeacher=matchingTutor.isTeacher("Computer");
                setSubjectButtons(matchingTutor, isMathsTeacher,isPhysicsTeacher,isChemistryTeacher,isComputerTeacher);

                // Set the floating button to send an email
                FloatingActionButton sendEmailButton = (FloatingActionButton) findViewById(R.id.fab);
                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{matchingTutor.getEmail()});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Hi! I'm looking for a tutor");
                        startActivity(Intent.createChooser(intent, ""));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    private void setSubjectButtons(final User matchingTutor,
                                   boolean isMathsTeacher,
                                   boolean isPhysicsTeacher,
                                   boolean isChemistryTeacher,
                                   boolean isComputerTeacher){
        // Maths button
        if(isMathsTeacher) {
            ImageButton mathsButton = (ImageButton) findViewById(R.id.mathsButton);
            mathsButton.setImageResource(R.drawable.school);
            mathsButton.setVisibility(View.VISIBLE);
            View.OnClickListener mathsClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMathsProfile(v, matchingTutor);
                }
            };
            mathsButton.setOnClickListener(mathsClick);
        }

        // Physics button
        if(isPhysicsTeacher) {
            ImageButton physicsButton = (ImageButton) findViewById(R.id.physicsButton);
            physicsButton.setImageResource(R.drawable.molecule);
            physicsButton.setVisibility(View.VISIBLE);

            View.OnClickListener physicsClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPhysicsProfile(v, matchingTutor);
                }
            };
            physicsButton.setOnClickListener(physicsClick);
        }

        // Chemistry button
        if(isChemistryTeacher) {
            ImageButton chemistryButton = (ImageButton) findViewById(R.id.chemistryButton);
            chemistryButton.setImageResource(R.drawable.flask);
            chemistryButton.setVisibility(View.VISIBLE);
            View.OnClickListener chemistryClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openChemistryProfile(v, matchingTutor);
                }
            };
            chemistryButton.setOnClickListener(chemistryClick);
        }
        // Computer button
        if(isComputerTeacher) {
            ImageButton computerButton = (ImageButton) findViewById(R.id.computerButton);
            computerButton.setImageResource(R.drawable.computer);
            computerButton.setVisibility(View.VISIBLE);
            View.OnClickListener computerClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openComputerProfile(v, matchingTutor);
                }
            };
            computerButton.setOnClickListener(computerClick);

        }

    }


    private void openMathsProfile(@SuppressWarnings("UnusedParameters") View view, final User matchingTutor) {
        openPresentation("Mathematics",matchingTutor.getCourseDescriprion("Maths"));
        setButtonsToOpen(matchingTutor);
        ImageButton thisButton=(ImageButton) findViewById(R.id.mathsButton);
        View.OnClickListener closeMathsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation(matchingTutor);
            }
        };
        thisButton.setOnClickListener(closeMathsClick);
    }

    private void openPhysicsProfile(@SuppressWarnings("UnusedParameters") View view, final User matchingTutor) {
        openPresentation("Physics", matchingTutor.getCourseDescriprion("Physics"));
        setButtonsToOpen(matchingTutor);
        ImageButton thisButton=(ImageButton) findViewById(R.id.physicsButton);
        View.OnClickListener closePhysicsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation(matchingTutor);
            }
        };
        thisButton.setOnClickListener(closePhysicsClick);
    }

    private void openChemistryProfile(@SuppressWarnings("UnusedParameters") View view, final User matchingTutor) {
        openPresentation("Chemistry",matchingTutor.getCourseDescriprion("Chemistry"));
        setButtonsToOpen(matchingTutor);
        ImageButton thisButton=(ImageButton) findViewById(R.id.chemistryButton);
        View.OnClickListener closeChemistryClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation(matchingTutor);
            }
        };
        thisButton.setOnClickListener(closeChemistryClick);
    }

    private void openComputerProfile(@SuppressWarnings("UnusedParameters") View view, final User matchingTutor) {
        openPresentation("Computer Science",matchingTutor.getCourseDescriprion("Computer"));
        setButtonsToOpen(matchingTutor);

        ImageButton thisButton=(ImageButton) findViewById(R.id.computerButton);
        View.OnClickListener closeComputerClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation(matchingTutor);
            }
        };
        thisButton.setOnClickListener(closeComputerClick);
    }

    private void setButtonsToOpen(final User matchingTutor){
        // Set Maths
        ImageButton button=(ImageButton) findViewById(R.id.mathsButton);
        View.OnClickListener openMathsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMathsProfile(v, matchingTutor);
            }
        };
        button.setOnClickListener(openMathsClick);

        // Set Physics
        button=(ImageButton) findViewById(R.id.physicsButton);

        View.OnClickListener openPhysicsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhysicsProfile(v, matchingTutor);
            }
        };
        button.setOnClickListener(openPhysicsClick);

        // Set Chemistry
        button=(ImageButton) findViewById(R.id.chemistryButton);

        View.OnClickListener openChemistryClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChemistryProfile(v, matchingTutor);
            }
        };
        button.setOnClickListener(openChemistryClick);

        // Set Computer Science
        button=(ImageButton) findViewById(R.id.computerButton);

        View.OnClickListener openComputerClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComputerProfile(v, matchingTutor);
            }
        };
        button.setOnClickListener(openComputerClick);
    }

    private void openPresentation(String name, String presentation) {
        TextView subjectName=(TextView) findViewById(R.id.subjectName);
        subjectName.setText(name);
        subjectName.setVisibility(View.VISIBLE);
        TextView subjectPresentation=(TextView) findViewById(R.id.subjectPresentation);
        subjectPresentation.setText(presentation);
        subjectPresentation.setVisibility(View.VISIBLE);
    }
    private void closePresentation(User matchingTutor) {
        TextView subjectName=(TextView) findViewById(R.id.subjectName);
        subjectName.setVisibility(View.GONE);
        TextView subjectPresentation=(TextView) findViewById(R.id.subjectPresentation);
        subjectPresentation.setVisibility(View.GONE);
        setButtonsToOpen(matchingTutor);
    }

    public void showComments(@SuppressWarnings("UnusedParameters") View view){
        TextView comments=(TextView) findViewById(R.id.commentsView);
        comments.setVisibility(View.VISIBLE);
        Button thisButton=(Button) findViewById(R.id.commentsButton);
        thisButton.setText("Hide comments");
        View.OnClickListener showCommentsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideComments(v);
            }
        };
        thisButton.setOnClickListener(showCommentsClick);
    }
    public void hideComments(@SuppressWarnings("UnusedParameters") View view){
        TextView comments=(TextView) findViewById(R.id.commentsView);
        comments.setVisibility(View.GONE);
        Button thisButton=(Button) findViewById(R.id.commentsButton);
        thisButton.setText("Show comments");
        View.OnClickListener hideCommentsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComments(v);
            }
        };
        thisButton.setOnClickListener(hideCommentsClick);
    }
}

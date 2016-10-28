package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import ch.epfl.sweng.tutosaurus.model.User;

public class PublicProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Create the profiles
        FindTutorResult result=new FindTutorResult();
        User[] profiles = result.createProfiles();

        Intent intent = getIntent();
        String sciperNumber = intent.getStringExtra("SCIPER_NUMBER");

        User matchingTutor=new User("0");
        for(User profile : profiles){
            if(profile.getSciper().equals(sciperNumber)){
                matchingTutor=profile;
            }
        }

        // Set profile name
        TextView profileName= (TextView) findViewById(R.id.profileName);
        profileName.setText(matchingTutor.getFullName());

        // Set profile picture
        ImageView profilePicture=(ImageView) findViewById(R.id.profilePicture);
        profilePicture.setImageResource(matchingTutor.getPicture());

        // Set email
        TextView email = (TextView) findViewById(R.id.emailView);
        email.setText(matchingTutor.getEmail());

        // Set the ratings TODO: get ratings from database
        RatingBar professorRate=(RatingBar) findViewById(R.id.ratingBarProfessor);
        professorRate.setRating((float) matchingTutor.getGlobalRating());
        professorRate.setVisibility(View.VISIBLE);
        TextView professorView = (TextView) findViewById(R.id.professorView);
        professorView.setVisibility(View.VISIBLE);

        //RatingBar studentRate=(RatingBar) findViewById(R.id.ratingBarStudent);
        //studentRate.setRating(4f);

        // Set the level TODO: get progress from database
        ProgressBar level=(ProgressBar) findViewById(R.id.levelBar);
        level.setProgress(88);

        // Set the thaught subjects.
        // TODO: get subject list from database
        boolean isMathsTeacher=matchingTutor.isTeacher("0");
        boolean isPhysicsTeacher=matchingTutor.isTeacher("1");
        boolean isChemistryTeacher=matchingTutor.isTeacher("2");
        boolean isComputerTeacher=matchingTutor.isTeacher("3");
        setSubjectButtons(isMathsTeacher,isPhysicsTeacher,isChemistryTeacher,isComputerTeacher);

    }

    private void setSubjectButtons(boolean isMathsTeacher,
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
                    openMathsProfile(v);
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
                    openPhysicsProfile(v);
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
                    openChemistryProfile(v);
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
                    openComputerProfile(v);
                }
            };
            computerButton.setOnClickListener(computerClick);

        }

    }

    private void openMathsProfile(@SuppressWarnings("UnusedParameters") View view) {
        openPresentation("Mathematics",getResources().getString(R.string.mathsPresExample));
        setButtonsToOpen();
        ImageButton thisButton=(ImageButton) findViewById(R.id.mathsButton);
        View.OnClickListener closeMathsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation();
            }
        };
        thisButton.setOnClickListener(closeMathsClick);
    }

    private void openPhysicsProfile(@SuppressWarnings("UnusedParameters") View view) {
        openPresentation("Physics",getResources().getString(R.string.physicsPresExample));
        setButtonsToOpen();
        ImageButton thisButton=(ImageButton) findViewById(R.id.physicsButton);
        View.OnClickListener closePhysicsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation();
            }
        };
        thisButton.setOnClickListener(closePhysicsClick);
    }

    private void openChemistryProfile(@SuppressWarnings("UnusedParameters") View view) {
        openPresentation("Chemistry",getResources().getString(R.string.chemistryPresExample));
        setButtonsToOpen();
        ImageButton thisButton=(ImageButton) findViewById(R.id.chemistryButton);
        View.OnClickListener closeChemistryClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation();
            }
        };
        thisButton.setOnClickListener(closeChemistryClick);
    }

    private void openComputerProfile(@SuppressWarnings("UnusedParameters") View view) {
        openPresentation("Computer Science",getResources().getString(R.string.computerPresExample));
        setButtonsToOpen();

        ImageButton thisButton=(ImageButton) findViewById(R.id.computerButton);
        View.OnClickListener closeComputerClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePresentation();
            }
        };
        thisButton.setOnClickListener(closeComputerClick);
    }

    private void setButtonsToOpen(){
        // Set Maths
        ImageButton button=(ImageButton) findViewById(R.id.mathsButton);
        View.OnClickListener openMathsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMathsProfile(v);
            }
        };
        button.setOnClickListener(openMathsClick);

        // Set Physics
        button=(ImageButton) findViewById(R.id.physicsButton);

        View.OnClickListener openPhysicsClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhysicsProfile(v);
            }
        };
        button.setOnClickListener(openPhysicsClick);

        // Set Chemistry
        button=(ImageButton) findViewById(R.id.chemistryButton);

        View.OnClickListener openChemistryClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChemistryProfile(v);
            }
        };
        button.setOnClickListener(openChemistryClick);

        // Set Computer Science
        button=(ImageButton) findViewById(R.id.computerButton);

        View.OnClickListener openComputerClick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openComputerProfile(v);
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
    private void closePresentation() {
        TextView subjectName=(TextView) findViewById(R.id.subjectName);
        subjectName.setVisibility(View.GONE);
        TextView subjectPresentation=(TextView) findViewById(R.id.subjectPresentation);
        subjectPresentation.setVisibility(View.GONE);
        setButtonsToOpen();
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

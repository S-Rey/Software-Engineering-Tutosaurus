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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

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

        // Set the ratings TODO: get ratings from database
        RatingBar professorRate=(RatingBar) findViewById(R.id.ratingBarProfessor);
        professorRate.setRating(3.5f);
        RatingBar studentRate=(RatingBar) findViewById(R.id.ratingBarStudent);
        studentRate.setRating(4f);

        // Set the level TODO: get progress from database
        ProgressBar level=(ProgressBar) findViewById(R.id.levelBar);
        level.setProgress(88);

        // Set the thaught subjects.
        // TODO: get subject list from database
        boolean isMathsTeacher=true;
        boolean isPhysicsTeacher=false;
        boolean isChemistryTeacehr=false;
        boolean isComputerTeacher=true;

        // TODO: make a general activity for subject and set specific elements depending on which button is clicked
        setSubjectButtons(isMathsTeacher,isPhysicsTeacher,isChemistryTeacehr,isComputerTeacher);



        Intent intent = getIntent();
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

        }

    }

    public void openMathsProfile(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, MathsProfileActivity.class);
        startActivity(intent);
    }
    public void openPhysicsProfile(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, PhysicsProfileActivity.class);
        //String message = "Physics";
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    public void openChemistryProfile(@SuppressWarnings("UnusedParameters") View view) {
        Intent intent = new Intent(this, ChemistryProfileActivity.class);
        startActivity(intent);
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

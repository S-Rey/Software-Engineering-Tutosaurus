package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by santo on 09/11/16.
 */
public class CreateMeetingActivity extends AppCompatActivity {

    DatabaseHelper dbh = DatabaseHelper.getInstance();

    private static String sTeacherName;
    private static String sStudentName;
    private static Course sSubjectMeeting = new Course();
    public static int sDurationMeeting;
    private static Location sLocationMeeting;
    private static Date sDateMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);

        Button createMeetingButton = (Button) findViewById(R.id.confirmMeeting);
        createMeetingButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                EditText teacherName = (EditText) findViewById(R.id.teacherName);
                sTeacherName = teacherName.getText().toString();
                EditText studentName = (EditText) findViewById(R.id.studentName);
                sStudentName = studentName.getText().toString();
                EditText subjectName = (EditText) findViewById(R.id.subjectMeeting);
                Toast.makeText(getBaseContext(), "ccccc", Toast.LENGTH_SHORT).show();
                addMeeting();
            }

            private void addMeeting() {
                Meeting meeting = new Meeting();
                meeting.addParticipant(sStudentName);
                meeting.addParticipant(sTeacherName);
//                meeting.setLatitudeLocation(sLocationMeeting.getLatitude());
//                meeting.setLongitudeLocation(sLocationMeeting.getLongitude());
                dbh.addMeeting(meeting);
            }
        });

    }

}

package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by santo on 09/11/16.
 */

public class CreateMeetingFragment extends Fragment {

    DatabaseHelper dbh = DatabaseHelper.getInstance();
    public String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    View myView;

    private static String sTeacherName;
    private static String sStudentName;
    private static Course sSubjectMeeting = new Course();
    public static int sDurationMeeting;
    private static Location sLocationMeeting;
    private static Date sDateMeeting;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Button createMeetingButton = (Button) findViewById(R.id.confirmMeeting);
//        createMeetingButton.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View view)
//            {
//                EditText teacherName = (EditText) findViewById(R.id.teacherName);
//                sTeacherName = teacherName.getText().toString();
//                EditText studentName = (EditText) findViewById(R.id.studentName);
//                sStudentName = studentName.getText().toString();
//                EditText subjectName = (EditText) findViewById(R.id.subjectMeeting);
//                Toast.makeText(getBaseContext(), "ccccc", Toast.LENGTH_SHORT).show();
//                addMeeting();
//            }
//
//            private void addMeeting() {
//                Meeting meeting = new Meeting();
//                meeting.addParticipant(currentUser);
//                meeting.addParticipant(sTeacherName);
////                meeting.setLatitudeLocation(sLocationMeeting.getLatitude());
////                meeting.setLongitudeLocation(sLocationMeeting.getLongitude());
//                dbh.addMeeting(meeting);
//            }
//        });
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.create_meeting_fragment, container, false);
        dbh = DatabaseHelper.getInstance();


        return myView;
    }
}

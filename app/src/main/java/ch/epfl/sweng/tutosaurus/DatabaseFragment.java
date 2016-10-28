package ch.epfl.sweng.tutosaurus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseFragment extends Fragment implements View.OnClickListener {

    View myView;
    DatabaseHelper dbh = new DatabaseHelper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.database_fragment, container, false);
        Button signupButton = (Button) myView.findViewById(R.id.db_signup_button);
        signupButton.setOnClickListener(this);
        Button courseButton = (Button) myView.findViewById(R.id.db_course_button);
        courseButton.setOnClickListener(this);
        Button getMeetingButton = (Button) myView.findViewById(R.id.db_meetingID_button);
        getMeetingButton.setOnClickListener(this);
        Button addMeetingButton = (Button) myView.findViewById(R.id.db_meeting_add);
        addMeetingButton.setOnClickListener(this);
        Button addTeacherToCourseButton = (Button) myView.findViewById(R.id.db_course_teach_add);
        addTeacherToCourseButton.setOnClickListener(this);
        Button addStudentToCourseButton = (Button) myView.findViewById(R.id.db_course_learn_add);
        addStudentToCourseButton.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_signup_button :
                signUp();
                break;
            case R.id.db_course_button :
                addCourse();
                break;
            case R.id.db_meetingID_button :
                retrieveMeeting();
                break;
            case R.id.db_meeting_add :
                addMeeting();
                break;
            case R.id.db_course_teach_add :
                addTeacherToCourse();
                break;
            case R.id.db_course_learn_add :
                addStudentToCourse();
                break;
        }
    }

    private void signUp() {
        String sciper = ((EditText)myView.findViewById(R.id.db_signup_sciper)).getText().toString();
        String username = ((EditText)myView.findViewById(R.id.db_signup_username)).getText().toString();
        String fullName = ((EditText)myView.findViewById(R.id.db_signup_name)).getText().toString();
        String email = ((EditText)myView.findViewById(R.id.db_signup_email)).getText().toString();
        User user = new User(sciper, username);
        user.setFullName(fullName);
        user.setEmail(email);

        user.addTeaching("0");
        user.addStudying("1");
        user.addLanguage("English");

        dbh.signUp(user);
    }

    private void addCourse() {
        String id = ((EditText)myView.findViewById(R.id.db_course_id_add)).getText().toString();
        String name = ((EditText)myView.findViewById(R.id.db_course_name)).getText().toString();
        Course course = new Course(id, name);
        dbh.addCourse(course);
    }

    private void retrieveMeeting() {
        String key = ((EditText)myView.findViewById(R.id.db_getMeeting_text)).getText().toString();
        dbh.getMeeting(key);
    }

    private void addMeeting(){
        String location = ((EditText)myView.findViewById(R.id.db_meeting_location)).getText().toString();
        String part1 = ((EditText)myView.findViewById(R.id.db_meeting_part1)).getText().toString();
        String part2 = ((EditText)myView.findViewById(R.id.db_meeting_part2)).getText().toString();
        Date date = new Date();
        Meeting meeting = new Meeting(date, 60, new Course("0", "Maths"));
        meeting.setLocation(location);
        meeting.addParticipant(part1);
        meeting.addParticipant(part2);
        dbh.addMeeting(meeting);
    }

    private void addTeacherToCourse() {
        String sciper = ((EditText)myView.findViewById(R.id.db_sciper_teach_learn)).getText().toString();
        String courseId = ((EditText)myView.findViewById(R.id.db_course_id_teach_learn)).getText().toString();
        dbh.addTeacherToCourse(sciper, Integer.parseInt(courseId));
    }

    private void addStudentToCourse() {
        String sciper = ((EditText)myView.findViewById(R.id.db_sciper_teach_learn)).getText().toString();
        String courseId = ((EditText)myView.findViewById(R.id.db_course_id_teach_learn)).getText().toString();
        dbh.addStudentToCourse(sciper, Integer.parseInt(courseId));
    }
}

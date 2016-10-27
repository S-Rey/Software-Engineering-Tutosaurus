package ch.epfl.sweng.tutosaurus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseFragment extends Fragment implements View.OnClickListener {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.database_fragment, container, false);
        Button testButton = (Button) myView.findViewById(R.id.dbSubmitButton);
        testButton.setOnClickListener(this);
        Button signupButton = (Button) myView.findViewById(R.id.db_signup_button);
        signupButton.setOnClickListener(this);
        Button courseButton = (Button) myView.findViewById(R.id.db_course_button);
        courseButton.setOnClickListener(this);
        Button getMeetingButton = (Button) myView.findViewById(R.id.db_meetingID_button);
        getMeetingButton.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dbSubmitButton :
                insertIntoDb();
                break;
            case R.id.db_signup_button :
                signUp();
                break;
            case R.id.db_course_button :
                addCourse();
                break;
            case R.id.db_meetingID_button :
                retrieveMeeting();
                break;
        }
    }

    private void insertIntoDb(){
        String value = ((EditText)myView.findViewById(R.id.dbTestInput)).getText().toString();
        DatabaseHelper dbh = new DatabaseHelper();
        dbh.writeSomething(value);
    }

    private void signUp() {
        DatabaseHelper dbh = new DatabaseHelper();
        String sciper = ((EditText)myView.findViewById(R.id.db_signup_sciper)).getText().toString();
        String username = ((EditText)myView.findViewById(R.id.db_signup_username)).getText().toString();
        String fullName = ((EditText)myView.findViewById(R.id.db_signup_name)).getText().toString();
        String email = ((EditText)myView.findViewById(R.id.db_signup_email)).getText().toString();
        User user = new User(Integer.parseInt(sciper), username);
        user.setFullName(fullName);
        user.setEmail(email);

        Course maths = new Course(1234, "Maths");
        user.addTeachingCourse(maths);
        Course physics = new Course(5678, "Physics");
        user.addTeachingCourse(physics);

        dbh.signUp(user);
    }

    private void addCourse() {
        DatabaseHelper dbh = new DatabaseHelper();
        String id = ((EditText)myView.findViewById(R.id.db_course_id)).getText().toString();
        String name = ((EditText)myView.findViewById(R.id.db_course_name)).getText().toString();
        String teacher = ((EditText)myView.findViewById(R.id.db_course_teacher)).getText().toString();
        Course course = new Course(Integer.parseInt(id), name);
        dbh.addCourse(course);
    }

    private void retrieveMeeting() {
        DatabaseHelper dbh = new DatabaseHelper();
        String key = ((EditText)myView.findViewById(R.id.db_getMeeting_text)).getText().toString();
        dbh.getMeeting(key);
    }
}

package ch.epfl.sweng.tutosaurus.helper;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseHelper {

    FirebaseDatabase db;

    public DatabaseHelper(){
        super();
        db = FirebaseDatabase.getInstance();
    }

    public void writeSomething(String value) {
        DatabaseReference ref = db.getReference("test");
        ref.setValue(value);
    }

    public void signUp(String sciper, String username, String name, String email) {
        String headPath = "user/" + sciper;
        String pUsername = headPath + "/username";
        String pName = headPath + "/name";
        String pEmail = headPath + "/email";
        Log.d("DBH", "headPath: " + headPath);
        DatabaseReference ref = db.getReference(pUsername);
        ref.setValue(username);
        ref = db.getReference(pName);
        ref.setValue(name);
        ref = db.getReference(pEmail);
        ref.setValue(email);

    }

    public void signUp(User user) {
        String rootPath = "user/" + user.getSciper();
        String pUsername = rootPath + "/username";
        String pName = rootPath + "/name";
        String pEmail = rootPath + "/email";
        DatabaseReference ref = db.getReference(pUsername);
        ref.setValue(user.getUsername());
        ref = db.getReference(pName);
        ref.setValue(user.getFullName());
        ref = db.getReference(pEmail);
        ref.setValue(user.getEmail());
    }

    public void addCourse(String id, String name, String teacher) {
        String headPath = "courses/" + id;
        String pName = headPath + "/name";
        String pTeacher = headPath + "/teachBy";
        Log.d("DBH", "headPath:" + headPath);
        DatabaseReference ref = db.getReference(pName);
        ref.setValue(name);
        ref = db.getReference(pTeacher);
        ref.setValue(teacher);
    }

    public void addCourse(Course course) {
        String rootPath = "course/" + course.getId();
        String pName = rootPath + "/courseName";
        DatabaseReference ref = db.getReference(pName);
        ref.setValue(course.getName());
    }

    public void addMeeting(Meeting meeting) {
        String rootPath = "/meeting" + meeting.getId();
        String pDate = rootPath + "/date";
        String pLocation = rootPath + "/location";
        DatabaseReference ref = db.getReference(pDate);
        ref.setValue(meeting.getDate());
        ref = db.getReference(pLocation);
        ref.setValue(meeting.getLocation());
    }
}
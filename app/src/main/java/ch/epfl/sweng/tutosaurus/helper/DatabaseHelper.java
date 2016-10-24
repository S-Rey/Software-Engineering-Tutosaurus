package ch.epfl.sweng.tutosaurus.helper;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseHelper {

    DatabaseReference db;

    public DatabaseHelper(){
        super();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void writeSomething(String value) {
        DatabaseReference ref = db.child("test");
        ref.setValue(value);
    }

    public void signUp(User user) {
        String key = db.child("users").push().getKey();
        DatabaseReference ref = db.child("user" + key);
        ref.setValue(user);
    }

    public void addCourse(Course course) {
        String key = db.child("course").push().getKey();
        DatabaseReference ref = db.child("course" + key);
        ref.setValue(course);
    }

    public void addMeeting(Meeting meeting) {
        String key = db.child("meeting").push().getKey();
        DatabaseReference ref = db.child("meeting" + key);
        ref.setValue(meeting);
    }
}
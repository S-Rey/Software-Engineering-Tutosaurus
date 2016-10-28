package ch.epfl.sweng.tutosaurus.helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseHelper {

    public static final String MEETING_PATH = "meeting/";
    public static final String USER_PATH = "user/";
    public static final String COURSE_PATH = "course/";

    private DatabaseReference db;

    public DatabaseHelper(){
        super();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void signUp(User user) {
        DatabaseReference ref = db.child(USER_PATH + user.getSciper());
        ref.setValue(user);
    }

    public void addCourse(Course course) {
        DatabaseReference ref = db.child(COURSE_PATH + course.getId());
        ref.setValue(course);
    }

    public void addTeacherToCourse(String sciper, int courseId) {
        DatabaseReference courseRef = db.child(COURSE_PATH + courseId + "/teaching/" + sciper);
        DatabaseReference userTeachCourseRef = db.child(USER_PATH + sciper + "/teaching/" + courseId);
        userTeachCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public void addStudentToCourse(String sciper, int courseId) {
        DatabaseReference courseRef = db.child(COURSE_PATH + courseId + "/studying/" + sciper);
        DatabaseReference userLearnCourseRef = db.child(USER_PATH + sciper + "/studying/" + courseId);
        userLearnCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public String addMeeting(Meeting meeting) {
        String key = db.child(MEETING_PATH).push().getKey();
        meeting.setId(key);
        DatabaseReference meetingRef = db.child(MEETING_PATH + key);
        DatabaseReference userRef = db.child(USER_PATH);
        for (String sciper: meeting.getParticipants()) {
            DatabaseReference ref = userRef.child(sciper + "/meetings/" + meeting.getId());
            ref.setValue(true);
        }
        meetingRef.setValue(meeting);
        DatabaseReference courseMeetingRef = db.child(COURSE_PATH + meeting.getCourse().getId() + "/meeting/" + meeting.getId());
        courseMeetingRef.setValue(true);
        return key;
    }

    public void getMeeting(String key) {
        // Attach a listener to read the data at our posts reference
        db.child("meeting/" + key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Meeting meeting = dataSnapshot.getValue(Meeting.class);
                System.out.println(meeting);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }



}
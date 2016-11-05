package ch.epfl.sweng.tutosaurus.helper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.model.Chat;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.Message;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseHelper {

    public static final String MEETING_PATH = "meeting/";
    public static final String USER_PATH = "user/";
    public static final String COURSE_PATH = "course/";
    public static final String MEETING_PER_USER_PATH = "meetingsPerUser/";

    private FirebaseDatabase db;
    private DatabaseReference dbf;

    private static DatabaseHelper instance = null;

    private DatabaseHelper(){
        db = FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);
        dbf = db.getReference();
    }

    public static DatabaseHelper getInstance() {
        if(instance == null){
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public DatabaseReference getReference(){
        return dbf;
    }

    public void signUp(User user) {
        DatabaseReference ref = dbf.child(USER_PATH + user.getUid());
        ref.setValue(user);
    }

    public void addCourse(Course course) {
        DatabaseReference ref = dbf.child(COURSE_PATH + course.getId());
        ref.setValue(course);
    }

    public void addTeacherToCourse(String sciper, int courseId) {
        DatabaseReference courseRef = dbf.child(COURSE_PATH + courseId + "/teaching/" + sciper);
        DatabaseReference userTeachCourseRef = dbf.child(USER_PATH + sciper + "/teaching/" + courseId);
        userTeachCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public void addStudentToCourse(String sciper, int courseId) {
        DatabaseReference courseRef = dbf.child(COURSE_PATH + courseId + "/studying/" + sciper);
        DatabaseReference userLearnCourseRef = dbf.child(USER_PATH + sciper + "/studying/" + courseId);
        userLearnCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public String addMeeting(Meeting meeting) {
        String key = dbf.child(MEETING_PATH).push().getKey();
        meeting.setId(key);
        DatabaseReference meetingRef = dbf.child(MEETING_PATH + key);
        DatabaseReference userRef = dbf.child(USER_PATH);
        DatabaseReference meetingsPerUserRef = dbf.child(MEETING_PER_USER_PATH);
        for (String sciper: meeting.getParticipants()) {
            DatabaseReference userMeetingsRef = userRef.child(sciper + "/meetings/" + meeting.getId());
            DatabaseReference meetingsPerUserUserRef = meetingsPerUserRef.child(sciper + "/" + meeting.getId());
            userMeetingsRef.setValue(true);
            meetingsPerUserUserRef.setValue(meeting);
        }
        meetingRef.setValue(meeting);
        DatabaseReference courseMeetingRef = dbf.child(COURSE_PATH + meeting.getCourse().getId() + "/meeting/" + meeting.getId());
        courseMeetingRef.setValue(true);
        return key;
    }

    public void getMeeting(String key) {
        // Attach a listener to read the data at our posts reference
        dbf.child("meeting/" + key).addValueEventListener(new ValueEventListener() {
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

    public void sendMessage(String from, String to, String content){
        DatabaseReference chatIdFromRef = dbf.child("chats/" + from);
        DatabaseReference chatIdToRef = dbf.child("chats/" + to);

        String chatKey = chatIdFromRef.push().getKey();
        Chat fromChat = new Chat(to);
        Chat toChat = new Chat(from);
        chatIdFromRef.child(chatKey).setValue(fromChat);
        chatIdToRef.child(chatKey).setValue(toChat);

        DatabaseReference messageFromRef = dbf.child("messages/" + from + "/" +to);
        DatabaseReference messageToRef = dbf.child("messages/" + to + "/" + from);

        String key = messageFromRef.push().getKey();

        long timestamp = (new Date()).getTime();
        Message message = new Message(from, content, timestamp);
        messageFromRef.child(key).setValue(message);
        messageToRef.child(key).setValue(message);
    }

    public DatabaseReference getMeetingsRefForUser(String sciper) {
        return dbf.child(MEETING_PER_USER_PATH + sciper);
    }

}
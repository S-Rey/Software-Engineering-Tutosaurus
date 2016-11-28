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
    public static final String MEETING_REQUEST_PATH = "meetingRequests";
    public static final String MEETING_PER_USER_PATH = "meetingsPerUser/";

    private DatabaseReference dbf;

    private static DatabaseHelper instance = null;

    private DatabaseHelper(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
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

    public void addLanguageToUser(String userId, String languageId) {
        DatabaseReference userSpeakLanguageRef = dbf.child(USER_PATH + userId + "/speaking/" + languageId);
        userSpeakLanguageRef.setValue(true);
    }

    public void removeLanguageFromUser(String userId, String languageId) {
        DatabaseReference userSpeakLanguageRef = dbf.child(USER_PATH + userId + "/speaking/" + languageId);
        userSpeakLanguageRef.setValue(false);
    }

    public void addTeacherToCourse(String userId, String courseId) {
        DatabaseReference courseRef = dbf.child(COURSE_PATH + courseId + "/teaching/" + userId);
        DatabaseReference userTeachCourseRef = dbf.child(USER_PATH + userId + "/teaching/" + courseId);
        userTeachCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public void removeTeacherFromCourse(String userId, String courseId) {
        DatabaseReference courseRef = dbf.child(COURSE_PATH + courseId + "/teaching/" + userId);
        DatabaseReference userTeachCourseRef = dbf.child(USER_PATH + userId + "/teaching/" + courseId);
        userTeachCourseRef.setValue(false);
        courseRef.setValue(false);
    }

    public void addStudentToCourse(String userId, String courseId) {
        DatabaseReference courseRef = dbf.child(COURSE_PATH + courseId + "/studying/" + userId);
        DatabaseReference userLearnCourseRef = dbf.child(USER_PATH + userId + "/studying/" + courseId);
        userLearnCourseRef.setValue(true);
        courseRef.setValue(true);
    }

    public String addMeeting(Meeting meeting) {
        String key = dbf.child(MEETING_PATH).push().getKey();
        meeting.setId(key);
        DatabaseReference meetingRef = dbf.child(MEETING_PATH).child(key);
        DatabaseReference userRef = dbf.child(USER_PATH);
        DatabaseReference meetingsPerUserRef = dbf.child(MEETING_PER_USER_PATH);
        for (String userKey: meeting.getParticipants()) {
            DatabaseReference userMeetingsRef = userRef.child(userKey + "/meetings/" + meeting.getId());
            DatabaseReference meetingsPerUserUserRef = meetingsPerUserRef.child(userKey + "/" + meeting.getId());
            userMeetingsRef.setValue(true);
            meetingsPerUserUserRef.setValue(meeting);
        }
        meetingRef.setValue(meeting);
        if (meeting.getCourse() != null) {
            DatabaseReference courseMeetingRef = dbf.child(COURSE_PATH + meeting.getCourse().getId() + "/meeting/" + meeting.getId());
            courseMeetingRef.setValue(true);
        }
        return key;
    }

    public void requestMeeting(Meeting meeting, String teacher, String student) {
        String key = dbf.child(MEETING_REQUEST_PATH).child(student).push().getKey();
        meeting.setId(key);
        DatabaseReference teacherMeetingRef = dbf.child(MEETING_REQUEST_PATH).child(teacher).child(key).child("meeting");
        DatabaseReference teacherTypeRef = dbf.child(MEETING_REQUEST_PATH).child(teacher).child(key).child("type");
        DatabaseReference teacherAcceptedRef = dbf.child(MEETING_REQUEST_PATH).child(teacher).child(key).child("accepted");
        DatabaseReference teacherFromRef = dbf.child(MEETING_REQUEST_PATH).child(teacher).child(key).child("from");

        teacherMeetingRef.setValue(meeting);
        teacherTypeRef.setValue("received");
        teacherAcceptedRef.setValue(false);
        teacherFromRef.setValue(student);
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

    public void sendMessage(String fromUid, String fromFullName, String toUid, String toFullName, String content){
        DatabaseReference chatIdFromRef = dbf.child("chats/" + fromUid);
        DatabaseReference chatIdToRef = dbf.child("chats/" + toUid);

        Chat fromChat = new Chat(toUid);
        fromChat.setFullName(toFullName);
        Chat toChat = new Chat(fromUid);
        toChat.setFullName(fromFullName);
        chatIdFromRef.child(toUid).setValue(fromChat);
        chatIdToRef.child(fromUid).setValue(toChat);

        DatabaseReference messageFromRef = dbf.child("messages/" + fromUid + "/" +toUid);
        DatabaseReference messageToRef = dbf.child("messages/" + toUid + "/" + fromUid);

        String key = messageFromRef.push().getKey();

        long timestamp = (new Date()).getTime();
        Message message = new Message(fromUid, content, timestamp);
        messageFromRef.child(key).setValue(message);
        messageToRef.child(key).setValue(message);
    }

    public void addSubjectDescription(String description, String userId, String courseId){
        DatabaseReference userLearnCourseRef = dbf.child(USER_PATH + userId + "/coursePresentation/" + courseId);
        userLearnCourseRef.setValue(description);
    }

    public DatabaseReference getMeetingsRefForUser(String key) {
        return dbf.child(MEETING_PER_USER_PATH + key);
    }

    public DatabaseReference getUserRef() {
        return dbf.child(USER_PATH);
    }
}
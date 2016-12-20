package ch.epfl.sweng.tutosaurus.helper;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.model.Chat;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;
import ch.epfl.sweng.tutosaurus.model.Message;
import ch.epfl.sweng.tutosaurus.model.User;

public class DatabaseHelper {

    private final static String TAG = "DatabaseHelper";

    private static final String MEETING_PATH = "meeting/";
    public static final String USER_PATH = "user/";
    private static final String COURSE_PATH = "course/";
    public static final String MEETING_REQUEST_PATH = "meetingRequests";
    private static final String MEETING_PER_USER_PATH = "meetingsPerUser/";

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


    public void addLanguageToUser(String userId, String languageId) {
        DatabaseReference userSpeakLanguageRef = dbf.child(USER_PATH + userId + "/speaking/" + languageId);
        userSpeakLanguageRef.setValue(true);
    }


    public void removeLanguageFromUser(String userId, String languageId) {
        DatabaseReference userSpeakLanguageRef = dbf.child(USER_PATH + userId + "/speaking/" + languageId);
        userSpeakLanguageRef.setValue(false);
    }



    public void setRating(String userId, float globalRating) {
        DatabaseReference userRatingRef = dbf.child(USER_PATH + userId + "/globalRating/");
        userRatingRef.setValue(globalRating);
    }


    public void setNumRatings(String userId, int numRatings) {
        DatabaseReference userNumRatingRef = dbf.child(USER_PATH + userId + "/numRatings/");
        userNumRatingRef.setValue(numRatings);
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


    private String addMeeting(Meeting meeting) {
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


    // the 'teacher' String is the teacher uid
    public String requestMeeting(MeetingRequest request, String teacher) {
        String key = dbf.child(MEETING_REQUEST_PATH).child(request.getFrom()).push().getKey();
        request.setUid(key);
        DatabaseReference requestRef = dbf.child(MEETING_REQUEST_PATH).child(teacher).child(key);
        requestRef.setValue(request);
        return key; // return the key of this request
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

        DatabaseReference messageFromRef = dbf.child("messages/" + fromUid + "/" + toUid);
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


    public DatabaseReference getMeetingRequestsRef() {
        return dbf.child(MEETING_REQUEST_PATH);
    }


    public String confirmMeeting(String currentUserUid, MeetingRequest request) {
        String meetingId;
        DatabaseReference meetingRequestRef = dbf.child(MEETING_REQUEST_PATH).child(currentUserUid).child(request.getUid());
        Meeting meeting = request.getMeeting();
        meetingId = addMeeting(meeting);
        Log.d(TAG, "meeting added: " + meeting.getId());
        meetingRequestRef.removeValue();
        return meetingId;
    }


    public void setMeetingRated(String userId, String meetingId, float rating) {
        DatabaseReference meetingsPerUserCurrentUserRef = dbf.child(MEETING_PER_USER_PATH + userId +"/" + meetingId + "/rated/");
        meetingsPerUserCurrentUserRef.setValue(true);

        DatabaseReference meetingsPerUserCurrentUserRefRating = dbf.child(MEETING_PER_USER_PATH + userId +"/" + meetingId + "/rating/");
        meetingsPerUserCurrentUserRefRating.setValue(rating);

    }
}
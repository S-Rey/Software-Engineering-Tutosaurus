package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

public class MeetingConfirmationActivityTest {

    String currentUserUid;
    String studentUid = "RandomTestUser-0000000";

    @Rule
    public ActivityTestRule<MeetingConfirmationActivity> rule = new ActivityTestRule<>(
            MeetingConfirmationActivity.class,
            true,
            false
    );

    @Before
    public void logInAndRequestMeeting() {
        Task<AuthResult> loginTask = FirebaseAuth.getInstance().signInWithEmailAndPassword("albert.einstein@epfl.ch", "tototo");
        try {
            Tasks.await(loginTask);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        removeAllMeetings();

        Date date = new Date();

        Meeting meeting = new Meeting();
        meeting.setDescription("This is a test meeting");
        meeting.setDate(date);
        meeting.addParticipant(currentUserUid);

        MeetingRequest request = new MeetingRequest();
        request.setMeeting(meeting);
        request.setFrom(studentUid);

        DatabaseHelper.getInstance().requestMeeting(request, currentUserUid);
    }

    @Test
    public void requestDisappearsAfterConfirmation(){
        rule.launchActivity(new Intent());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.meeting_confirmation_row_confirm)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.meeting_confirmation_row_confirm)).check(doesNotExist());
    }

    @After
    public void removeAllMeetings() {
        DatabaseReference meetingRef = DatabaseHelper.getInstance().getReference().child(DatabaseHelper.MEETING_PATH);
        DatabaseReference meetingPerUserRef = DatabaseHelper.getInstance().getReference().child(DatabaseHelper.MEETING_PER_USER_PATH);
        DatabaseReference meetingRequestRef = DatabaseHelper.getInstance().getReference().child(DatabaseHelper.MEETING_REQUEST_PATH);

        meetingRequestRef.child(currentUserUid).removeValue();

        meetingRef.child(currentUserUid).removeValue();
        meetingRef.child(studentUid).removeValue();

        meetingPerUserRef.child(currentUserUid).removeValue();
        meetingPerUserRef.child(studentUid).removeValue();
    }
}
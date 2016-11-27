package ch.epfl.sweng.tutosaurus;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.adapter.MeetingConfirmationAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;


public class MeetingConfirmationActivity extends AppCompatActivity{

    private String currentUserUid;
    private DatabaseHelper dbh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbh = DatabaseHelper.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            currentUserUid = currentUser.getUid();
        }
        setContentView(R.layout.activity_meeting_confirmation);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Confirm meetings");

        Query meetingRequestsRef = dbh.getMeetingRequestsRef().child(currentUserUid);
        MeetingConfirmationAdapter adapter = new MeetingConfirmationAdapter(this, MeetingRequest.class, R.layout.meeting_confirmation_row, meetingRequestsRef);

        ListView reqList = (ListView)findViewById(R.id.meeting_request_list);
        reqList.setAdapter(adapter);
    }



}

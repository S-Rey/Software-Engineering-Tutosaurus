package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.tutosaurus.adapter.MessageListAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Message;

public class ChatActivity extends AppCompatActivity {

    private String currentUser;
    private String otherUser;
    private DatabaseHelper dbh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        dbh = DatabaseHelper.getInstance();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUser = intent.getStringExtra(MessagingFragment.EXTRA_MESSAGE_USER_ID);

        dbh.getReference().child(DatabaseHelper.USER_PATH).child(otherUser).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.getValue(String.class);
                mActionBar.setTitle(fullName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference conversationRef = dbh.getReference().child("messages").child(currentUser).child(otherUser);

        MessageListAdapter adapter = new MessageListAdapter(this, Message.class, R.layout.chat_message_row, conversationRef, currentUser, otherUser);
        ListView messageList = (ListView) findViewById(R.id.chat_message_list);
        messageList.setAdapter(adapter);
    }
}

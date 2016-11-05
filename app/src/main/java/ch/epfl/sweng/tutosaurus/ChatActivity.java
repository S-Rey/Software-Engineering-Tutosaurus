package ch.epfl.sweng.tutosaurus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.tutosaurus.adapter.MessageListAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Message;

import static ch.epfl.sweng.tutosaurus.RegisterScreenActivity.PROFILE_INFOS;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private String currentUser;
    private String otherUser;
    private DatabaseHelper dbh;
    private String currentFullName;
    private String otherFullName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        dbh = DatabaseHelper.getInstance();
        SharedPreferences settings = getSharedPreferences(FirebaseAuth.getInstance().getCurrentUser().getEmail(), Context.MODE_PRIVATE);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        currentFullName = settings.getString("firstName", "") + " " + settings.getString("lastName", "");
        Intent intent = getIntent();
        otherUser = intent.getStringExtra(MessagingFragment.EXTRA_MESSAGE_USER_ID);
        otherFullName = intent.getStringExtra(MessagingFragment.EXTRA_MESSAGE_FULL_NAME);
        Log.d(TAG, "otherFullName: " + otherFullName);
        Log.d(TAG, "currentFullName: " + currentFullName);

        mActionBar.setTitle(otherFullName);

        Query conversationRef = dbh.getReference().child("messages").child(currentUser).child(otherUser);
        conversationRef = conversationRef.orderByChild("timestamp");

        MessageListAdapter adapter = new MessageListAdapter(this, Message.class, R.layout.chat_message_row, conversationRef, currentUser, otherUser);
        ListView messageList = (ListView) findViewById(R.id.chat_message_list);
        messageList.setAdapter(adapter);

        ImageButton sendButton = (ImageButton) findViewById(R.id.chat_message_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ((EditText)findViewById(R.id.chat_message_input)).getText().toString();
                if(!message.equals("")){
                    dbh.sendMessage(currentUser, currentFullName, otherUser, otherFullName, message);
                    EditText messageInput = (EditText) findViewById(R.id.chat_message_input);
                    messageInput.getText().clear();
                }
            }
        });
    }
}

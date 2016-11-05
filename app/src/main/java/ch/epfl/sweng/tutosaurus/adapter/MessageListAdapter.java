package ch.epfl.sweng.tutosaurus.adapter;


import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Message;

public class MessageListAdapter extends FirebaseListAdapter<Message>{

    private String currentUser;
    private String otherUser;

    public MessageListAdapter(Activity activity, Class<Message> modelClass, int modelLayout, Query query, String currentUser, String otherUser) {
        super(activity, modelClass, modelLayout, query);
        this.currentUser = currentUser;
        this.otherUser = otherUser;
    }

    @Override
    protected void populateView(View mainView, Message message, int position) {
        TextView content = (TextView) mainView.findViewById(R.id.chat_message_row_content);
        String from = message.getFrom();
        if (from.equals(currentUser)) {
            content.setGravity(Gravity.RIGHT);
            content.setPadding(30, 0, 0, 0);
        } else {
            content.setGravity(Gravity.LEFT);
            content.setPadding(0, 0, 30, 0);
        }
        content.setText(message.getContent());
    }
}

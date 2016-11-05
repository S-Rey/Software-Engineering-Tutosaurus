package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.Chat;


public class ChatListAdapter extends FirebaseListAdapter<Chat>{

    public ChatListAdapter(Activity activity, java.lang.Class<Chat> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(final View mainView, Chat chat, int position) {
        TextView chatName = (TextView) mainView.findViewById(R.id.message_chat_row_name);
        chatName.setText(chat.getOtherUserUid());
    }
}

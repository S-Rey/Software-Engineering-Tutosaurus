package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.adapter.ChatListAdapter;
import ch.epfl.sweng.tutosaurus.adapter.UserListAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Chat;
import ch.epfl.sweng.tutosaurus.model.Identifiable;
import ch.epfl.sweng.tutosaurus.model.User;

public class MessagingFragment extends Fragment {

    View myView;
    private ChatListAdapter chatListAdapter;
    private UserListAdapter userListAdapter;
    private DatabaseHelper dbh;
    private String currentUser;
    private ListView listView;

    public static final String EXTRA_MESSAGE_USER_ID = "ch.epfl.sweng.tutosaurus.USER_ID";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.messaging_fragment, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Messages");
        dbh = DatabaseHelper.getInstance();
        listView = (ListView) myView.findViewById(R.id.message_list);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query chatRef = dbh.getReference().child("chats").child(currentUser);
        Query userRef = dbh.getReference().child("user");

        chatListAdapter = new ChatListAdapter(getActivity(), Chat.class, R.layout.message_chat_row, chatRef);
        userListAdapter = new UserListAdapter(getActivity(), User.class, R.layout.message_user_row, userRef);

        listView.setAdapter(userListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Identifiable item = (Identifiable) listView.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EXTRA_MESSAGE_USER_ID, item.getUid());
                startActivity(intent);
            }
        });
        return myView;
    }
}

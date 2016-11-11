package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.User;

public class UserListAdapter extends FirebaseListAdapter<User> {

    public UserListAdapter(Activity activity, java.lang.Class<User> modelClass, int modelLayout, Query query) {
        super(activity, modelClass, modelLayout, query);
    }

    @Override
    protected void populateView(View mainView, User user, int position) {
        TextView userFullName = (TextView) mainView.findViewById(R.id.message_user_row_fullName);
        userFullName.setText(user.getFullName());
    }
}

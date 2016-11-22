package ch.epfl.sweng.tutosaurus.findTutor;

        import android.app.Activity;
        import android.content.Intent;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.firebase.ui.database.FirebaseListAdapter;
        import com.google.firebase.database.Query;

        import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
        import ch.epfl.sweng.tutosaurus.R;
        import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by albertochiappa on 01/11/16.
 */

public class FirebaseTutorAdapter extends FirebaseListAdapter<User> {

    public FirebaseTutorAdapter(Activity activity, java.lang.Class<User> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(View view, User tutor, int position) {
        TextView profileName = (TextView) view.findViewById(R.id.profileName);
        profileName.setText(tutor.getFullName());
        ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        profilePicture.setImageResource(tutor.getPicture());

        // Set the OnClickListener on each name of the list
        final Intent intent = new Intent(view.getContext(), PublicProfileActivity.class);
        intent.putExtra("USER_ID", tutor.getUid());

        profileName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });
    }


}
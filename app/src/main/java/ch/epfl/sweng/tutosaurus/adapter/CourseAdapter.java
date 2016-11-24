package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;

/**
 * Created by santo on 22/11/16.
 */

public class CourseAdapter extends FirebaseListAdapter<Course> {

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    public CourseAdapter(Activity activity, java.lang.Class<Course> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
    }

    @Override
    protected void populateView(final View mainView, final Course course, int position) {

        TextView profileName = (TextView) mainView.findViewById(R.id.courseName);
        profileName.setText(course.getName());
        //ImageView profilePicture = (ImageView) mainView.findViewById(R.id.coursePicture);
        //profilePicture.setImageResource(course.getPicture());

        // Set the OnClickListener on each name of the list
        final Intent intent = new Intent(mainView.getContext(), PublicProfileActivity.class);
        intent.putExtra("COURSE_ID", course.getId());

        profileName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });

    }

}

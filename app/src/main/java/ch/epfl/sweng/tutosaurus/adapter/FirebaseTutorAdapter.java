package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
        import android.util.Log;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.database.Query;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
        import ch.epfl.sweng.tutosaurus.R;
        import ch.epfl.sweng.tutosaurus.model.User;

public class FirebaseTutorAdapter extends FirebaseListAdapter<User> {

    private static final String TAG = "FirebaseTutorAdapter";

    private Map<Integer, ImageView> picMap = new HashMap<>();

    Activity activity;

    public FirebaseTutorAdapter(Activity activity, java.lang.Class<User> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View view, User tutor, int position) {
        Log.d(TAG, "Populating view at position " + position + " with tutor " + tutor.getFullName());
        TextView profileName = (TextView) view.findViewById(R.id.profileName);
        profileName.setText(tutor.getFullName());
        ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        profilePicture.setImageResource(R.drawable.calculator);
        picMap.put(position, profilePicture);
        //profilePicture.setImageResource(tutor.getPicture());

        // Set the OnClickListener on each name of the list
        final Intent intent = new Intent(view.getContext(), PublicProfileActivity.class);
        intent.putExtra("USER_ID", tutor.getUid());

        profileName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
            }
        });

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
        Log.d(TAG, "tutor: " +tutor.getSciper() + " " + tutor.getFullName());
        StorageReference picRef = storageRef.child("profilePictures").child(tutor.getSciper()+".png");
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(picRef)
                .into(profilePicture);
    }
}
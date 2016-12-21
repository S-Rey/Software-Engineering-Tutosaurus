package ch.epfl.sweng.tutosaurus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.tutosaurus.PublicProfileActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.model.User;

public class FirebaseTutorAdapter extends FirebaseListAdapter<User> {

    private static final String TAG = "FirebaseTutorAdapter";

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
        final ImageView profilePicture = (ImageView) view.findViewById(R.id.profilePicture);

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
        final StorageReference picRef = storageRef.child("profilePictures").child(tutor.getSciper()+".png");

        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(picRef)
                /* Glide uses the hash of the path to determine cache invalidation. There is no easy way to determine
                * if a file with the same path has changed. A workaround is to define a signature that is always
                * different so that Glide fetches the data each time. */
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(profilePicture);
    }
}
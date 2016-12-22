package ch.epfl.sweng.tutosaurus.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.activity.HomeScreenActivity;
import ch.epfl.sweng.tutosaurus.adapter.MeetingAdapter;
import ch.epfl.sweng.tutosaurus.adapter.MeetingConfirmationAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.helper.LocalDatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;
import ch.epfl.sweng.tutosaurus.model.User;


public class ProfileFragment extends Fragment {

    private View myView;

    private String currentUser;
    DatabaseHelper dbh = DatabaseHelper.getInstance();
    private MeetingAdapter adapter;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        final Activity activity = getActivity();
        ((HomeScreenActivity)activity).setActionBarTitle("Profile");
        loadImageFromStorage();

        DatabaseHelper dbh = DatabaseHelper.getInstance();

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentFirebaseUser != null) {
            currentUser = currentFirebaseUser.getUid();
        }

        final String userId = currentUser;

        DatabaseReference ref = dbh.getReference();
        ref.child("user/" + userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User thisUser = dataSnapshot.getValue(User.class);

                // Set profile name
                TextView profileName = (TextView) myView.findViewById(R.id.profileName);
                profileName.setText(thisUser.getFullName());
                saveUserLocalDB(thisUser, activity);
                /*
                // Set profile picture
                    ImageView profilePicture=(ImageView) findViewById(R.id.profilePicture);
                profilePicture.setImageResource(user.getPicture());
                */

                // Set rating
                RatingBar ratingBar = (RatingBar) myView.findViewById(R.id.ratingBar);
                ratingBar.setRating(thisUser.getGlobalRating());
                //getImage(thisUser.getSciper());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                // if offline, retrieve user from local database
                User user = getUserLocalDB(activity);
                if (user == null) {
                    return;
                }
                TextView profileName = (TextView) myView.findViewById(R.id.profileName);
                profileName.setText(user.getFullName());
                RatingBar ratingBar = (RatingBar) myView.findViewById(R.id.ratingBar);
                ratingBar.setRating(user.getGlobalRating());
            }
        });



        Query refRequestedMeeting = dbh.getMeetingRequestsRef().child(currentUser);
        ListView meetingRequested = (ListView) myView.findViewById(R.id.meetingRequests);
        MeetingConfirmationAdapter requestedMeetingAdapter = new MeetingConfirmationAdapter(getActivity(),
                                                                                            MeetingRequest.class,
                                                                                            R.layout.meeting_confirmation_row,
                                                                                            refRequestedMeeting);
        meetingRequested.setAdapter(requestedMeetingAdapter);
        return myView;
    }

    private void loadImageFromStorage() {
        FileInputStream in;
        try {
            in = getActivity().getApplicationContext().openFileInput("user_profile_pic.bmp");
            Bitmap b = BitmapFactory.decodeStream(in);
            ImageView img = (ImageView) myView.findViewById(R.id.picture_view);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e) {
            ImageView img = (ImageView) myView.findViewById(R.id.picture_view);
            img.setImageResource(R.drawable.dino_logo);
            e.printStackTrace();
        }
//        User user = getUserLocalDB(getActivity().getApplicationContext());
//        if(user != null) {
//            getImage(user.getSciper());
//        }



    }

    /**
     * Download a picture from the sciper/ folder from the storage of Firebase
     * @param key the name of the picture
     */
//     private void getImage(String key) {
//        StorageReference storageRef = FirebaseStorage.getInstance().
//                getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
//        final long MAX_SIZE = 4096 * 4096;
//         storageRef.child("profilePictures/" + key + ".png").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//             @Override
//             public void onSuccess(byte[] bytes) {
//                 //Toast.makeText( getActivity().getBaseContext(),"hello",Toast.LENGTH_LONG).show();
//                 ImageView img = (ImageView) myView.findViewById(R.id.picture_view);
//                 Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                 img.setImageBitmap(bmp);
//                 saveToInternalStorage(bmp);
//                 NavigationView navigationView = (NavigationView) myView.findViewById(R.id.nav_view);
//                 if(navigationView!=null) {
//                     CircleImageView circleView = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleView);
//                     FileInputStream in;
//                     try {
//                         in = getActivity().openFileInput("user_profile_pic.bmp");
//                         Bitmap b = BitmapFactory.decodeStream(in);
//                         circleView.setImageBitmap(b);
//                         Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
//
//                     }
//                     catch (FileNotFoundException e) {
//                         circleView.setImageResource(R.drawable.dino_logo);
//                         e.printStackTrace();
//                     }
//                 } else {
//                 }
//
//             }
//         }).addOnFailureListener(new OnFailureListener() {
//             @Override
//             public void onFailure(@NonNull Exception exception) {
//                 // Handle any errors
//             }
//         });
//
//
//    }


    private void saveUserLocalDB(User user, Context context) {
        dbHelper = new LocalDatabaseHelper(context);
        Activity activity = getActivity();
        if(dbHelper != null) {
            database = dbHelper.getWritableDatabase();
            LocalDatabaseHelper.insertUser(user, database);
        }
    }

    @Nullable
    private User getUserLocalDB(Context context) {
        dbHelper = new LocalDatabaseHelper(context);
        Activity activity = getActivity();
        if(dbHelper != null) {
            database = dbHelper.getReadableDatabase();
            return LocalDatabaseHelper.getUser(database);
        }
        return null;
    }


}

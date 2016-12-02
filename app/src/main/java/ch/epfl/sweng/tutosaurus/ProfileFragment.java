package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ch.epfl.sweng.tutosaurus.adapter.MeetingAdapter;
import ch.epfl.sweng.tutosaurus.adapter.MeetingConfirmationAdapter;
import ch.epfl.sweng.tutosaurus.adapter.MeetingRatingAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.MeetingRequest;
import ch.epfl.sweng.tutosaurus.model.User;

import static android.view.View.GONE;

public class ProfileFragment extends Fragment {

    View myView;

    private String currentUser;
    DatabaseHelper dbh = DatabaseHelper.getInstance();
    private MeetingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Profile");
        loadImageFromStorage();

        ((HomeScreenActivity) getActivity()).setPassTabToOpen(myView);
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
                /*
                // Set profile picture
                    ImageView profilePicture=(ImageView) findViewById(R.id.profilePicture);
                profilePicture.setImageResource(user.getPicture());
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
    }

    /**
     * Download a picture from the sciper/ folder from the storage of Firebase
     * @param key
     * @return
     */
     private void getImage(String key) {
        StorageReference storageRef = FirebaseStorage.getInstance().
                getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
        final long MAX_SIZE = 2048 * 2048;
         storageRef.child("profilePictures/" + key + ".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
             @Override
             public void onSuccess(byte[] bytes) {
                 Toast.makeText( getActivity().getBaseContext(),"hello",Toast.LENGTH_LONG).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                 // Handle any errors
                 Toast.makeText( getActivity().getBaseContext(),"Erreur ma gueule !",Toast.LENGTH_LONG).show();

             }
         });


    }


}










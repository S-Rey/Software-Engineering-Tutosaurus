package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.helper.PictureHelper;

public class ProfileFragment extends Fragment {

    View myView;

    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Profile");
        loadImageFromStorage();

        ((HomeScreenActivity) getActivity()).setPassTabToOpen(myView);

        return myView;
    }

    private void loadImageFromStorage() {
        FileInputStream in = null;
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
     * @param namePic
     * @param sciper
     * @return
     */
     private void getImage(String namePic, String sciper) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().
                getReferenceFromUrl("gs://tutosaurus-16fce.appspot.com");
        final long MAX_SIZE = 2048 * 2048;
         storageRef.child(sciper + "/" + namePic + ".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

       /* StorageReference picRef = storageRef.child(sciper + "/" + namePic + ".jpg");
        picRef.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Toast.makeText( getActivity().getBaseContext(),"hello",Toast.LENGTH_LONG).show();
                Bitmap pic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView img = (ImageView) myView.findViewById(R.id.picture_view);
                img.setImageBitmap(pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText( getActivity().getBaseContext(),"Erreur ma gueule !",Toast.LENGTH_LONG).show();

            }
        });*/

    }

}










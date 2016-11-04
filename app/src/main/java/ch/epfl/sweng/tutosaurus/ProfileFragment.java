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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ch.epfl.sweng.tutosaurus.helper.PictureHelper;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    View myView;
    CheckBox computer_science;
    CheckBox mathematics;
    CheckBox chemistry;
    CheckBox physics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Profile");
        loadImageFromStorage();

        SharedPreferences pref;
        final SharedPreferences.Editor editor;
        final String MY_SHARED_PREF = "checkbox_pref";
        pref = this.getActivity().getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        mathematics = (CheckBox) myView.findViewById(R.id.mathematics_checkbox);
        mathematics.setChecked(pref.getBoolean("mathematics_checkbox", false));
        physics = (CheckBox) myView.findViewById(R.id.physics_checkbox);
        physics.setChecked(pref.getBoolean("physics_checkbox", false));
        chemistry = (CheckBox) myView.findViewById(R.id.chemistry_checkbox);
        chemistry.setChecked(pref.getBoolean("chemistry_checkbox", false));
        computer_science = (CheckBox) myView.findViewById(R.id.computer_science_checkbox);
        computer_science.setChecked(pref.getBoolean("computer_science_checkbox", false));
        Button buttonSavePic = (Button) myView.findViewById(R.id.button_save_picture);
        buttonSavePic.setOnClickListener(this);
        Button buttonLoadPic = (Button) myView.findViewById(R.id.button_load_picture);
        buttonLoadPic.setOnClickListener(this);

        editor = pref.edit();

        CheckBox mathematics_check = (CheckBox) myView.findViewById(R.id.mathematics_checkbox);
        mathematics_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    editor.putBoolean("mathematics_checkbox", true);
                    editor.commit();
                } else {
                    editor.putBoolean("mathematics_checkbox", false);
                    editor.commit();
                }
            }
        });

        CheckBox physics_check = (CheckBox) myView.findViewById(R.id.physics_checkbox);
        physics_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    editor.putBoolean("physics_checkbox", true);
                    editor.commit();
                } else {
                    editor.putBoolean("physics_checkbox", false);
                    editor.commit();
                }
            }
        });

        CheckBox chemistry_check = (CheckBox) myView.findViewById(R.id.chemistry_checkbox);
        chemistry_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    editor.putBoolean("chemistry_checkbox", true);
                    editor.commit();
                } else {
                    editor.putBoolean("chemistry_checkbox", false);
                    editor.commit();
                }
            }
        });

        CheckBox computer_science_check = (CheckBox) myView.findViewById(R.id.computer_science_checkbox);
        computer_science_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked()){
                    editor.putBoolean("computer_science_checkbox", true);
                    editor.commit();
                } else {
                    editor.putBoolean("computer_science_checkbox", false);
                    editor.commit();
                }
            }
        });

        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_picture:
                Bitmap pic = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
                        R.drawable.dino_logo);
                PictureHelper.savePicture(getActivity(), "profile", pic);
                break;
            case R.id.button_load_picture:
                try {
                    PictureHelper.storeProfilePic(getActivity(), "111111");
                    getImage("profile", "111111");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
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










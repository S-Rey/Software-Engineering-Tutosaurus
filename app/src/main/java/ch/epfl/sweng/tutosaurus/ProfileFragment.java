package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {

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

}

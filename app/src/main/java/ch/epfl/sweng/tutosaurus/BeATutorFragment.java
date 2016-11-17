package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;

public class BeATutorFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    DatabaseHelper dbh = DatabaseHelper.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Be A Tutor");
        addPreferencesFromResource(R.xml.be_a_tutor_preferences_layout);

        // remove dividers
        View rootView = getView();
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setDividerHeight(0);
        list.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        list.setVerticalScrollBarEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("checkbox_preference_mathematics")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_mathematics", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Maths");
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Maths");
            }
        }
        if (key.equals("checkbox_preference_physics")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_physics", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Physics");
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Physics");
            }
        }
        if (key.equals("checkbox_preference_chemistry")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_chemistry", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Chemistry");
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Chemistry");
            }
        }
        if (key.equals("checkbox_preference_computer_science")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_computer_science", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Computer");
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Computer");
            }
        }
    }
}

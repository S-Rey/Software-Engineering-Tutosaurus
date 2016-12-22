package ch.epfl.sweng.tutosaurus.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.tutosaurus.activity.HomeScreenActivity;
import ch.epfl.sweng.tutosaurus.R;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.helper.LocalDatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;
import ch.epfl.sweng.tutosaurus.model.User;

import static java.util.Arrays.*;

public class BeATutorFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DatabaseHelper dbh = DatabaseHelper.getInstance();
    private String currentuserUid;
    private ArrayList<Course> courses;
    private List<String> languages = asList("english", "french", "german", "italian", "chinese", "russian");


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Be A Tutor");
        addPreferencesFromResource(R.xml.be_a_tutor_preferences_layout);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            currentuserUid = currentUser.getUid();
        }
        // remove dividers
        View rootView = getView();
        ListView list;
        if (rootView != null) {
            list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDividerHeight(0);
        }

        String userId = currentuserUid;
        DatabaseReference ref = dbh.getReference();
        ref.child("user/" + userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User thisUser = dataSnapshot.getValue(User.class);

                //Set subject preferences
                courses = FullCourseList.getInstance().getListOfCourses();
                for (Course course : courses) {
                    String courseName = course.getId();
                    CheckBoxPreference checkBoxSub = (CheckBoxPreference) getPreferenceManager().findPreference(
                            "checkbox_preference_" + courseName);
                    checkBoxSub.setChecked(thisUser.getTeaching().get(courseName));
                }

                //Set description preferences
                for (Course course : courses) {
                    String courseName = course.getId();
                    EditTextPreference descriptionTextSub = (EditTextPreference) getPreferenceManager().findPreference(
                            "edit_text_preference_" + courseName);
                    descriptionTextSub.setText(thisUser.getCourseDescription(courseName));
                    descriptionTextSub.setTitle(descriptionTextSub.getText());

                    if (!((CheckBoxPreference) findPreference("checkbox_preference_" + courseName)).isChecked()) {
                        descriptionTextSub.setEnabled(false);
                        descriptionTextSub.setSelectable(false);
                    } else {
                        descriptionTextSub.setEnabled(true);
                        descriptionTextSub.setSelectable(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error Loading Data", Toast.LENGTH_SHORT).show();
            }
        });
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

        for (String language : languages) {
            if (key.equals("checkbox_preference_" + language)) {
                boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_" + language, true);
                if (isEnable) {
                    dbh.addLanguageToUser(currentuserUid, language);
                } else {
                    dbh.removeLanguageFromUser(currentuserUid, language);
                }
            }
        }

        for (Course course : courses) {
            String courseName = course.getId();
            EditTextPreference descriptionPreference = (EditTextPreference) getPreferenceScreen().findPreference(
                    "edit_text_preference_" + courseName);

            if (key.equals("checkbox_preference_" + courseName)) {
                boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_" + courseName, true);
                if (isEnable) {
                    dbh.addTeacherToCourse(currentuserUid, courseName);
                    descriptionPreference.setEnabled(true);
                    descriptionPreference.setSelectable(true);
                } else {
                    dbh.removeTeacherFromCourse(currentuserUid, courseName);
                    descriptionPreference.setEnabled(false);
                    descriptionPreference.setSelectable(false);
                }
            }

            if (key.equals("edit_text_preference_" + courseName)) {
                if (!(descriptionPreference.getText().equals("") ||
                        descriptionPreference.getText().equals("Enter your description."))) {
                    descriptionPreference.setTitle(descriptionPreference.getText());
                    dbh.addSubjectDescription(descriptionPreference.getText(), currentuserUid, courseName);
                } else {
                    descriptionPreference.setTitle("Enter your description.");
                    descriptionPreference.setText("Enter your description.");
                    dbh.addSubjectDescription(descriptionPreference.getText(), currentuserUid, courseName);
                }
            }
        }
    }
}

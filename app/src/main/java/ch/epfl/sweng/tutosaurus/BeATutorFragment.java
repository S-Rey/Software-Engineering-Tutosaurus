package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;

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
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setDividerHeight(0);

        courses = FullCourseList.getInstance().getListOfCourses();
        for (Course course: courses) {

            String courseName = course.getId();
            EditTextPreference descriptionPreference = (EditTextPreference) getPreferenceManager().findPreference(
                    "edit_text_preference_" + courseName);

            if (descriptionPreference.getText().equals("")) {
                descriptionPreference.setTitle("Enter your description.");
                descriptionPreference.setText("Enter your description.");
            } else {
                descriptionPreference.setTitle(descriptionPreference.getText());
            }

            if (!((CheckBoxPreference) findPreference("checkbox_preference_" + courseName)).isChecked()) {
                descriptionPreference.setEnabled(false);
                descriptionPreference.setSelectable(false);
            }
        }
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
                    descriptionPreference.setEnabled(false);
                    dbh.removeTeacherFromCourse(currentuserUid, courseName);
                    descriptionPreference.setSelectable(false);
                }
            }

            if (key.equals("edit_text_preference_" + courseName)) {
                if (!(descriptionPreference.getText().equals("") || descriptionPreference.getText().equals("Enter your description."))) {
                    descriptionPreference.setTitle(descriptionPreference.getText());
                    dbh.addSubjectDescription(descriptionPreference.getText(), currentuserUid, courseName);
                } else {
                    descriptionPreference.setTitle("Enter your description.");
                    descriptionPreference.setText("Enter your description.");
                }
            }
        }
    }

}

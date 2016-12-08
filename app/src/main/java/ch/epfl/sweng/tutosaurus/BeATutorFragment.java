package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Course;
import ch.epfl.sweng.tutosaurus.model.FullCourseList;

import static java.util.Arrays.*;

public class BeATutorFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DatabaseHelper dbh = DatabaseHelper.getInstance();
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<Course> courses;
    private List<String> languages = asList("english", "french", "german", "italian", "chinese", "russian");


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Be A Tutor");
        addPreferencesFromResource(R.xml.be_a_tutor_preferences_layout);

        // remove dividers
        View rootView = getView();
        ListView list;
        if (rootView != null) {
            list = (ListView) rootView.findViewById(android.R.id.list);
            list.setDividerHeight(0);
        }

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
                    dbh.addLanguageToUser(currentUser, language);
                } else {
                    dbh.removeLanguageFromUser(currentUser, language);
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
                    dbh.addTeacherToCourse(currentUser, courseName);
                    descriptionPreference.setEnabled(true);
                    descriptionPreference.setSelectable(true);
                } else {
                    descriptionPreference.setEnabled(false);
                    dbh.removeTeacherFromCourse(currentUser, courseName);
                    descriptionPreference.setSelectable(false);
                }
            }

            if (key.equals("edit_text_preference_" + courseName)) {
                if (!(descriptionPreference.getText().equals("") || descriptionPreference.getText().equals("Enter your description."))) {
                    descriptionPreference.setTitle(descriptionPreference.getText());
                    dbh.addSubjectDescription(descriptionPreference.getText(), currentUser, courseName);
                } else {
                    descriptionPreference.setTitle("Enter your description.");
                    descriptionPreference.setText("Enter your description.");
                }
            }
        }
    }

}

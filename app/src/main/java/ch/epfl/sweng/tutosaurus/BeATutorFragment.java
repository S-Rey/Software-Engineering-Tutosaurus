package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

        EditTextPreference ed_mathematics = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_mathematics");
        if (!((CheckBoxPreference) findPreference("checkbox_preference_mathematics")).isChecked()) {
            getPreferenceScreen().removePreference(ed_mathematics);
        } else {
            if (ed_mathematics.getText().equals("")) {
                ed_mathematics.setTitle("Enter Your Description.");
            } else {
                ed_mathematics.setTitle(ed_mathematics.getText());
            }
        }

        EditTextPreference ed_physics = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_physics");
        if (!((CheckBoxPreference) findPreference("checkbox_preference_physics")).isChecked()) {
            getPreferenceScreen().removePreference(ed_physics);
        } else {
            if (ed_physics.getText().equals("")) {
                ed_physics.setTitle("Enter Your Description.");
            } else {
                ed_physics.setTitle(ed_physics.getText());
            }
        }

        EditTextPreference ed_chemistry = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_chemistry");
        if (!((CheckBoxPreference) findPreference("checkbox_preference_chemistry")).isChecked()) {
            getPreferenceScreen().removePreference(ed_chemistry);
        } else {
            if (ed_chemistry.getText().equals("")) {
                ed_chemistry.setTitle("Enter Your Description.");
            } else {
                ed_chemistry.setTitle(ed_chemistry.getText());
            }
        }

        EditTextPreference ed_computer_science = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_computer_science");
        if (!((CheckBoxPreference) findPreference("checkbox_preference_computer_science")).isChecked()) {
            getPreferenceScreen().removePreference(ed_computer_science);
        } else {
            if (ed_computer_science.getText().equals("")) {
                ed_computer_science.setTitle("Enter Your Description.");
            } else {
                ed_computer_science.setTitle(ed_computer_science.getText());
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
        switch (key) {
            case "checkbox_preference_english":
                break;
            case "checkbox_preference_french":
                break;
            case "checkbox_preference_german":
                break;
            case "checkbox_preference_italian":
                break;
            case "checkbox_preference_chinese":
                break;
            case "checkbox_preference_russian":
                break;
            default:
                break;
        }

        if (key.equals("checkbox_preference_mathematics")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_mathematics", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Maths");
                EditTextPreference ed = new EditTextPreference(getActivity());
                ed.setKey("edit_text_preference_mathematics");
                ed.setDefaultValue("Enter your description.");
                ed.setOrder(1);
                EditText et = ed.getEditText();
                et.setSelectAllOnFocus(true);
                et.setSingleLine(true);
                getPreferenceScreen().addPreference(ed);
                if (!ed.getText().equals("")) {
                    ed.setTitle(ed.getText());
                } else {
                    ed.setTitle("Enter Your Description.");
                }
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Maths");
                getPreferenceScreen().removePreference(getPreferenceManager().findPreference("edit_text_preference_mathematics"));
            }
        }
        if (key.equals("edit_text_preference_mathematics")) {
            EditTextPreference ed_mathematics_edit = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_mathematics");
            if (!ed_mathematics_edit.getText().equals("")) {
                ed_mathematics_edit.setTitle(ed_mathematics_edit.getText());
                dbh.addSubjectDescription(ed_mathematics_edit.getText().toString(), currentUser, "Maths");

            } else {
                ed_mathematics_edit.setTitle("Enter Your Description");
            }
        }

        if (key.equals("checkbox_preference_physics")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_physics", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Physics");
                EditTextPreference ed = new EditTextPreference(getActivity());
                ed.setKey("edit_text_preference_physics");
                ed.setDefaultValue("Enter your description.");
                ed.setOrder(4);
                EditText et = ed.getEditText();
                et.setSelectAllOnFocus(true);
                et.setSingleLine(true);
                getPreferenceScreen().addPreference(ed);
                if (!ed.getText().equals("")) {
                    ed.setTitle(ed.getText());
                } else {
                    ed.setTitle("Enter Your Description.");
                }
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Physics");
                getPreferenceScreen().removePreference(getPreferenceManager().findPreference("edit_text_preference_physics"));
            }
        }
        if (key.equals("edit_text_preference_physics")) {
            EditTextPreference ed_physics_edit = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_physics");
            if (!ed_physics_edit.getText().equals("")) {
                ed_physics_edit.setTitle(ed_physics_edit.getText());
                dbh.addSubjectDescription(ed_physics_edit.getText().toString(), currentUser, "Physics");

            } else {
                ed_physics_edit.setTitle("Enter Your Description");
            }
        }

        if (key.equals("checkbox_preference_chemistry")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_chemistry", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Chemistry");
                EditTextPreference ed = new EditTextPreference(getActivity());
                ed.setKey("edit_text_preference_chemistry");
                ed.setDefaultValue("Enter your description.");
                ed.setOrder(7);
                EditText et = ed.getEditText();
                et.setSelectAllOnFocus(true);
                et.setSingleLine(true);
                getPreferenceScreen().addPreference(ed);
                if (!ed.getText().equals("")) {
                    ed.setTitle(ed.getText());
                } else {
                    ed.setTitle("Enter Your Description.");
                }
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Chemistry");
                getPreferenceScreen().removePreference(getPreferenceManager().findPreference("edit_text_preference_chemistry"));
            }
        }
        if (key.equals("edit_text_preference_chemistry")) {
            EditTextPreference ed_chemistry_edit = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_chemistry");
            if (!ed_chemistry_edit.getText().equals("")) {
                ed_chemistry_edit.setTitle(ed_chemistry_edit.getText());
                dbh.addSubjectDescription(ed_chemistry_edit.getText().toString(), currentUser, "Chemistry");
            } else {
                ed_chemistry_edit.setTitle("Enter Your Description");
            }
        }

        if (key.equals("checkbox_preference_computer_science")) {
            boolean isEnable = sharedPreferences.getBoolean("checkbox_preference_computer_science", true);
            if (isEnable) {
                dbh.addTeacherToCourse(currentUser, "Computer");
                EditTextPreference ed = new EditTextPreference(getActivity());
                ed.setKey("edit_text_preference_computer_science");
                ed.setDefaultValue("Enter your description.");
                ed.setOrder(10);
                EditText et = ed.getEditText();
                et.setSelectAllOnFocus(true);
                et.setSingleLine(true);
                getPreferenceScreen().addPreference(ed);
                if (!ed.getText().equals("")) {
                    ed.setTitle(ed.getText());
                } else {
                    ed.setTitle("Enter Your Description.");
                }
            } else {
                dbh.removeTeacherFromCourse(currentUser, "Computer");
                getPreferenceScreen().removePreference(getPreferenceManager().findPreference("edit_text_preference_computer_science"));
            }
        }
        if (key.equals("edit_text_preference_computer_science")) {
            EditTextPreference ed_computer_science_edit = (EditTextPreference) getPreferenceManager().findPreference("edit_text_preference_computer_science");
            if (!ed_computer_science_edit.getText().equals("")) {
                ed_computer_science_edit.setTitle(ed_computer_science_edit.getText());
                dbh.addSubjectDescription(ed_computer_science_edit.getText().toString(), currentUser, "Computer");

            } else {
                ed_computer_science_edit.setTitle("Enter Your Description");
            }
        }
    }
}

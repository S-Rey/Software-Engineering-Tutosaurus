package ch.epfl.sweng.tutosaurus;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");
        addPreferencesFromResource(R.xml.settings_preferences_layout);

        // remove dividers
        View rootView = getView();
        ListView list = (ListView) rootView.findViewById(android.R.id.list);
        list.setDivider(new ColorDrawable(this.getResources().getColor(R.color.colorPrimaryDark)));
        list.setDividerHeight(2);
        list.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        list.setVerticalScrollBarEnabled(false);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("checkbox_preference_notification")) {
            //Do Something
        }
        if (key.equals("checkbox_preference_calendar")) {
            //Do Something
        }
        if (key.equals("checkbox_preference_location")) {
            //Do Something
        }
    }

//-----------------------
//UNUSED METHODS BELOW
//-----------------------
//THE FOLLOWING METHODS ARE FOR CLICKING AUTOMATICALLY ON A PREFERENCE IN THE LIST DEPENDING ON THE KEY
//RESERVED FOR TESTS !!!!!!
    public PreferenceScreen findPreferenceScreenForPreference(String key, PreferenceScreen screen ) {
        if( screen == null ) {
            screen = getPreferenceScreen();
        }

        PreferenceScreen result = null;

        android.widget.Adapter ada = screen.getRootAdapter();
        for( int i = 0; i < ada.getCount(); i++ ) {
            String prefKey = ((Preference)ada.getItem(i)).getKey();
            if( prefKey != null && prefKey.equals( key ) ) {
                return screen;
            }
            if( ada.getItem(i).getClass().equals(android.preference.PreferenceScreen.class) ) {
                result = findPreferenceScreenForPreference( key, (PreferenceScreen) ada.getItem(i) );
                if( result != null ) {
                    return result;
                }
            }
        }

        return null;
    }

    public void openPreference( String key ) {
        PreferenceScreen screen = findPreferenceScreenForPreference( key, null );
        if( screen != null ) {
            screen.onItemClick(null, null, findPreference(key).getOrder(), 0);
        }
    }

}

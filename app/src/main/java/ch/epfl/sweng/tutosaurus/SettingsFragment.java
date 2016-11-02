package ch.epfl.sweng.tutosaurus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Settings");
        addPreferencesFromResource(R.xml.settings_preferences_layout);

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

        //SharedPreferences calendar = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        //syncCalendar = calendar.getBoolean("checkbox_preference_calendar", true);
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
        if (key.equals("checkbox_preference_calendar")) {
            synchronizeCalendar();
        }

        if (key.equals("checkbox_preference_notification")) {
            //Do Something
        }

        if (key.equals("checkbox_preference_location")) {
            //Do Something
        }
    }

    private void synchronizeCalendar() {
        DatabaseReference ref = dbh.getReference();
        ref.child("meetingsPerUser/456892").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                CheckBoxPreference calendar = (CheckBoxPreference) findPreference("checkbox_preference_calendar");
                if (calendar.isChecked()) {
                    for (DataSnapshot meetingSnapshot : snapshot.getChildren()) {
                        Meeting meeting = meetingSnapshot.getValue(Meeting.class);
                        long startMillis = 0;
                        long endMillis = 0;
                        Calendar beginTime = Calendar.getInstance();
                        beginTime.setTime(meeting.getDate());
                        Toast.makeText(getActivity().getBaseContext(), meeting.getDate().toString(), Toast.LENGTH_LONG).show();
                        startMillis = beginTime.getTimeInMillis();
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTime(meeting.getDate());
                        endTime.add(Calendar.HOUR, meeting.getDuration());
                        endMillis = endTime.getTimeInMillis();

                        long calID;
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        Uri uri = CalendarContract.Calendars.CONTENT_URI;
                        Cursor cursorCalendarID = contentResolver.query(uri, EVENT_PROJECTION, null, null, null);
                        if (cursorCalendarID != null) {
                            cursorCalendarID.moveToFirst();
                        }
                        calID = cursorCalendarID.getLong(PROJECTION_ID_INDEX);

                        ContentValues values = new ContentValues();
                        values.put(CalendarContract.Events.DTSTART, startMillis);
                        values.put(CalendarContract.Events.DTEND, endMillis);
                        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Switzerland/Lausanne");
                        values.put(CalendarContract.Events.TITLE, meeting.getNameLocation());
                        values.put(CalendarContract.Events.DESCRIPTION, meeting.getDescription());
                        values.put(CalendarContract.Events.EVENT_LOCATION, meeting.getNameLocation());
                        values.put(CalendarContract.Events.CALENDAR_ID, calID);
                        //values.put(CalendarContract.Events._ID, meeting.getId());
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        Uri newEvent = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
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

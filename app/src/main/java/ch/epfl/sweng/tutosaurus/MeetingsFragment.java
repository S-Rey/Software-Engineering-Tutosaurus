package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import ch.epfl.sweng.tutosaurus.adapter.MeetingAdapter;
import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;

public class MeetingsFragment extends Fragment {

    View myView;
    private MeetingAdapter adapter;
    private String currentUser = "123456";
    DatabaseHelper dbh = DatabaseHelper.getInstance();
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.meetings_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Meetings");

        ListView meetingList = (ListView) myView.findViewById(R.id.meetingList);
        Query ref = dbh.getMeetingsRefForUser(currentUser);
        ref = ref.orderByChild("date");
        adapter = new MeetingAdapter(getActivity(), Meeting.class, R.layout.listview_meetings_row, ref);
        meetingList.setAdapter(adapter);



        return myView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }

//    CheckBoxPreference calendar = (CheckBoxPreference) findPreference("checkbox_preference_calendar");
//    if (calendar.isChecked()) {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DatabaseReference ref = dbh.getReference();
        ref.child("meetingsPerUser/" + currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SharedPreferences calendar = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                boolean syncCalendar = calendar.getBoolean("checkbox_preference_calendar", true);
                if (syncCalendar) {
                    for (DataSnapshot meetingSnapshot : snapshot.getChildren()) {
                        Meeting meeting = meetingSnapshot.getValue(Meeting.class);
                        long startMillis = 0;
                        long endMillis = 0;
                        Calendar beginTime = Calendar.getInstance();
                        beginTime.setTime(meeting.getDate());
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
                        values.put(CalendarContract.Events.TITLE, meeting.getCourse().getName());
                        values.put(CalendarContract.Events.DESCRIPTION, meeting.getDescription());
                        values.put(CalendarContract.Events.EVENT_LOCATION, meeting.getNameLocation());
                        values.put(CalendarContract.Events.CALENDAR_ID, calID);
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

}

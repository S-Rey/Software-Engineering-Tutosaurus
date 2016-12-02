package ch.epfl.sweng.tutosaurus;

import android.Manifest;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

    private static final int MY_PERMISSIONS_REQUEST_CALENDAR = 100;
    View myView;
    private MeetingAdapter adapter;
    private String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        long lastWeekInMillis = System.currentTimeMillis() + 59958140730000L - (86400 * 7 * 1000);
        ref = ref.orderByChild("date/time").startAt(lastWeekInMillis);
        adapter = new MeetingAdapter(getActivity(), Meeting.class, R.layout.listview_meetings_row, ref);
        meetingList.setAdapter(adapter);

        return myView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }


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

                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    Manifest.permission.WRITE_CALENDAR)) {
                                // Show an explanation to the user *asynchronously* -- don't block
                                // this thread waiting for the user's response! After the user
                                // sees the explanation, try again to request the permission.
                            } else {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.WRITE_CALENDAR},
                                        MY_PERMISSIONS_REQUEST_CALENDAR);
                            }

                        }
                        //Log.d("Meetings Fragment", "ciao");
                        Calendar beginTime = Calendar.getInstance();
                        Calendar endTime = Calendar.getInstance();
                        if (meeting.getDate() != null) {
                            beginTime.setTime(meeting.getDate());
                            startMillis = beginTime.getTimeInMillis();
                            endTime.setTime(meeting.getDate());
                            //endTime.add(Calendar.HOUR, 2); //TODO: fix duration and create a calendar
                            endMillis = endTime.getTimeInMillis();
                        }

                        long calID;
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        Uri uri = CalendarContract.Calendars.CONTENT_URI;
                        Cursor cursorCalendarID = contentResolver.query(uri, EVENT_PROJECTION, null, null, null);
                        while (cursorCalendarID.moveToNext()) {
                            calID = cursorCalendarID.getLong(PROJECTION_ID_INDEX);
                            ContentValues values = new ContentValues();
                            values.put(CalendarContract.Events.DTSTART, startMillis);
                            values.put(CalendarContract.Events.DTEND, endMillis);
                            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Switzerland/Lausanne");
                            if (meeting.getCourse() != null) {
                                values.put(CalendarContract.Events.TITLE, meeting.getCourse().getName());
                            }
                            if (meeting.getDescription() != null) {
                                values.put(CalendarContract.Events.DESCRIPTION, meeting.getDescription());
                            }
                            if (meeting.getNameLocation() != null) {
                                values.put(CalendarContract.Events.EVENT_LOCATION, meeting.getNameLocation());
                            }
                            values.put(CalendarContract.Events.CALENDAR_ID, calID);
                            Uri newEvent = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values);
                        }
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

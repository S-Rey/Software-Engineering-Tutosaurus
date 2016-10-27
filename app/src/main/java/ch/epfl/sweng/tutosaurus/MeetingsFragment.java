package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ch.epfl.sweng.tutosaurus.model.Meeting;

public class MeetingsFragment extends Fragment {

    View myView;
    private ArrayList<Meeting> meetings = new ArrayList<>();

    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.meetings_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Meetings");

        Button updateList = (Button) myView.findViewById(R.id.updateList);
        updateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fillListView(newMeetings);
                fillListViewTest();
            }
        });

        Button syncCalendar = (Button) myView.findViewById(R.id.syncCalendar);
        syncCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Meeting m : meetings) {
                    long startMillis = 0;
                    long endMillis = 0;
                    Calendar beginTime = Calendar.getInstance();
                    beginTime.setTime(m.getDate());
                    startMillis = beginTime.getTimeInMillis();
                    Calendar endTime = Calendar.getInstance();
                    endTime.setTime(m.getDate());
                    endTime.add(Calendar.HOUR, m.getDuration());
                    endMillis = endTime.getTimeInMillis();

                    long calID;
                    ContentResolver cr = getActivity().getContentResolver();
                    Uri uri = CalendarContract.Calendars.CONTENT_URI;
                    Cursor cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
                    cur.moveToFirst();
                    calID = cur.getLong(PROJECTION_ID_INDEX);
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.DTSTART, startMillis);
                    values.put(CalendarContract.Events.DTEND, endMillis);
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, "Switzerland/Lausanne");
                    values.put(CalendarContract.Events.TITLE, "subject");
                    values.put(CalendarContract.Events.DESCRIPTION, "teacher or student");
                    values.put(CalendarContract.Events.EVENT_LOCATION, m.getLocation());
                    values.put(CalendarContract.Events.CALENDAR_ID, calID);
                    if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Uri newEvent = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                }

            }

        });

        return myView;
    }

    public boolean isEventInCal(Context context, String cal_meeting_id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[] { "_id" }, " _id = ? ",
                new String[] { cal_meeting_id }, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    private ArrayList<Meeting> createMeetings() {
        ArrayList<Meeting> meetList = new ArrayList<>();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String l = "Lausanne CM1 1";
        try {
            for (int i = 0; i < 2; i++) {
                Date date = dateformat.parse("1" + "9" + "/11/2016");
                Meeting newMeeting = new Meeting(i, date, 2);
                newMeeting.setLocation(l);
                meetList.add(newMeeting);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return meetList;
    }

    private void fillListView(ArrayList<Meeting> meetingNames){
        MeetingAdapter arrayAdapter = new MeetingAdapter(
                getActivity(),
                R.layout.listview_meetings_row,
                meetingNames);
        ListView meetingList = (ListView) getView().findViewById(R.id.meetingList);
        meetingList.setAdapter(arrayAdapter);

        Meeting meetingToAdd;
        for (Meeting meeting : meetingNames) {
            if(!meetings.contains(meeting)) {
                meetingToAdd = new Meeting(meeting.getId(), meeting.getDate(), meeting.getDuration());
                meetingToAdd.setLocation(meeting.getLocation());
                meetings.add(meetingToAdd);
            }
        }
    }

    private void fillListViewTest() {
        ArrayList<Meeting> meetingNames = createMeetings();
        MeetingAdapter arrayAdapter = new MeetingAdapter(
                getActivity(),
                R.layout.listview_meetings_row,
                meetingNames);
        ListView meetingList = (ListView) getView().findViewById(R.id.meetingList);
        meetingList.setAdapter(arrayAdapter);

        Meeting meetingToAdd;
        for (Meeting meeting : meetingNames) {
            if (!meetings.contains(meeting)) {
                meetingToAdd = new Meeting(meeting.getId(), meeting.getDate(), meeting.getDuration());
                meetingToAdd.setLocation(meeting.getLocation());
                meetings.add(meetingToAdd);
            }
        }
    }


}

package ch.epfl.sweng.tutosaurus;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
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

import ch.epfl.sweng.tutosaurus.findTutor.Tutor;
import ch.epfl.sweng.tutosaurus.model.Meeting;
import ch.epfl.sweng.tutosaurus.model.User;

public class MeetingsFragment extends Fragment {

    View myView;
    View myListView;
    private ArrayList<Meeting> meetings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.meetings_layout, container, false);
        ((HomeScreenActivity) getActivity()).setActionBarTitle("Meetings");

        Button updateList = (Button) myView.findViewById(R.id.updateList);
        updateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Meeting> meetings_created = createMeetings();
                fillListView(meetings_created);
            }
        });

        Button syncCalendar = (Button) myView.findViewById(R.id.syncCalendar);
        syncCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar beginTime = java.util.Calendar.getInstance();
                //beginTime.setTime(meeting.getDate());
                beginTime.set(2016, 10, 19, 7, 30);
                java.util.Calendar endTime = java.util.Calendar.getInstance();
                //endTime.setTime(meeting.getDate());
                //endTime.add(Calendar.HOUR, meeting.getDuration());
                endTime.set(2016, 10, 19, 8, 30);
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        //.putExtra(CalendarContract.Events.TITLE, meeting.getId())
                        .putExtra(CalendarContract.Events.DESCRIPTION, "Partial differential equations")
                        //.putExtra(CalendarContract.Events.EVENT_LOCATION, meeting.getLocation())
                        .putExtra(Intent.EXTRA_EMAIL, "santo.gioia@epfl.ch")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                startActivity(intent);
            }
        });

        return myView;
    }

    private ArrayList<Meeting> createMeetings() {
        ArrayList<Meeting> meetList = new ArrayList<>();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Location l = new Location("network");
        try {
            for (int i = 0; i < 10; i++) {
                Date date = dateformat.parse("17/07/1989");
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
            meetingToAdd = new Meeting(meeting.getId(), meeting.getDate(), meeting.getDuration());
            meetingToAdd.setLocation(meeting.getLocation());
            meetings.add(meetingToAdd);
        }
    }

}

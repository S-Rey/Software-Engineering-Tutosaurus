package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.epfl.sweng.tutosaurus.model.Meeting;

/**
 * Created by santo on 25/10/16.
 */

public class MeetingAdapter extends ArrayAdapter<Meeting> {
    Context context;
    int layoutResourceId;
    ArrayList<Meeting> meetingList = null;

    public MeetingAdapter(Context context, int layoutResourceId, ArrayList<Meeting> meetingList){
        super(context, layoutResourceId, meetingList);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.meetingList = meetingList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        MeetingHolder holder = null;
        if(row == null){
            LayoutInflater inflater=((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MeetingHolder();
            holder.location = (TextView) row.findViewById(R.id.location);
            holder.date = (TextView) row.findViewById(R.id.date);
            holder.addCalendar = (ImageButton) row.findViewById(R.id.add_calendar);


            row.setTag(holder);
        }
        else{
            holder=(MeetingHolder) row.getTag();
        }
        Meeting meeting = meetingList.get(position);
        if (meeting.getLocation() != null)
            holder.location.setText(meeting.getLocation().toString());
        if (meeting.getDate() != null)
            holder.date.setText(meeting.getDate().toString());

        return row;
    }

    public class MeetingHolder {
        TextView location;
        TextView date;
        ImageButton addCalendar;
    }

}

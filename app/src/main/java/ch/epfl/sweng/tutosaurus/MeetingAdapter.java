package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.tutosaurus.model.Meeting;

import static android.content.ContentValues.TAG;

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
        MeetingHolder holder;
        if(row == null){
            LayoutInflater inflater=((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new MeetingHolder();
            holder.location = (TextView) row.findViewById(R.id.location);
            holder.date = (TextView) row.findViewById(R.id.date);
            row.setTag(holder);
        }
        else{
            holder = (MeetingHolder) row.getTag();
        }
        Meeting meeting = meetingList.get(position);
        if (meeting.getLocation() != null) {
            holder.location.setText(meeting.getLocation());
        }
        if (meeting.getDate() != null)
            holder.date.setText(meeting.getDate().toString());

        return row;
    }

    public class MeetingHolder {
        TextView location;
        TextView date;
    }

}

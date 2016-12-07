package ch.epfl.sweng.tutosaurus;

import android.app.Dialog;
import android.app.DialogFragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    private int meetingHour;
    private int meetingMinutes;
    //TODO: setArgument to change the textview (date-time picker)

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    public void onTimeSet(TimePicker view, int hour, int minute) {
        meetingHour = hour;
        meetingMinutes = minute;
    }


    public String getTime() {
        return meetingHour + ":" + meetingMinutes;
    }


    public int getMeetingHour() {
        return meetingHour;
    }


    public int getMeetingMinutes() {
        return meetingMinutes;
    }
}
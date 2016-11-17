package ch.epfl.sweng.tutosaurus;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by santo on 10/11/16.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    private int meetingHour;
    private int meetingMinutes;


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


    public int getMeetingHour() {
        return meetingHour;
    }

    public int getMeetingMinutes() {
        return meetingMinutes;
    }
}
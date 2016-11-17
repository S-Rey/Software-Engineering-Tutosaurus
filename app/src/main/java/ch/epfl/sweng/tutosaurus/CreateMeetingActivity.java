package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;

public class CreateMeetingActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    DatabaseHelper dbh = DatabaseHelper.getInstance();
    public String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TimePickerFragment timePicker = new TimePickerFragment();
    DatePickerFragment datePicker = new DatePickerFragment();

    Meeting meeting = new Meeting();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        Intent intent = getIntent();
        final String teacherId = intent.getStringExtra("TEACHER");
        meeting.addParticipant(teacherId);
        meeting.addParticipant(currentUser);

        final Button addMeeting = (Button) findViewById(R.id.addMeeting);
        addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText description = (EditText) findViewById(R.id.description);
                meeting.addDescription(description.getText().toString());

                Date dateMeeting = new Date();
                dateMeeting.setMinutes(timePicker.getMeetingMinutes());
                dateMeeting.setHours(timePicker.getMeetingHour());
                dateMeeting.setYear(datePicker.getMeetingYear());
                dateMeeting.setMonth(datePicker.getMeetingMonth());
                dateMeeting.setDate(datePicker.getMeetingDay());

                meeting.setDate(dateMeeting);

                dbh.addMeeting(meeting);
            }
        });


    }


    public void showDateTimePickerDialog(View v) {
        timePicker.show(getFragmentManager(), "timePicker");
        datePicker.show(getFragmentManager(), "datePicker");
    }


    public void showLocationPickerDialog(View v) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                meeting.setNameLocation(place.getName().toString());
                meeting.setLatitudeLocation(place.getLatLng().latitude);
                meeting.setLongitudeLocation(place.getLatLng().longitude);
            }
        }
    }
}

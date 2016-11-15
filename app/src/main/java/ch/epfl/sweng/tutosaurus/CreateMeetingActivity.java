package ch.epfl.sweng.tutosaurus;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.Meeting;

public class CreateMeetingActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    DatabaseHelper dbh = DatabaseHelper.getInstance();
    public String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        Intent intent = getIntent();
        final String teacherId = intent.getStringExtra("TEACHER");

        final Button addMeeting = (Button) findViewById(R.id.addMeeting);
        addMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText description = (EditText) findViewById(R.id.description);
                addMeeting(teacherId, description.getText().toString());
            }
        });


    }


    private void addMeeting(String userId, String description) {
        Meeting meeting = new Meeting();
        meeting.addParticipant(currentUser);
        meeting.addParticipant(userId);
        meeting.addDescription(description);
        // meeting.setLatitudeLocation(sLocationMeeting.getLatitude());
        // meeting.setLongitudeLocation(sLocationMeeting.getLongitude());
        dbh.addMeeting(meeting);
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    public void showLocationPickerDialog(View v) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}

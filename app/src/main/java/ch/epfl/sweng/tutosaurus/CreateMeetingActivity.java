package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by santo on 09/11/16.
 */
public class CreateMeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_meeting);
        Intent intent = getIntent();
        //Bundle extras = intent.getExtras();
    }

}

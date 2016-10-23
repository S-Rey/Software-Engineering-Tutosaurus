package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        Intent intent = getIntent();

        String first_name = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_FIRST_NAME);
        String last_name = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_LAST_NAME);
        String email_address = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_EMAIL_ADDRESS);
        String sciper = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_SCIPER);

        TextView first_name_text = (TextView) findViewById(R.id.firstNameProvided);
        first_name_text.setText("First name : " + first_name);
        TextView last_name_text = (TextView) findViewById(R.id.lastNameProvided);
        last_name_text.setText("Last name : " + last_name);
        TextView email_address_text = (TextView) findViewById(R.id.emailAddressProvided);
        email_address_text.setText("Email address : " + email_address);
        TextView sciper_text = (TextView) findViewById(R.id.sciperProvided);
        sciper_text.setText("Sciper : " + sciper);
    }

    public void sendMessageForLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

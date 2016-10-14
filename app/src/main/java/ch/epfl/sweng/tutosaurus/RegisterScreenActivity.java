package ch.epfl.sweng.tutosaurus;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterScreenActivity extends AppCompatActivity {

    Button send_button;
    EditText first_name_text;
    EditText last_name_text;
    EditText email_address_text;
    EditText sciper_text;
    String first_name;
    String last_name;
    String email_address;
    String sciper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        send_button = (Button)findViewById(R.id.sendButton);
        first_name_text = (EditText)findViewById(R.id.firstNameEntry);
        last_name_text = (EditText)findViewById(R.id.lastNameEntry);
        email_address_text = (EditText)findViewById(R.id.emailAddressEntry);
        sciper_text = (EditText)findViewById(R.id.sciperEntry);

        send_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        first_name = first_name_text.getText().toString();
                        last_name = last_name_text.getText().toString();
                        email_address = email_address_text.getText().toString();
                        sciper = sciper_text.getText().toString();

                        sendMessageForAccess(view);
                    }
                }
        );

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void sendMessageForAccess(View view) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        startActivity(intent);
    }

}

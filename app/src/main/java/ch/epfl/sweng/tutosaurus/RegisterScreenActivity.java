package ch.epfl.sweng.tutosaurus;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import java.io.IOException;

import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RegisterScreenActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_FIRST_NAME = "com.example.myfirstapp.FIRSTNAME";
    public final static String EXTRA_MESSAGE_LAST_NAME = "com.example.myfirstapp.LASTNAME";
    public final static String EXTRA_MESSAGE_EMAIL_ADDRESS = "com.example.myfirstapp.EMAILADDRESS";
    public final static String EXTRA_MESSAGE_SCIPER = "com.example.myfirstapp.SCIPER";

    private static final String CLIENT_ID = "";
    private static final String CLIENT_KEY = "";
    private static final String REDIRECT_URI = "tutosaurus://login";

    public Dialog authDialog;
    public WebView authWebv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

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

        EditText first_name_text = (EditText)findViewById(R.id.firstNameEntry);
        EditText last_name_text = (EditText)findViewById(R.id.lastNameEntry);
        EditText email_address_text = (EditText)findViewById(R.id.emailAddressEntry);
        EditText sciper_text = (EditText)findViewById(R.id.sciperEntry);

        String first_name = first_name_text.getText().toString();
        String last_name = last_name_text.getText().toString();
        String email_address = email_address_text.getText().toString();
        String sciper = sciper_text.getText().toString();

        intent.putExtra(EXTRA_MESSAGE_FIRST_NAME, first_name);
        intent.putExtra(EXTRA_MESSAGE_LAST_NAME, last_name);
        intent.putExtra(EXTRA_MESSAGE_EMAIL_ADDRESS, email_address);
        intent.putExtra(EXTRA_MESSAGE_SCIPER, sciper);

        startActivity(intent);
    }

    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
    }


}

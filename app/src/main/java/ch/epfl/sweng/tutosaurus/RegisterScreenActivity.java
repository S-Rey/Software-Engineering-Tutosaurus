package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.IOException;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RegisterScreenActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_FIRST_NAME = "com.example.myfirstapp.FIRSTNAME";
    public final static String EXTRA_MESSAGE_LAST_NAME = "com.example.myfirstapp.LASTNAME";
    public final static String EXTRA_MESSAGE_EMAIL_ADDRESS = "com.example.myfirstapp.EMAILADDRESS";
    public final static String EXTRA_MESSAGE_SCIPER = "com.example.myfirstapp.SCIPER";

    /*Dialog authDialog;
    WebView authWebv;
    public String codeRequestUrl;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        /*try{
            OAuth2Config config = readConfig();
            codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        }catch(IOException e){

        }


        authDialog = new Dialog(this);
        //authDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        //authDialog.setContentView(R.layout.authentification_screen);
        authDialog.setCancelable(true);

        authWebv = new WebView(this);
        authWebv.getSettings().setJavaScriptEnabled(true);
        authWebv.clearCache(true);
        authWebv.loadUrl(codeRequestUrl);

        authWebv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

        authDialog.setContentView(authWebv);

        authDialog.show();*/
        showDialog();
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

    /*private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
    }*/

    void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        AuthFragment newFragment = new AuthFragment();
        newFragment.show(ft, "Dialog");
    }


}

package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import java.util.Map;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.AuthServer;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;
import ch.epfl.sweng.tutosaurus.Tequila.Profile;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class RegisterScreenActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE_FIRST_NAME = "com.example.myfirstapp.FIRSTNAME";
    public final static String EXTRA_MESSAGE_LAST_NAME = "com.example.myfirstapp.LASTNAME";
    public final static String EXTRA_MESSAGE_EMAIL_ADDRESS = "com.example.myfirstapp.EMAILADDRESS";
    public final static String EXTRA_MESSAGE_SCIPER = "com.example.myfirstapp.SCIPER";

    private static final String CLIENT_ID = "2e58be9551a5fd7286b718bd@epfl.ch";
    private static final String CLIENT_KEY = "97fb52cdc30384634c5eeb8cdc684baf";
    private static final String REDIRECT_URI = "tutosaurus://login";

    Dialog authDialog;
    WebView webViewOauth;

    private static OAuth2Config config;
    private static Map<String, String> tokens;
    private static Profile profile;
    String codeRequestUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        getConfig();
        codeRequestUrl = AuthClient.createCodeRequestUrl(config);

        authDialog = new Dialog(RegisterScreenActivity.this);
        authDialog.setContentView(R.layout.auth_screen);

        webViewOauth = (WebView) authDialog.findViewById(R.id.web_oauth);
        webViewOauth.getSettings().setJavaScriptEnabled(true);
        webViewOauth.clearCache(true);
        webViewOauth.loadUrl(codeRequestUrl);

        webViewOauth.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            Intent resultIntent = new Intent();

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=") && authComplete != true) {
                    authComplete = true;
                    authDialog.dismiss();
                    new ManageAccessToken().execute(url);
                }

            }
        });

        authDialog.show();
        authDialog.setCancelable(true);
        authDialog.setTitle("Tequila authentification");


        /**authDialog = new Dialog(this);
         //authDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
         //authDialog.setContentView(R.layout.authentification_screen);
         authDialog.setCancelable(true);

         authWebv = new WebView(this);
         authWebv.getSettings().setJavaScriptEnabled(true);
         authWebv.clearCache(true);
         authWebv.loadUrl(codeRequestUrl);

         authWebv.setWebViewClient(new WebViewClient() {
        @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);

        return true;
        }
        });

         authDialog.setContentView(authWebv);

         authDialog.show();*/
        //showDialog();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void sendMessageForAccess(View view) {
        Intent intent = new Intent(this, ConfirmationActivity.class);

        /**EditText first_name_text = (EditText) findViewById(R.id.firstNameEntry);
        EditText last_name_text = (EditText) findViewById(R.id.lastNameEntry);
        EditText email_address_text = (EditText) findViewById(R.id.emailAddressEntry);
        EditText sciper_text = (EditText) findViewById(R.id.sciperEntry);

        String first_name = first_name_text.getText().toString();
        String last_name = last_name_text.getText().toString();
        String email_address = email_address_text.getText().toString();
        String sciper = sciper_text.getText().toString();*/

        intent.putExtra(EXTRA_MESSAGE_FIRST_NAME, profile.firstNames);
        intent.putExtra(EXTRA_MESSAGE_LAST_NAME, profile.lastNames);
        intent.putExtra(EXTRA_MESSAGE_EMAIL_ADDRESS, profile.email);
        intent.putExtra(EXTRA_MESSAGE_SCIPER, profile.sciper);

        startActivity(intent);
    }

    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
    }

    private static void getConfig() {
        try {
            config = readConfig();
        } catch (IOException e) {

        }
    }

    private static void getAccessToken(OAuth2Config config, String code) {
        try {
            tokens = AuthServer.fetchTokens(config, code);
        } catch (IOException e) {

        }
    }

    private static void getProfile() {
        try {
            profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
        } catch (IOException e) {

        }
    }

    private class ManageAccessToken extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        String Code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterScreenActivity.this);
            pDialog.setMessage("Gathering Info ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String code = AuthClient.extractCode(url[0]);
            getAccessToken(config, code);
            getProfile();
            return "OK";
        }

        @Override
        protected void onPostExecute(String json) {
            pDialog.dismiss();
        }
    }

    /*void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        AuthFragment newFragment = new AuthFragment();
        newFragment.show(ft, "Dialog");
    }*/


}

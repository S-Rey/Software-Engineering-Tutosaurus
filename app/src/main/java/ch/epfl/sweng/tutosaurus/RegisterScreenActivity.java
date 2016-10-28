package ch.epfl.sweng.tutosaurus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.AuthServer;
import ch.epfl.sweng.tutosaurus.Tequila.MyAppVariables;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;
import ch.epfl.sweng.tutosaurus.Tequila.Profile;


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
    Button sendButton;

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

        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                // a callback which is executed when the cookies have been removed
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                }
            });
        }
        else {
            cookieManager.removeAllCookie();
        }

        webViewOauth.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=") && authComplete != true) {
                    MyAppVariables.setRegistered(true);
                    authComplete = true;
                    authDialog.dismiss();
                    new ManageAccessToken().execute(url);
                }

            }
        });

        authDialog.show();
        authDialog.setCancelable(true);
        authDialog.setTitle("Tequila authentification");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void sendMessageForAccess(View view) {
        if(MyAppVariables.getRegistered() == true){
            Intent intent = new Intent(this, ConfirmationActivity.class);

            intent.putExtra(EXTRA_MESSAGE_FIRST_NAME, profile.firstNames);
            intent.putExtra(EXTRA_MESSAGE_LAST_NAME, profile.lastNames);
            intent.putExtra(EXTRA_MESSAGE_EMAIL_ADDRESS, profile.email);
            intent.putExtra(EXTRA_MESSAGE_SCIPER, profile.sciper);

            startActivity(intent);
        }
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

    /**private void setScreenInfo(){
        if(profile != null){
            EditText first_name_text = (EditText) findViewById(R.id.firstNameEntry);
            EditText last_name_text = (EditText) findViewById(R.id.lastNameEntry);
            EditText email_address_text = (EditText) findViewById(R.id.emailAddressEntry);
            EditText sciper_text = (EditText) findViewById(R.id.sciperEntry);

            first_name_text.setText(profile.firstNames);
            last_name_text.setText(profile.lastNames);
            email_address_text.setText(profile.email);
            sciper_text.setText(profile.sciper);
        }
    }*/


}

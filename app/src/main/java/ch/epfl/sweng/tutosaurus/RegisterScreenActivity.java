package ch.epfl.sweng.tutosaurus;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.AuthServer;
import ch.epfl.sweng.tutosaurus.Tequila.MyAppVariables;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;
import ch.epfl.sweng.tutosaurus.Tequila.Profile;


/**
 * 1. Client creates request url: AuthClient.createCodeRequestUrl(config). The config is obtained from client id, client key and redirect uri. <br>
 * 2. Client accesses request url <br>
 * 3. Client enters username and password; gets 'code' in return. If user already entered details, the webview uses a cookie. <br>
 * 4. Client uses 'code' to request access token: AuthServer.fetchTokens(config, code). config is the same as in step 1, it contains client id and client secret.
 *    At this point, the user is logged in.<br>
 * 5. Client requests profile info using the token obtained in step 4. <br>
 */
public class RegisterScreenActivity extends AppCompatActivity {

    public final static String TAG = "RegisterScreenActivity";

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

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String gaspar;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        Intent intent = getIntent();
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void sendMessageForAccess(View view) {
        if(MyAppVariables.getRegistered() == true){
            mAuth.createUserWithEmailAndPassword(profile.email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());
                    if(!task.isSuccessful()) {
                        Toast.makeText(RegisterScreenActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(RegisterScreenActivity.this, ConfirmationActivity.class);

                    intent.putExtra(EXTRA_MESSAGE_FIRST_NAME, profile.firstNames);
                    intent.putExtra(EXTRA_MESSAGE_LAST_NAME, profile.lastNames);
                    intent.putExtra(EXTRA_MESSAGE_EMAIL_ADDRESS, profile.email);
                    intent.putExtra(EXTRA_MESSAGE_SCIPER, profile.sciper);

                    startActivity(intent);
                }
            });
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
            for(String s : url) {
                Log.d(TAG, "ManageAccessToken.url: " + s);
            }
            String code = AuthClient.extractCode(url[0]);
            // This code is given by tutosaurus://login?code='code'
            getAccessToken(config, code);
            getProfile();
            return "OK";
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d(TAG, "ManageAccessToken.json: " + json);
            for (String token : tokens.keySet()) {
                Log.d(TAG, "token name: " + token + " token value: " + tokens.get(token));
            }
            pDialog.dismiss();
        }
    }

    private void startAuthDialog(){
        getConfig();
        codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        Log.d(TAG, "codeRequestUrl: " + codeRequestUrl);

        authDialog = new Dialog(RegisterScreenActivity.this);
        authDialog.setContentView(R.layout.auth_screen);

        webViewOauth = (WebView) authDialog.findViewById(R.id.web_oauth);
        webViewOauth.getSettings().setJavaScriptEnabled(true);
        webViewOauth.getSettings().setDomStorageEnabled(true);
        webViewOauth.clearCache(true);
        webViewOauth.loadUrl(codeRequestUrl);

        CookieManager.getInstance().removeAllCookie();

        /**CookieManager cookieManager = CookieManager.getInstance();
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
         }*/

        webViewOauth.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "onPageStartedUrl: " + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d(TAG, "onPageFinishedUrl: "+ url);
                super.onPageFinished(view, url);

                String js_g = "javascript:document.getElementById('username').value = '" + gaspar + "';";
                String js_pw = "javascript:document.getElementById('password').value = '" + password + "';";
                Log.d(TAG, "js: " + js_g + js_pw);

                if (url.contains("requestkey")) {
                    Log.d(TAG, "TRIGGERED");
                    view.evaluateJavascript(js_g + js_pw, null);
                }

                else if (url.contains("?code=") && !authComplete) {
                    MyAppVariables.setRegistered(true);
                    authComplete = true;
                    authDialog.dismiss();
                    new ManageAccessToken().execute(url);
                }
                String cookies = CookieManager.getInstance().getCookie("tequila.epfl.ch");
                Log.d(TAG, "All the cookies in a string: " + cookies);

            }
        });

        authDialog.show();
        authDialog.setCancelable(true);
        authDialog.setTitle("Tequila authentification");
    }

    public void logIn(View view) {
        String gaspar = ((EditText)findViewById(R.id.register_gaspar_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.register_password)).getText().toString();

        this.gaspar = gaspar;
        this.password = password;

        startAuthDialog();
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

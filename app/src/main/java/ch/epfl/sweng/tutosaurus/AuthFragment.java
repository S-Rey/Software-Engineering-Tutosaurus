package ch.epfl.sweng.tutosaurus;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.AuthServer;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;
import ch.epfl.sweng.tutosaurus.Tequila.Profile;

/**
 * Created by Stephane on 10/24/2016.
 */

public class AuthFragment extends DialogFragment {

    private static final String CLIENT_ID = "2e58be9551a5fd7286b718bd@epfl.ch";
    private static final String CLIENT_KEY = "97fb52cdc30384634c5eeb8cdc684baf";
    private static final String REDIRECT_URI = "tutosaurus://login";

    private WebView webViewOauth;

    private static OAuth2Config config;
    private static Map<String, String> tokens;
    private static Profile profile;
    String codeRequestUrl;

    static String firstName;
    static String lastName;
    static String sciper;
    static String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    static AuthFragment newInstance(int num) {
        AuthFragment f = new AuthFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    private static class MyWebViewClient extends WebViewClient {
        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //check if the login was successful and the access token returned
            //this test depend of your API
            if (url.contains("code=")) {
                //save your token
                String code = AuthClient.extractCode(url);
                getAccessToken(config, code);
                //getProfile();
                return false;
            }
            return false;
        }*/

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("code=")){
                //new ManageAccessToken().execute(url);
                /**enableStrictMode();
                getAccessToken(config, code);**/
                getProfile();
                firstName = profile.firstNames;
                lastName = profile.lastNames;
                sciper = profile.sciper;
                email = profile.email;

                Log.d("FirstName!", firstName);
                Log.d("LastName!", lastName);
                Log.d("Sciper", sciper);
                Log.d("Email", email);
            }
        }
    }

    private class ManageAccessToken extends AsyncTask<String, Integer, Long>{


        @Override
        protected Long doInBackground(String... url){
            String code = AuthClient.extractCode(url[0]);
            getAccessToken(config, code);
            return 0l;
        }

    }

    @Override
    public void onViewCreated(View arg0, Bundle arg1) {
        super.onViewCreated(arg0, arg1);

        try{
            config = readConfig();
            codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        }catch(IOException e){

        }

        //load the url of the oAuth login page
        webViewOauth
                .loadUrl(codeRequestUrl);
        //set the web client
        webViewOauth.setWebViewClient(new MyWebViewClient());
        //activates JavaScript (just in case)
        WebSettings webSettings = webViewOauth.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Retrieve the webview
        View v = inflater.inflate(R.layout.auth_screen, container, false);
        webViewOauth = (WebView) v.findViewById(R.id.web_oauth);
        getDialog().setTitle("Tequila");
        return v;
    }

    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
    }

    private static void getAccessToken(OAuth2Config config, String code){
        try{
            tokens = AuthServer.fetchTokens(config, code);
        }catch(IOException e){

        }
    }

    private static void getProfile(){
        try{
            profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
        }catch(IOException e){

        }
    }

    public static void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }

}

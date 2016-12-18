package ch.epfl.sweng.tutosaurus;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.tutosaurus.Tequila.AuthClient;
import ch.epfl.sweng.tutosaurus.Tequila.AuthServer;
import ch.epfl.sweng.tutosaurus.Tequila.OAuth2Config;
import ch.epfl.sweng.tutosaurus.Tequila.Profile;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Stephane on 12/7/2016.
 */

public class RegisterScreenActivityTest {
    private Solo solo;
    private String validTequilaUrl = "https://tequila.epfl.ch/cgi-bin/tequila/login";

    private static final String CLIENT_ID = "2e58be9551a5fd7286b718bd@epfl.ch";
    private static final String CLIENT_KEY = "97fb52cdc30384634c5eeb8cdc684baf";
    private static final String REDIRECT_URI = "tutosaurus://login";

    @Rule
    public ActivityTestRule<RegisterScreenActivity> mActivityRule = new ActivityTestRule<>(
            RegisterScreenActivity.class);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),
                mActivityRule.getActivity());
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @Test
    public void ClickOnRegisterPopupTequilaLoginWebpage(){
        solo.assertCurrentActivity("correct activity", RegisterScreenActivity.class);
        solo.typeText(0, "albert");
        solo.typeText(1, "notThePassword");
        solo.clickOnView(solo.getView(R.id.registerLogin));
        //boolean tequilaWebPage = solo.searchText("tequila.epfl.ch");
        //assertTrue(tequilaWebPage);
        boolean teqWebPage = solo.searchText("Sign in");
        assertTrue(teqWebPage);
    }

    @Test
    public void testRegisterHomeUpGoesToMain() {
        solo.assertCurrentActivity("correct activity", RegisterScreenActivity.class);
        Intents.init();
        solo.clickOnActionBarHomeButton();
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }

    @Test(expected = IOException.class)
    public void testFetchBadTokensThrowsException()throws IOException{
        OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
        String code = "nonsense";
        AuthServer authServer = new AuthServer();
        Map<String, String> tokens = authServer.fetchTokens(config, code);
    }

    @Test(expected = IOException.class)
    public void testFetchProfileWithBadAcessToken() throws IOException{
        String invalid_token = "Bearer jd8ff5edaa22386b785ced275b13d625e4e14c07";
        AuthServer authServer = new AuthServer();
        Profile profile = authServer.fetchProfile(invalid_token);
    }

    @Test
    public void testAuthClientCodeExtract() {
        String uri = "url&code=1234";
        AuthClient authClient = new AuthClient();
        String codeExtract = authClient.extractCode(uri);
        assertEquals(codeExtract, "1234");
    }

    /**@Test
    public void testFetchTokens() throws IOException {
        OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, CLIENT_ID, CLIENT_KEY, REDIRECT_URI);
        String uri_3 = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/token?client_id=2e58be9551a5fd7286b718bd%40epfl.ch&client_secret=97fb52cdc30384634c5eeb8cdc684baf&redirect_uri=tutosaurus%3A%2F%2Flogin&grant_type=authorization_code&code=7655b2c3467560ebfa5f1a21ca609351&scope=Tequila.profile";
        String code = AuthClient.extractCode(uri_3);
        Map<String, String> tokens = AuthServer.fetchTokens(config, code);
    }

    @Test
    public void encodeUrlToJson() throws IOException{
        //String url =  "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" +
                               "?access_token=%7B+%22Sciper%22%3A+000000%2C+%22Username%22%3A+%22albert%22%2C+%22Firstname%22%3A+%22Albert%22%2C+%22Name%22%3A+%22Einstein%22%2C+%22Email%22%3A+%22albert.einstein%40epfl.ch%22+%7D";
        String url = "Bearer ad8ff5edaa22386b785ced275b13d625e4e14c07";
        Profile profile = AuthServer.fetchProfile(url);
        String email = profile.email;
        boolean eq = email.equals("stephan.selim@epfl.ch");
        assertEquals(email, "stephan.selim@epfl.ch");
    }*/

}

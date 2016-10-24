package ch.epfl.sweng.tutosaurus.Tequila;

/**
 * Created by Stephane on 10/24/2016.
 */

import android.text.TextUtils;

/**
 * Client code for Tequila authentication.
 *
 * @author Solal Pirelli
 */
public final class AuthClient {
    public static String createCodeRequestUrl(OAuth2Config config) {
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + TextUtils.join(",", config.scopes);
    }

    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }
}
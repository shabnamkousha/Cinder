package com.anuj.cinders.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anuj.cinders.R;
import com.anuj.cinders.utils.FoursquareTokenStore;
import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

/**
 * Created by anujacharya on 2/21/16.
 */
public class LoginActivity extends FragmentActivity {

    /**
     * Obtain your client id and secret from:
     * https://foursquare.com/developers/apps
     *
     * NOTE: Make sure to run
     * keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     * and put that hash value in the foursquare app console
     */
    private static final String CLIENT_ID = "XOVRZY35C4WWKXQR53KQGFD55HYEDUBACNSWND3D2PFW3OFY";
    private static final String CLIENT_SECRET = "SXMSMHOLQ3INMHT42DOWBF3XKKVX5IGE2XL2B5FELY5E2ZHQ";

    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ensureUi();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                onCompleteConnect(resultCode, data);
                break;

            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                onCompleteTokenExchange(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Update the UI. If we already fetched a token, we'll just show a success
     * message.
     */
    private void ensureUi() {
        boolean isAuthorized = !TextUtils.isEmpty(FoursquareTokenStore.get().getToken());

        TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvMessage.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the native auth flow.
                Intent intent = FoursquareOAuth.getConnectIntent(LoginActivity.this, CLIENT_ID);

                // If the device does not have the Foursquare app installed, we'd
                // get an intent back that would open the Play Store for download.
                // Otherwise we start the auth flow.
                if (FoursquareOAuth.isPlayStoreIntent(intent)) {
                    toastMessage(LoginActivity.this, getString(R.string.app_not_installed_message));
                    startActivity(intent);
                } else {
                    startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
                }
            }
        });
    }

    private void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();
            performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                toastMessage(this, "Canceled");

            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                toastMessage(this, "Denied");

            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else if (exception instanceof FoursquareUnsupportedVersionException) {
                // Unsupported Fourquare app version on the device.
                toastError(this, exception);

            } else if (exception instanceof FoursquareInvalidRequestException) {
                // Invalid request.
                toastError(this, exception);

            } else {
                // Error.
                toastError(this, exception);
            }
        }
    }

    private void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.
            toastMessage(this, "Access token: " + accessToken);

            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            FoursquareTokenStore.get().setToken(accessToken);

            //Anuj
            // Redirect to the Club Activity Intent
            Intent i = new Intent(LoginActivity.this, ClubActivity.class);
            startActivity(i);

        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = ((FoursquareOAuthException) exception).getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(this, errorMessage + " [" + errorCode + "]");

            } else {
                // Other exception type.
                toastError(this, exception);
            }
        }
    }

    /**
     * Exchange a code for an OAuth Token. Note that we do not recommend you
     * do this in your app, rather do the exchange on your server. Added here
     * for demo purposes.
     *
     * @param code
     *          The auth code returned from the native auth flow.
     */
    private void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(this, CLIENT_ID, CLIENT_SECRET, code);
        startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastError(Context context, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

}

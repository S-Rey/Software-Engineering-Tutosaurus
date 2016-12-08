package ch.epfl.sweng.tutosaurus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Stephane on 12/2/2016.
 */

/**
 * Class to be instantiated only once in MainActivity thus preferable to be Singleton
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    private Activity activity;
    public static final String LOG_TAG = "CheckNetworkStatus";
    private boolean isConnected;
    private boolean broadcastToastEnabled = false;
    private ArrayList<Button> buttonsToManage;
    private TextView netStatusTextView;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(LOG_TAG, "Receieved notification about network status");
        isNetworkAvailable(context);
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.isConnectedOrConnecting()) {
                    if (!isConnected()) {
                        Log.v(LOG_TAG, "Now you are connected to Internet!");
                        if (broadcastToastEnabled) {
                            Toast.makeText(activity, "You got internet!", Toast.LENGTH_SHORT).show();
                        }
                        isConnected = true;
                        //do your processing here ---
                        //if you need to post any data to the server or get status
                        //update from the server
                        setEnabledWhenConnected();
                        showConnectedStatus();
                    }
                    return true;
                }
            }
        }
        Log.v(LOG_TAG, "You are not connected to Internet!");
        if (broadcastToastEnabled) {
            Toast.makeText(activity, "You got no internet", Toast.LENGTH_SHORT).show();
        }
        setDisabledWhenNotConnected();
        showNotConnectedStatus();
        isConnected = false;
        return false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setButtonsToManage(ArrayList<Button> buttons) {
        if (buttons == null) {
            throw new IllegalArgumentException("null reference to buttons to manage");
        } else if (!buttons.isEmpty()) {
            buttonsToManage = new ArrayList<Button>(buttons);
        }
    }

    private void setEnabledWhenConnected() {
        if (buttonsToManage != null && !buttonsToManage.isEmpty()) {
            for (Button button : buttonsToManage) {
                button.setEnabled(true);
            }
        }
    }

    private void setDisabledWhenNotConnected() {
        if (buttonsToManage != null && !buttonsToManage.isEmpty()) {
            for (Button button : buttonsToManage) {
                button.setEnabled(false);
            }
        }
    }

    public void setActivity(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Null reference to activity");
        }
        this.activity = activity;
    }

    public void setNetStatusTextView(TextView textView) {
        if (textView == null) {
            throw new IllegalArgumentException("Null reference to net status textView");
        }
        netStatusTextView = textView;
    }

    private void showConnectedStatus() {
        if (netStatusTextView != null) {
            netStatusTextView.setText(R.string.status_connected);
        }
    }

    private void showNotConnectedStatus() {
        if (netStatusTextView != null) {
            netStatusTextView.setText(R.string.status_not_connected);
        }
    }

    /**
     * If enabled, network receiver will post a toast to notify every time connection status changes
     */
    public void setBroadcastToastEnabled() {
        broadcastToastEnabled = true;
    }

}
package ch.epfl.sweng.tutosaurus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ch.epfl.sweng.tutosaurus.Tequila.MyAppVariables;

public class MainActivity extends AppCompatActivity {

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.connectionButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                if (MyAppVariables.getRegistered() == false) {
//                    AlertDialog.Builder signUpAlertB = new AlertDialog.Builder(MainActivity.this);
//                    signUpAlertB.setMessage("Sign up First!");
//
//                    signUpAlertB.setPositiveButton("Ok", null);
//
//                    AlertDialog signUpAlert = signUpAlertB.create();
//                    signUpAlert.show();
//                } else {
                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                }
           // }
        });

        Intent intent = getIntent();
    }

//METHOD FOR NOTIFICATION IN "MY APPOINT RESULTS" TAB
    public void notification(View view) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Meeting Notification")
                        .setContentText("Click Here To Test The Notification")
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.colorPrimaryDark));
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, HomeScreenActivity.class);
        resultIntent.setAction("OPEN_TAB_MEETINGS");
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeScreenActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(9999, mBuilder.build());
    }

    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
        startActivity(intent);
    }

}

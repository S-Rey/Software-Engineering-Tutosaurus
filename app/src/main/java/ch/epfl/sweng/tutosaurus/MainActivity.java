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
                if (MyAppVariables.getRegistered() == false) {
                    AlertDialog.Builder signUpAlertB = new AlertDialog.Builder(MainActivity.this);
                    signUpAlertB.setMessage("Sign up First!");

                    signUpAlertB.setPositiveButton("Ok", null);

                    AlertDialog signUpAlert = signUpAlertB.create();
                    signUpAlert.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                    intent.setAction("OPEN_TAB_PROFILE");
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
    }

    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
        startActivity(intent);
    }

    public void openLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}

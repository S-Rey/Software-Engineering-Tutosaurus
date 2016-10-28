package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                if(MyAppVariables.getRegistered() == false){
                    AlertDialog.Builder signUpAlertB = new AlertDialog.Builder(MainActivity.this);
                    signUpAlertB.setMessage("Sign up First!");

                    signUpAlertB.setPositiveButton("Ok", null);

                    AlertDialog signUpAlert = signUpAlertB.create();
                    signUpAlert.show();
                }else {
                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                }
            }
        });

        Intent intent = getIntent();
    }

    public void sendMessageForHome(View view) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
        startActivity(intent);
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, PublicProfileActivity.class);
        startActivity(intent);
    }

    public void openLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }
}

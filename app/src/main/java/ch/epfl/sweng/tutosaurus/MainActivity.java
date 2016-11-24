package ch.epfl.sweng.tutosaurus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.epfl.sweng.tutosaurus.Tequila.MyAppVariables;
import ch.epfl.sweng.tutosaurus.helper.LocalDatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.User;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    private Button login;
    private Button bypassLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



        login = (Button) findViewById(R.id.connectionButton);
        bypassLogin = (Button) findViewById(R.id.mainBypassLoginButton);

        bypassLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*if (MyAppVariables.getRegistered() == false) {
                    AlertDialog.Builder signUpAlertB = new AlertDialog.Builder(MainActivity.this);
                    signUpAlertB.setMessage("Sign up First!");

                    signUpAlertB.setPositiveButton("Ok", null);

                    AlertDialog signUpAlert = signUpAlertB.create();
                    signUpAlert.show();
                } else {*/
                String email = "albert.einstein@epfl.ch";
                String password = "tototo";
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                                if (task.isSuccessful()) {
                                   dispatchHomeScreenIntent();
                                } else {
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT);
                                }
                            }
                        });
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder loginAlertB = new AlertDialog.Builder(MainActivity.this);
                loginAlertB.setTitle("Login").setPositiveButton("Ok", null).setIcon(R.drawable.dino_logo);
                String email = ((EditText) findViewById(R.id.main_email)).getText().toString();
                String password = ((EditText) findViewById(R.id.main_password)).getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    loginAlertB.setMessage("Please type in your email and password");
                    loginAlertB.create().show();
                }else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        dispatchHomeScreenIntent();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT);
                                        loginAlertB.setMessage("Login failed");
                                        loginAlertB.create().show();
                                    }
                                }
                            });
                }
            }
        });

    }

    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
        startActivity(intent);
    }

    public void openLocation(View view) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivity(intent);
    }

    private void dispatchHomeScreenIntent() {
        Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
        intent.setAction("OPEN_TAB_PROFILE");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    public void testDB () {
        dbHelper = new LocalDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        Toast.makeText(getBaseContext(),database.toString(),Toast.LENGTH_LONG).show();

        User profileTwo = new User("223415");
        profileTwo.setUsername("Albert");
        profileTwo.setFullName("Albert Einstein");
        profileTwo.setEmail("albert.einstein@epfl.ch");
        profileTwo.setPicture(R.drawable.einstein);

        profileTwo.addLanguage("German");
        profileTwo.addLanguage("English");

        profileTwo.addStudying("French");
        profileTwo.addTeaching("Physics");

        profileTwo.setCourseRating("Physics", 1.0);
        profileTwo.addHoursTaught("Physics", 4);

        LocalDatabaseHelper.insertUser(profileTwo, database);
        User user = LocalDatabaseHelper.getUser(dbHelper.getReadableDatabase());
        Toast.makeText(getBaseContext(),user.getUsername(),Toast.LENGTH_LONG).show();
    }
}

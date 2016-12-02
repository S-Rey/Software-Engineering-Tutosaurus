package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.tutosaurus.helper.LocalDatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.User;

import static ch.epfl.sweng.tutosaurus.NetworkChangeReceiver.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private NetworkChangeReceiver receiver;
    private TextView networkStatus;

    public final static String TAG = "MainActivity";

    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    SQLiteOpenHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        receiver.setActivity(MainActivity.this);
        receiver.setBroadcastToastEnabled();

        networkStatus = (TextView) findViewById(R.id.networkStatus);
        receiver.setNetStatusTextView(networkStatus);

        Button resetPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        Button login = (Button) findViewById(R.id.connectionButton);
        Button bypassLogin = (Button) findViewById(R.id.mainBypassLoginButton);

        ArrayList<Button> buttons = new ArrayList<Button>();
        buttons.add(resetPasswordButton);
        buttons.add(registerButton);
        buttons.add(login);
        buttons.add(bypassLogin);

        receiver.setButtonsToManage(buttons);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
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
                LoginAsyncTask loginTask = new LoginAsyncTask();
                loginTask.execute(email, password);
                Log.d(TAG, "3");
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder loginAlertB = new AlertDialog.Builder(MainActivity.this);
                loginAlertB.setTitle("Login").setPositiveButton("Ok", null).setIcon(R.drawable.dino_logo);
                String email = ((EditText) findViewById(R.id.main_email)).getText().toString();
                String password = ((EditText) findViewById(R.id.main_password)).getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    loginAlertB.setMessage("Please type in your email and password");
                    loginAlertB.create().show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        dispatchHomeScreenIntent();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                        loginAlertB.setMessage("Login failed");
                                        loginAlertB.create().show();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(receiver);

    }


    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
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


    public void testDB() {
        dbHelper = new LocalDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        Toast.makeText(getBaseContext(), database.toString(), Toast.LENGTH_LONG).show();

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
        Toast.makeText(getBaseContext(), user.getUsername(), Toast.LENGTH_LONG).show();
    }

    private class LoginAsyncTask extends AsyncTask<String, String, Task> {

        @Override
        protected Task doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
            task.addOnCompleteListener(new LoginOnCompleteListener());
            try {
                Tasks.await(task);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "1");
            return task;
        }

        @Override
        protected void onPostExecute(Task task) {
            Log.d(TAG, "2");
        }
    }

    private class LoginOnCompleteListener implements OnCompleteListener<AuthResult> {
        @Override
        public void onComplete(@NonNull Task task) {
            Log.d(TAG, "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
            if (task.isSuccessful()) {
                dispatchHomeScreenIntent();
            } else {
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        }


//    public void testStorage() {
//        String localPicPath = "/storage/emulated/0/Pictures/android.png";
//        String onlinePicPath = "logo/android.png";
//
//        PictureHelper.storePictureOnline(localPicPath,onlinePicPath);
//
//    }
    }
}

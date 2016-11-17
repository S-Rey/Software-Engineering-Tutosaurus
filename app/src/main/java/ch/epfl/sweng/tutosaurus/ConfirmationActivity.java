package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;
import ch.epfl.sweng.tutosaurus.model.User;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String TAG = "ConfirmationActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String email;
    private String fullName;
    private String sciper;
    private String gaspar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        Intent intent = getIntent();

        String first_name = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_FIRST_NAME);
        String last_name = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_LAST_NAME);
        email = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_EMAIL_ADDRESS);
        sciper = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_SCIPER);
        fullName = first_name + " " + last_name;
        gaspar = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_GASPAR);

        TextView first_name_text = (TextView) findViewById(R.id.firstNameProvided);
        first_name_text.setText("First name : " + first_name);
        TextView last_name_text = (TextView) findViewById(R.id.lastNameProvided);
        last_name_text.setText("Last name : " + last_name);
        TextView email_address_text = (TextView) findViewById(R.id.emailAddressProvided);
        email_address_text.setText("Email address : " + email);
        TextView sciper_text = (TextView) findViewById(R.id.sciperProvided);
        sciper_text.setText("Sciper : " + sciper);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), RegisterScreenActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void confirmRegistration(View view) {
        String pw1 = ((EditText)findViewById(R.id.confirmation_password1)).getText().toString();
        String pw2 = ((EditText)findViewById(R.id.confirmation_password2)).getText().toString();
        if(!pw1.equals(pw2)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, pw1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());
                    if(!task.isSuccessful()) {
                        Toast.makeText(ConfirmationActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                    } else {
                        String uid = user.getUid();
                        DatabaseHelper dbh = DatabaseHelper.getInstance();
                        DatabaseReference userRef = dbh.getReference().child(DatabaseHelper.USER_PATH).child(uid);
                        User user = new User(sciper, gaspar);
                        user.setEmail(email);
                        user.setFullName(fullName);
                        user.setUid(uid);
                        dbh.signUp(user);
                        finish();
                    }
                }
            });
        }
    }
}

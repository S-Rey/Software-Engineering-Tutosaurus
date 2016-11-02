package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConfirmationActivity extends AppCompatActivity {

    public static final String TAG = "ConfirmationActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
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
        String email_address = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_EMAIL_ADDRESS);
        email = email_address;
        String sciper = intent.getStringExtra(RegisterScreenActivity.EXTRA_MESSAGE_SCIPER);

        TextView first_name_text = (TextView) findViewById(R.id.firstNameProvided);
        first_name_text.setText("First name : " + first_name);
        TextView last_name_text = (TextView) findViewById(R.id.lastNameProvided);
        last_name_text.setText("Last name : " + last_name);
        TextView email_address_text = (TextView) findViewById(R.id.emailAddressProvided);
        email_address_text.setText("Email address : " + email_address);
        TextView sciper_text = (TextView) findViewById(R.id.sciperProvided);
        sciper_text.setText("Sciper : " + sciper);


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
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT);
        } else {

            mAuth.createUserWithEmailAndPassword(email, pw1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful());
                    if(!task.isSuccessful()) {
                        Toast.makeText(ConfirmationActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                }
            });
        }
    }
}

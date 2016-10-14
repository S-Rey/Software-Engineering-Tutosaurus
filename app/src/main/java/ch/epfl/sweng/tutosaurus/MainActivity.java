package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button connection_button;
    EditText username_text;
    EditText password_text;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection_button = (Button)findViewById(R.id.connectionButton);
        username_text = (EditText)findViewById(R.id.usernameEntry);
        password_text = (EditText)findViewById(R.id.passwordEntry);

        connection_button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        username = username_text.getText().toString();
                        password = password_text.getText().toString();

                        sendMessageForHome(view);
                    }
                }
        );

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
}

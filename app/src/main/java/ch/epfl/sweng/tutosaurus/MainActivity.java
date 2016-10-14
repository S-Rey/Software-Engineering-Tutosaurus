package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
    }

    public void sendMessageForHome(View view) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

    public void sendMessageForReg(View view) {
        Intent intent = new Intent(this, RegisterScreenActivity.class);
    }

    public void openProfile(View view) {
        Intent intent = new Intent(this, PublicProfileActivity.class);

        startActivity(intent);
    }
}

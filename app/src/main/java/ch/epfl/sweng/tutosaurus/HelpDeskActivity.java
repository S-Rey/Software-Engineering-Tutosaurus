package ch.epfl.sweng.tutosaurus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class HelpDeskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);

        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

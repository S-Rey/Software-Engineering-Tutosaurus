package ch.epfl.sweng.tutosaurus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ch.epfl.sweng.tutosaurus.helper.DatabaseHelper;

public class DatabaseFragment extends Fragment implements View.OnClickListener {

    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.database_fragment, container, false);
        Button testButton = (Button) myView.findViewById(R.id.dbSubmitButton);
        testButton.setOnClickListener(this);
        Button signupButton = (Button) myView.findViewById(R.id.db_signup_button);
        signupButton.setOnClickListener(this);
        return myView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dbSubmitButton :
                insertIntoDb();
                break;
            case R.id.db_signup_button :
                signUp();
                break;
        }
    }

    private void insertIntoDb(){
        String value = ((EditText)myView.findViewById(R.id.dbTestInput)).getText().toString();
        DatabaseHelper dbh = new DatabaseHelper();
        dbh.writeSomething(value);
    }

    private void signUp() {
        DatabaseHelper dbh = new DatabaseHelper();
        String username = ((EditText)myView.findViewById(R.id.db_signup_username)).getText().toString();
        String fullName = ((EditText)myView.findViewById(R.id.db_signup_name)).getText().toString();
        dbh.signUp(username, fullName);
    }
}

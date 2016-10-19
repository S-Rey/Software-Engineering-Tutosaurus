package ch.epfl.sweng.tutosaurus.helper;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {

    FirebaseDatabase db;

    public DatabaseHelper(){
        super();
        db = FirebaseDatabase.getInstance();
    }

    public void writeSomething(String value) {
        DatabaseReference ref = db.getReference("test");
        ref.setValue(value);
    }
    public void signUp(String sciper, String username, String name, String email) {
        String headPath = "users/" + sciper;
        String pUsername = headPath + "/username";
        String pName = headPath + "/name";
        String pEmail = headPath + "/email";
        Log.d("DBH", "headPath: " + headPath);
        DatabaseReference ref = db.getReference(pUsername);
        ref.setValue(username);
        ref = db.getReference(pName);
        ref.setValue(name);
        ref = db.getReference(pEmail);
        ref.setValue(email);

    }
}
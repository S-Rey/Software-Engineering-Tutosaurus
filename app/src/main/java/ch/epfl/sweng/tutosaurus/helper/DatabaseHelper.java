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

    public void signUp(String username, String name){
        String path = "users/" + username + "/name";
        Log.d("DBH", "path: " + path);
        DatabaseReference ref = db.getReference(path);
        ref.setValue(name);
    }
}
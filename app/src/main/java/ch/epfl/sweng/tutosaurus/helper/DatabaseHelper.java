package ch.epfl.sweng.tutosaurus.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ubervison on 11.10.16.
 */

public class DatabaseHelper {

    public void writeSomething(String message) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("test");
        ref.setValue(message);
    }
}
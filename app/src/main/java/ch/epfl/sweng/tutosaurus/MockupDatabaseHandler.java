package ch.epfl.sweng.tutosaurus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

/**
 * Created by albertochiappa on 17/10/16.
 */

public class MockupDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "profiles";
    private static final String DATABASE_NAME = "profileMockup";

    // Shops Table Columns names
    private static final String SCIPER = "sciperNumber";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String PROFESSOR_RATING = "professorRating";
    private static final String STUDENT_RATING = "studentRating";

    public MockupDatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE" + TABLE_NAME + "("
        + SCIPER + "INTEGER PRIMARY KEY," + NAME + "TEXT,"
        + SURNAME + "TEXT" +  PROFESSOR_RATING +"REAL"+ STUDENT_RATING +
                "REAL)";
        db.execSQL(CREATE_PROFILE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
    public void addProfile(ProfileMockup profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCIPER, profile.getSciperNumber());
        values.put(NAME, profile.getName());
        values.put(SURNAME, profile.getSurname());
        values.put(STUDENT_RATING, profile.getStudentRating());
        values.put(PROFESSOR_RATING, profile.getProfessorRating());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}

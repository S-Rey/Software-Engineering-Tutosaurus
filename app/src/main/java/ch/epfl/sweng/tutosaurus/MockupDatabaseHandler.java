package ch.epfl.sweng.tutosaurus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MockupDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "profiles";
    private static final String DATABASE_NAME = "profileMockup";

    // Profile Table Columns names
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

        // Initializing with one profile
        ContentValues values=new ContentValues();
        values.put(SCIPER,273516);
        values.put(NAME,"Alberto");
        values.put(SURNAME,"Chiappa");
        values.put(PROFESSOR_RATING,3.5f);
        values.put(STUDENT_RATING,4f);
        db.insert(TABLE_NAME,null,values);
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

    public ProfileMockup getProfile(int sciper) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] sciperString=new String[] { String.valueOf(sciper) };
        Cursor cursor = db.query(TABLE_NAME, null, SCIPER + "=?", sciperString, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return new ProfileMockup(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Float.parseFloat(cursor.getString(3)),
                Float.parseFloat(cursor.getString(4)));
    }

    public void deleteProfile (ProfileMockup profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, SCIPER + " = ?",
                new String[] { String.valueOf(profile.getSciperNumber()) });
        db.close();
    }

}

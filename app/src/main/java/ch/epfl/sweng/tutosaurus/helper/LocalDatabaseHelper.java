package ch.epfl.sweng.tutosaurus.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ch.epfl.sweng.tutosaurus.model.User;

/**
 * Created by samuel on 06.11.16.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    /*
    private String sciper;
    private String username;
    private String fullName;
    private String email;
    private String uid;
    private int profilePicture;

    private Map<String, Boolean> languages = new HashMap<>();

    private Map<String, Boolean> teaching = new HashMap<>();
    private Map<String, Boolean> studying = new HashMap<>();

    private Map<String, Double> ratings = new HashMap<>(); */
/* (course id -> globalRating) *//*

    private Map<String, Integer> totalHoursTaught = new HashMap<>(); */
/* (course id -> hours taught *//*


    private double globalRating;
*/


    private static final String LOGCAT = "LOGCATDB";

    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    // table user
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_SCIPER = "user_sciper";
    public static final String COLUMN_USER_USERNAME = "user_username";
    public static final String COLUMN_USER_FULLNAME = "user_full_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PICTURE = "user_picture";
    public static final String COLUMN_USER_GLOBAL_RATING = "user_global_rating";

    // create table of user
    private static final String CREATE_TABLE_USER = " CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USER_SCIPER + " TEXT, " +
            COLUMN_USER_USERNAME + " TEXT, " +
            COLUMN_USER_FULLNAME + " TEXT, " +
            COLUMN_USER_EMAIL + " TEXT, " +
            COLUMN_USER_PICTURE + " TEXT, " +
            COLUMN_USER_GLOBAL_RATING + " REAL " + ")";

    // table course
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_IS_TEACHED = "course_is_teached";
    public static final String COLUMN_COURSE_IS_STUDIED = "course_is_studied";
    public static final String COLUMN_COURSE_RATING = "course_rating";
    public static final String COLUMN_COURSE_HOURS = "course_hours";


    // create table of course
    private static final String CREATE_TABLE_COURSE = " CREATE TABLE " + TABLE_COURSE + "(" +
            COLUMN_COURSE_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_COURSE_NAME + " TEXT, " +
            COLUMN_COURSE_IS_TEACHED + " INTEGER " +
            COLUMN_COURSE_IS_STUDIED + " INTEGER " +
            COLUMN_COURSE_RATING + " REAL, " +
            COLUMN_COURSE_HOURS + " INTEGER " + ")";


    public static final String TABLE_LANGUAGE = "language";
    public static final String COLUMN_LANGUAGE_NAME = "language_name";
    public static final String COLUMN_LANGUAGE_IS_SPOKEN = "language_is_spoken";

    // create table of langugage
    private static final String CREATE_TABLE_LANGUAGE = " CREATE TABLE " + TABLE_LANGUAGE + "(" +
            COLUMN_LANGUAGE_NAME + " TEXT " +
            COLUMN_LANGUAGE_IS_SPOKEN + " INTEGER " + ")";


    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        Log.d(LOGCAT, "User table has been created");
        db.execSQL(CREATE_TABLE_COURSE);
        Log.d(LOGCAT, "Course table has been created");
        db.execSQL(CREATE_TABLE_LANGUAGE);
        Log.d(LOGCAT, "Language table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_COURSE);
        onCreate(db);
    }


    /**
     * Save an User object into the database.
     * TODO check if already exist
     * also check for the profile picture (user = int, db = string)
     * @param user
     * @param db
     */
    public static void insertUser(User user,SQLiteDatabase db) {
        ContentValues userValues = new ContentValues();
        ContentValues courseValues = new ContentValues();
        ContentValues languageValues = new ContentValues();

        userValues.put(COLUMN_USER_ID, user.getUid());
        userValues.put(COLUMN_USER_SCIPER, user.getSciper());
        userValues.put(COLUMN_USER_USERNAME, user.getUsername());
        userValues.put(COLUMN_USER_FULLNAME, user.getFullName());
        userValues.put(COLUMN_USER_EMAIL, user.getEmail());



        for(String language : user.getLanguages().keySet()) {

        }
    }
}




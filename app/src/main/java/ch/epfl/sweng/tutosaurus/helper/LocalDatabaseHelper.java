package ch.epfl.sweng.tutosaurus.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Rating;

import java.util.HashMap;
import java.util.Map;

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
    public static final String COLUMN_USER_LANGUAGE = "user_language";
    public static final String COLUMN_USER_TEACHING = "user_teaching";
    public static final String COLUMN_USER_STUDYING = "user_studying";
    public static final String COLUMN_USER_PICTURE = "user_picture";
    public static final String COLUMN_USER_GLOBAL_RATING = "user_global_rating";

    // create table of user
    private static final String CREATE_TABLE_USER = " CREATE TABLE " + TABLE_USER + "(" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USER_SCIPER + " TEXT, " +
            COLUMN_USER_USERNAME + " TEXT, " +
            COLUMN_USER_FULLNAME + " TEXT, " +
            COLUMN_USER_EMAIL + " TEXT, " +
            COLUMN_USER_LANGUAGE + " TEXT, " +
            COLUMN_USER_TEACHING + " INTEGER, " +
            COLUMN_USER_STUDYING + " INTEGER, " +
            COLUMN_USER_PICTURE + " TEXT, " +
            COLUMN_USER_GLOBAL_RATING + " REAL " + ")";

    // table course
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_RATING = "course_rating";
    public static final String COLUMN_COURSE_HOURS = "course_hours";

    // create table of course
    private static final String CREATE_TABLE_COURSE = " CREATE TABLE " + TABLE_COURSE + "(" +
            COLUMN_COURSE_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_COURSE_NAME + " TEXT, " +
            COLUMN_COURSE_RATING + " REAL, " +
            COLUMN_COURSE_HOURS + " INTEGER " + ")";





    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_COURSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_COURSE);
        onCreate(db);
    }
}

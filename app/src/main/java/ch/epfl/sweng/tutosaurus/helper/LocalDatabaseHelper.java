package ch.epfl.sweng.tutosaurus.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
            COLUMN_USER_PICTURE + " INTEGER, " +
            COLUMN_USER_GLOBAL_RATING + " REAL " + ")";

    // table course
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_IS_TEACHED = "course_is_teached";
    public static final String COLUMN_COURSE_IS_STUDIED = "course_is_studied";
    public static final String COLUMN_COURSE_RATING = "course_rating";
    public static final String COLUMN_COURSE_HOURS = "course_hours";


    // create table of course
    private static final String CREATE_TABLE_COURSE = " CREATE TABLE " + TABLE_COURSE + "(" +
            COLUMN_COURSE_NAME + " TEXT PRIMARY KEY, " +
            COLUMN_COURSE_IS_TEACHED + " INTEGER, " +
            COLUMN_COURSE_IS_STUDIED + " INTEGER, " +
            COLUMN_COURSE_RATING + " REAL, " +
            COLUMN_COURSE_HOURS + " INTEGER " + ")";


    public static final String TABLE_LANGUAGE = "language";
    public static final String COLUMN_LANGUAGE_NAME = "language_name";

    // create table of langugage
    private static final String CREATE_TABLE_LANGUAGE = " CREATE TABLE " + TABLE_LANGUAGE + "(" +
            COLUMN_LANGUAGE_NAME + " TEXT PRIMARY KEY " + ")";


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
     *
     * also check for the profile picture (user = int, db = string)
     * @param user
     * @param db
     */
    public static void insertUser(User user,SQLiteDatabase db) {
        insertUserTable(user,db);
        insertCourseTable(user,db);
        insertLanguageTable(user,db);
    }



    public static void insertUserTable(User user, SQLiteDatabase db) {
        ContentValues userValues = new ContentValues();

        // USER TABLE
        userValues.put(COLUMN_USER_SCIPER, user.getSciper());
        userValues.put(COLUMN_USER_USERNAME, user.getUsername());
        userValues.put(COLUMN_USER_FULLNAME, user.getFullName());
        userValues.put(COLUMN_USER_EMAIL, user.getEmail());
        userValues.put(COLUMN_USER_ID, user.getUid());
        userValues.put(COLUMN_USER_PICTURE, user.getPicture());
        userValues.put(COLUMN_USER_GLOBAL_RATING, user.getGlobalRating());

        db.insert(TABLE_USER, null, userValues);
    }


    public static void insertCourseTable(User user, SQLiteDatabase db) {
        ContentValues courseValues = new ContentValues();

        // COURSE TABLE
        Set<String> courseNames = new HashSet();
        courseNames.addAll(user.getStudying().keySet());
        courseNames.addAll(user.getTeaching().keySet());

        for(String key : courseNames) {
            courseValues.put(COLUMN_COURSE_NAME, key);

            if (user.getStudying().containsKey(key)) {
                courseValues.put(COLUMN_COURSE_IS_STUDIED, true);
            } else {
                courseValues.put(COLUMN_COURSE_IS_STUDIED, false);
            }

            if (user.isTeacher(key)) {
                courseValues.put(COLUMN_COURSE_IS_TEACHED, true);
                courseValues.put(COLUMN_COURSE_RATING, user.getCourseRating(key));
            } else {
                courseValues.put(COLUMN_COURSE_IS_TEACHED, false);
                courseValues.put(COLUMN_COURSE_RATING, 0.0);
            }

            courseValues.put(COLUMN_COURSE_HOURS, user.getHoursTaught(key));
            db.insert(TABLE_COURSE, null, courseValues);

        }

    }


    public static void insertLanguageTable(User user, SQLiteDatabase db) {
        ContentValues languageValues = new ContentValues();

        // LANGUAGE TABLE
        for(String key : user.getLanguages().keySet()) {
            languageValues.put(COLUMN_LANGUAGE_NAME, key);
            db.insert(TABLE_LANGUAGE, null, languageValues);
        }

    }


    @Nullable
    public static User getUser(SQLiteDatabase db) {
        User user = getUserTable(db);
        getCourse(user, db);
        getLanguage(user, db);
        return user;
    }

    @Nullable
    public static User getUserTable(SQLiteDatabase db) {
        String query = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            User user = new User(cursor.getString(1));
            user.setUsername(cursor.getString(2));
            user.setFullName(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setUid(cursor.getString(0));
            user.setPicture(cursor.getInt(5));
            user.setGlobalRating(cursor.getDouble(6));
            return user;
        } else {
            return null;
        }
    }

    public static void getCourse(User user, SQLiteDatabase db) {
        if(user == null) {
            return;
        }
        String query = "SELECT * FROM " + TABLE_COURSE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToNext()){
            // if course is teached
            if(cursor.getInt(1) != 0) {
                user.addTeaching(cursor.getString(0));
                user.setCourseRating(cursor.getString(0), cursor.getDouble(3));
                user.addHoursTaught(cursor.getString(0), cursor.getInt(4));
            }
            // if course is studied
            if (cursor.getInt(2) != 0) {
                user.addStudying(cursor.getString(0));
            }
        }
    }

    public static void getLanguage(User user, SQLiteDatabase db) {
        if (user == null) {
            return;
        }

        String query = "SELECT * FROM " + TABLE_LANGUAGE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                user.addLanguage(cursor.getString(0));
            }
        }
    }
}




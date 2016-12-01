package ch.epfl.sweng.tutosaurus.helper;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.LocaleList;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;

/**
 * Created by samuel on 29.11.16.
 */

@RunWith(AndroidJUnit4.class)
public class LocalDatabaseHelperTest {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    @Before
    public void setup() {
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_COURSE);
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_USER);
        getTargetContext().deleteDatabase(LocalDatabaseHelper.TABLE_LANGUAGE);
        dbHelper = new LocalDatabaseHelper(getTargetContext());
        database = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        LocalDatabaseHelper.clear(database);
    }

    @Test
    public void testGetDatabase() {
        dbHelper.getReadableDatabase();
        dbHelper.getWritableDatabase();
    }

}

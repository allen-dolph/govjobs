package com.allendolph.android.govjobs;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.allendolph.android.govjobs.data.JobDbHelper;

/**
 * Created by allendolph on 1/2/15.
 */
public class TestDb extends AndroidTestCase  {
    public static final String LOG_TAG = "TEST_DB";

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(JobDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new JobDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }
}

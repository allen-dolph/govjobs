package com.allendolph.android.govjobs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.allendolph.android.govjobs.data.JobDbHelper;
import com.allendolph.android.govjobs.data.JobsContract;
import com.allendolph.android.govjobs.data.JobsContract.JobEntry;
import com.allendolph.android.govjobs.data.JobsContract.LocationEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by allendolph on 1/2/15.
 */
public class TestDb extends AndroidTestCase  {
    public static final String LOG_TAG = "TEST_DB";


    public static final String TEST_CITY = "San Francisco";
    public static final String TEST_STATE = "CA";

    ContentValues getLocationContentValues() {
        ContentValues values = new ContentValues();

        values.put(LocationEntry.COLUMN_CITY, TEST_CITY);
        values.put(LocationEntry.COLUMN_STATE, TEST_STATE);

        return values;
    }

    ContentValues getJobContentValues() {
        ContentValues values = new ContentValues();

        values.put(JobEntry.COLUMN_POSITION_TITLE, "Title");
        values.put(JobEntry.COLUMN_JOB_ID, "012345");
        values.put(JobEntry.COLUMN_JOB_URL, "http://job.com/url");
        values.put(JobEntry.COLUMN_ORGANIZATION_NAME, "Organization");
        values.put(JobEntry.COLUMN_START_DATETEXT, "20150101");
        values.put(JobEntry.COLUMN_END_DATETEXT, "20150201");
        values.put(JobEntry.COLUMN_RATE_INTERVAL_CODE, "INT_CODE");
        values.put(JobEntry.COLUMN_SALARY_MIN, "50000");
        values.put(JobEntry.COLUMN_SALARY_MAX, "100000");

        return values;
    }

    ContentValues getJobLocationLookupValues(long locId, long jobId) {
        ContentValues values = new ContentValues();

        values.put(JobsContract.LookupJobsLocationsEntry.COLUMN_LOC_KEY, locId);
        values.put(JobsContract.LookupJobsLocationsEntry.COLUMN_JOB_KEY, jobId);

        return values;
    }

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(JobDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new JobDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb()  {
        mContext.deleteDatabase(JobDbHelper.DATABASE_NAME);
        JobDbHelper dbHelper = new JobDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Get values for a test location
        ContentValues locValues = getLocationContentValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, locValues);

        // Verify we got a row back
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "new location row id: " + locationRowId);

        // get a cursor back and validate the values match what we inserted
        Cursor cursor = db.query(
                LocationEntry.TABLE_NAME, // Table to query
                null, // columns to return -> null for all
                null, // Columns for the where clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // Sort order
        );

        // use the helper method to validate it
        // (assertions in helper method)
        validateCursor(locValues, cursor);
        cursor.close();

        // Test insert and read Job
        ContentValues jobValues = getJobContentValues();

        long jobRowId;
        jobRowId = db.insert(JobEntry.TABLE_NAME, null, jobValues);

        // Verify we got a row back
        assertTrue(jobRowId != -1);
        Log.d(LOG_TAG, "new job row id: " + jobRowId);

        // get a cursor back and validate the values match our job content values
        cursor = db.query(
                JobEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // use the helper method to validate
        validateCursor(jobValues, cursor);
        cursor.close();

        // now that we have a job and location id, test that we can associated in
        // the lookup table
        ContentValues locLookupValues = getJobLocationLookupValues(locationRowId, jobRowId);

        long lookupId;
        lookupId = db.insert(JobsContract.LookupJobsLocationsEntry.TABLE_NAME, null, locLookupValues);

        // verify we got a row back
        assertTrue(lookupId != -1);

        // validate the values
        cursor = db.query(
                JobsContract.LookupJobsLocationsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        validateCursor(locLookupValues, cursor);
    }


    // Make sure that everything in our content matches our insert
    static public void validateCursor(ContentValues expectedValues, Cursor valueCursor) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        if(valueCursor.moveToFirst()) {
            for (Map.Entry<String, Object> entry : valueSet) {
                String columnName = entry.getKey();
                int idx = valueCursor.getColumnIndex(columnName);
                assertFalse(-1 == idx);

                String expectedValue = entry.getValue().toString();
                assertEquals(expectedValue, valueCursor.getString(idx));
            }
        } else {
            // no rows returned, something went wrong
            fail();
        }
    }
}

package com.allendolph.android.govjobs.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.allendolph.android.govjobs.data.JobsContract.LocationEntry;
import com.allendolph.android.govjobs.data.JobsContract.JobEntry;
import com.allendolph.android.govjobs.data.JobsContract.LookupJobsLocationsEntry;

/**
 * Created by allendolph on 12/31/14.
 */
public class JobDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "gov.jobs.db";

    public JobDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Location Table
        final String SQL_CREATE_LOCATION_TABLE =
                "CREATE TABLE " + LocationEntry.TABLE_NAME + "( " +
                        LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LocationEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                        LocationEntry.COLUMN_STATE + " TEXT NOT NULL, " +

                        // To assure the application has just one location per state
                        // per city, create a UNIQUE contraint with replace
                        " UNIQUE (" + LocationEntry.COLUMN_STATE + ", " +
                        LocationEntry.COLUMN_CITY + ") ON CONFLICT REPLACE)";

        db.execSQL(SQL_CREATE_LOCATION_TABLE);

        // Create the Jobs Table
        final String SQL_CREATE_JOBS_TABLE =
                "CREATE TABLE " + JobEntry.TABLE_NAME + " ( " +
                        JobEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        JobEntry.COLUMN_JOB_ID + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_POSITION_TITLE + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_ORGANIZATION_NAME + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_RATE_INTERVAL_CODE + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_SALARY_MIN + " INTEGER NOT NULL, " +
                        JobEntry.COLUMN_SALARY_MAX + " INTEGER NOT NULL, " +
                        JobEntry.COLUMN_START_DATETEXT + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_END_DATETEXT + " TEXT NOT NULL, " +
                        JobEntry.COLUMN_JOB_URL + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_JOBS_TABLE);

        // Create the job/location lookup table
        final String SQL_CREATE_JOB_LOCATION_LOOKUP_TABLE =
                "CREATE TABLE " + LookupJobsLocationsEntry.TABLE_NAME + " ( " +
                        LookupJobsLocationsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        LookupJobsLocationsEntry.COLUMN_JOB_KEY + " INTEGER NOT NULL, " +
                        LookupJobsLocationsEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +

                        // Setup the job column as a foreign key to jobs table
                        " FOREIGN KEY (" + LookupJobsLocationsEntry.COLUMN_JOB_KEY +
                        ") REFERENCES " + JobEntry.TABLE_NAME + " (" + JobEntry._ID + "), " +

                        // Setup the loc column as a foreign key to location table
                        " FOREIGN KEY (" + LookupJobsLocationsEntry.COLUMN_LOC_KEY +
                        ") REFERENCES " + LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "))";

        db.execSQL(SQL_CREATE_JOB_LOCATION_LOOKUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

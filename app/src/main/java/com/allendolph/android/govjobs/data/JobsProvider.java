package com.allendolph.android.govjobs.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by allendolph on 1/3/15.
 */
public class JobsProvider extends ContentProvider {
    // Constants to represent our possible Uris
    private static final int JOB = 100;
    private static final int LOCATION = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private JobDbHelper mOpenHelper;
    //private static final SQLiteQueryBuilder sJobByLocationQueryBuilder;

    /*
     * The custom uri matcher
     * All paths added to the UriMatcher have a corresponding code to return when
     * a match is found.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = JobsContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, JobsContract.PATH_JOB, JOB);

        matcher.addURI(authority, JobsContract.PATH_LOCATION, LOCATION);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new JobDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request
        // it is and query the database accordingly
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "job"
            case JOB: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        JobsContract.JobEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "location"
            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        JobsContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case JOB:
                return JobsContract.JobEntry.CONTENT_ITEM_TYPE;
            case LOCATION:
                return JobsContract.LocationEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case JOB: {
                long _id = db.insert(JobsContract.JobEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = JobsContract.JobEntry.buildJobUri(_id);
                } else {
                    throw new SQLException("Failed to insert job row into " + uri);
                }
                break;
            }
            case LOCATION: {
                long _id = db.insert(JobsContract.LocationEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = JobsContract.LocationEntry.buildLocationUri(_id);
                } else {
                    throw new SQLException("Failed to insert location row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(returnUri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final  SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case JOB:
                rowsDeleted = db.delete(JobsContract.JobEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION:
                rowsDeleted = db.delete(JobsContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case JOB:
                rowsUpdated = db.update(JobsContract.JobEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(JobsContract.LocationEntry.TABLE_NAME,
                        values, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int returnCount;

        switch (match) {
            case JOB:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(
                                JobsContract.JobEntry.TABLE_NAME,
                                null,
                                value
                        );
                        if(_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            case LOCATION:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(
                                JobsContract.LocationEntry.TABLE_NAME,
                                null,
                                value
                        );
                        if(_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            default:
                return super.bulkInsert(uri, values);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}

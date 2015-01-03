package com.allendolph.android.govjobs.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by allendolph on 12/31/14.
 */
public class JobsContract {

    // Content Provider name
    public static final String CONTENT_AUTHORITY = "com.allendolph.govjobs";

    // Use CONTENT_AUTHORITY to create a uri which apps will use to connect
    // to the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's
    public static final String PATH_JOB = "job";
    public static final String PATH_LOCATION = "location";

    // Format used to store post dates in the database
    public static final String DATE_FORMAT = "yyyyMMdd";

    /*
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT
     */
    public static String getDbDateString(java.util.Date date) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to a valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /* Inner class that defined the table contents of the jobs table */
    public static final class JobEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_JOB).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_JOB;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_JOB;

        // Table Name
        public static final String TABLE_NAME = "jobs";

        // Columns
        public static final String COLUMN_JOB_ID = "job_id";
        public static final String COLUMN_POSITION_TITLE = "position_title";
        public static final String COLUMN_ORGANIZATION_NAME = "organization_name";
        public static final String COLUMN_RATE_INTERVAL_CODE = "rate_intervale_code";
        public static final String COLUMN_SALARY_MIN = "salary_min";
        public static final String COLUMN_SALARY_MAX = "salary_max";
        public static final String COLUMN_START_DATETEXT = "start_date";
        public static final String COLUMN_END_DATETEXT = "end_date";
        public static final String COLUMN_JOB_URL = "job_url";

        // Uri builder helper methods
        public static Uri buildJobUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Utility methods to parse uris
        public static String getJobIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents for the location table */
    public static final class LocationEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_JOB;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_JOB;

        // Table Name
        public static final String TABLE_NAME = "locations";

        // Columns
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_CITY = "city";

        // Uri builder helper methods
        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        // Utility methods to parse uris
        public static String getLocationIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents for the location/job lookup */
    public static final class LookupJobsLocationsEntry implements BaseColumns {
        // We dont need a content uri as we are not going to allow direct access

        // Table Name
        public static final String TABLE_NAME = "lookup_jobs_locations";

        //Columns
        public static final String COLUMN_JOB_KEY = "job_id";
        public static final String COLUMN_LOC_KEY = "loc_id";
    }
}

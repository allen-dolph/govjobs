package com.allendolph.android.govjobs;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by allendolph on 12/31/14.
 */
public class FetchJobsTask extends AsyncTask<String, Void, Void> {
    public static final String LOG_TAG = "FETCH_JOBS_TASK";

    private ArrayAdapter<String> mJobsAdapter;
    private Context mContext;

    // Constructor
    public FetchJobsTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        String queryString = "";
        // If there are no params we cant search
        // Otherwise build up a url encoded string of the parameters
        if(params.length == 0) {
            return null;
        } else {
            for (int i = 0; i < params.length; i++) {
                queryString += params[i] + "+";
            }
            queryString = queryString.substring(0, queryString.length() - 1);
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jobsJsonStr = null;

        String format = "json";

        try {
            // Construct the URL for the DigitalGov Jobs Url
            // params are a search across all fields
            final String JOBS_BASE_URL =
                    "http://api.usa.gov/jobs/search.json";
            final String QUERY_PARAM = "query";

            Uri builtUri = Uri.parse(JOBS_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to the API and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if(inputStream == null) {
                // Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                // Stream was empty, no need to parse
                return null;
            }
            jobsJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            // If the code didn't successfully get the jobs data,
            // there is no need to proceed
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        // TODO
        // Parse the string to JSON using helper methods

        return null;
    }
}

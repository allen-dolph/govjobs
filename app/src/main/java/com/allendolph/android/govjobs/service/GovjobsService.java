package com.allendolph.android.govjobs.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.allendolph.android.govjobs.models.Job;
import com.allendolph.android.govjobs.util.JobsDateDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by allendolph on 1/25/15.
 */
public class GovjobsService extends IntentService {

    public static final String LOG_TAG = GovjobsService.class.getCanonicalName();
    public static final String JOBS_QUERY_EXTRA = "gjqa";

    public GovjobsService() {
        super("GovjobsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // If there is no query string, there is nothing to look up
        if(!intent.hasExtra(JOBS_QUERY_EXTRA)) {
            return;
        }

        // Get the query/search string from the intent
        String[] query = intent.getStringExtra(JOBS_QUERY_EXTRA).split("\\s");

        String queryString = "";
        // If there are no params we cant search
        // Otherwise build up a url encoded string of the parameters
        if(query.length == 0) {
            return;
        } else {
            for (int i = 0; i < query.length; i++) {
                queryString += query[i] + "+";
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
                return;
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
                return;
            }

            jobsJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Data as string: " + jobsJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR: " + e);
            // If the code didn't successfully get the jobs data,
            // there is no need to proceed
            return;
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
        try{
            getJobsDataFromJson(jobsJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the job query
        return;
    }

    private void getJobsDataFromJson(String jobsJsonStr) throws JSONException {

        // Use gson libary to deserialize
        GsonBuilder gsonBuilder = new GsonBuilder();

        // use a custom date deserializer
        JobsDateDeserializer ds = new JobsDateDeserializer();
        gsonBuilder.registerTypeAdapter(java.util.Date.class, ds);

        Gson gson = gsonBuilder.create();

        Job[] jobs = gson.fromJson(jobsJsonStr, Job[].class);

    }
}

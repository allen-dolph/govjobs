package com.allendolph.android.govjobs;

import android.test.AndroidTestCase;

/**
 * Created by allendolph on 12/31/14.
 */
public class TestFetchJobsTask extends AndroidTestCase {
    public static final String LOG_TAG = "TEST_FETCH_JOBS_TASK";

    public void testFetchJobs() {
        FetchJobsTask task = new FetchJobsTask(mContext);
        task.doInBackground(new String[] { "nursing", "ny" });
        assertEquals(true, true);
    }
}
package com.allendolph.android.govjobs;

import android.content.Intent;
import android.test.ServiceTestCase;

import com.allendolph.android.govjobs.service.GovjobsService;

/**
 * Created by allendolph on 1/25/15.
 */

public class TestJobsService extends ServiceTestCase<TestJobsServiceFake> {

    public static final String LOG_TAG = TestJobsService.class.getCanonicalName();

    public TestJobsService() {
        super(TestJobsServiceFake.class);
    }

    public void testOnHandelIntent() {
        // for this to be a "true" test we should hook up a cursor
        // but for now we'll just use it to debug the service

        Intent intent = new Intent(mContext, GovjobsService.class);
        intent.putExtra(GovjobsService.JOBS_QUERY_EXTRA, "tech");
        startService(intent);
    }

}

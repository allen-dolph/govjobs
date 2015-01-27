package com.allendolph.android.govjobs;

import android.content.Intent;

import com.allendolph.android.govjobs.service.GovjobsService;

/**
 * Created by allendolph on 1/25/15.
 */
public class TestJobsServiceFake extends GovjobsService {

    public TestJobsServiceFake() {
        super();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        onHandleIntent(intent);
        stopSelf(startId);
    }
}

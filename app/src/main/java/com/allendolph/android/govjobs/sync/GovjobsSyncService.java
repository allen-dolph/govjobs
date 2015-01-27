package com.allendolph.android.govjobs.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by allendolph on 1/24/15.
 */
public class GovjobsSyncService extends Service {
    private static final String LOG_TAG = GovjobsSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static GovjobsSyncAdapter sGovjobsSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - GovjobsSyncService");
        synchronized (sSyncAdapterLock) {
            if(sGovjobsSyncAdapter == null) {
                sGovjobsSyncAdapter = new GovjobsSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sGovjobsSyncAdapter.getSyncAdapterBinder();
    }
}

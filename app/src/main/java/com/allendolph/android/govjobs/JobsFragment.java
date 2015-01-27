package com.allendolph.android.govjobs;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by allendolph on 1/24/15.
 */
public class JobsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        /*
         * Callback for when an item has been selected
         */
        public void onItemSelected(String[] keywords);
    }

    private JobsAdapter mJobsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // we should also "refresh" the job listing//
        // TODO

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.jobs_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Only return jobs that have not expired.
        return  null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mJobsAdapter.swapCursor(null);
    }

}

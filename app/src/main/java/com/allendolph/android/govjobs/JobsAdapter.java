package com.allendolph.android.govjobs;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by allendolph on 1/24/15.
 */
public class JobsAdapter extends CursorAdapter {

    public JobsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }


}

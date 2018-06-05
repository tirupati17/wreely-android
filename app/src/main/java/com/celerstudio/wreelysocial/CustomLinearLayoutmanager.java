package com.celerstudio.wreelysocial;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class CustomLinearLayoutmanager extends LinearLayoutManager {
    public CustomLinearLayoutmanager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}

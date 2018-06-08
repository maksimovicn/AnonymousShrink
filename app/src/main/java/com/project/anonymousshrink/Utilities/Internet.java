package com.project.anonymousshrink.Utilities;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

public class Internet {

    public void internetAlert(final Context context, View view)
    {
        Snackbar snackbar= Snackbar.make(view,"No internet connection", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(context.getResources().getColor(android.R.color.white));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark) );
        snackbar.setAction("Enable", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        snackbar.show();
    }
}

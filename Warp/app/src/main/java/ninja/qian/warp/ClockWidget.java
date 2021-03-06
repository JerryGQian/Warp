package ninja.qian.warp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class ClockWidget extends AppWidgetProvider {

    static Util util;
    static Settings settings;



    private int mInterval = 250; // 5 seconds by default, can be changed later
    private Handler mHandler = new Handler();
    private static Context context = null;
    private static AppWidgetManager appWidgetManager = null;
    private static int appWidgetId = 0;
    private static SharedPreferences set = null;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
        System.out.println("StartingLoop");
    }
    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    void updateStatus() {
        updateAppWidget(context, appWidgetManager, appWidgetId, set);
    }

    static String daytimeToString(Util.Daytime dtime) {
        if (dtime == null) return "0:00";
        String secString = "";
        if (dtime.min >= 10)
            secString = Integer.toString(dtime.min);
        else if (dtime.min >= 0)
            secString = "0" + Integer.toString(dtime.min);
        return Integer.toString(dtime.hour % 12 == 0 ? 12 : dtime.hour % 12) + ":" + secString;
    }


    static void updateAppWidget(Context contex, AppWidgetManager appWidgetManage,
                                int appWidgetI, SharedPreferences se) {
        System.out.println("test1");
        context = contex;
        appWidgetManager = appWidgetManage;
        appWidgetId = appWidgetI;
        set = se;
        if (set != null && context != null) {
            if (util == null) {
                settings = new Settings(set);
                util = new Util(settings);
            }

            Util.PrintData data = util.convert();

            //Overlay Button
            /*Intent myIntent = new Intent(CurrentActivity.this, NextActivity.class);
            myIntent.putExtra("key", value); //Optional parameters
            CurrentActivity.this.startActivity(myIntent);*/

            // Construct the RemoteViews object

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            views.setTextViewText(R.id.time_text, daytimeToString(data.t));
            if (data.tot != null) {
                views.setTextColor(R.id.time_text, Color.parseColor("#22ffffff"));
            } else if (data.tot == null) {
                views.setTextColor(R.id.time_text, Color.parseColor("#ffffffff"));
            }

            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            views2.setTextViewText(R.id.ampm_text, data.t.ampm());

            RemoteViews views3 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            if (data.elapsed != null)
                views3.setTextViewText(R.id.elapsed_text, daytimeToString(data.elapsed));
            else if (data.elapsed == null) views3.setTextViewText(R.id.elapsed_text, "- : --");
            //views3.setTextViewText(R.id.elapsed_text, "0:27");

            RemoteViews views4 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            if (data.tot != null)
                views4.setTextViewText(R.id.total_text, daytimeToString(data.tot));
            else if (data.tot == null) views4.setTextViewText(R.id.total_text, "- : --");
            //views4.setTextViewText(R.id.total_text, "1:01");

            RemoteViews views5 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            views5.setTextViewText(R.id.slash_text, "/");

            RemoteViews views6 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            views6.setTextViewText(R.id.synced_text, ".");
            if (data.sync) { //sets color of dot
                views6.setTextColor(R.id.synced_text, Color.GREEN);
            } else if (!data.sync) {
                views6.setTextColor(R.id.synced_text, Color.YELLOW);
            }

            // Progress bar
            RemoteViews views7 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            //ProgressBar bar = (ProgressBar)findViewById(R.id.extraTimeBar);
            if (data.tot != null && data.elapsed != null)
                views7.setProgressBar(R.id.extraTimeBar, 100, (int) (data.elapsed.toFloat() / data.tot.toFloat() * 100), false);//s(((data.elapsed.hour * 3600) + (data.elapsed.min * 60) + data.elapsed.sec) / ((data.tot.hour * 3600) + (data.tot.min * 60) + data.tot.sec)) * 100, false);
            else views7.setProgressBar(R.id.extraTimeBar, 100, 1, false);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.updateAppWidget(appWidgetId, views2);
            appWidgetManager.updateAppWidget(appWidgetId, views3);
            appWidgetManager.updateAppWidget(appWidgetId, views4);
            appWidgetManager.updateAppWidget(appWidgetId, views5);
            appWidgetManager.updateAppWidget(appWidgetId, views6);
            appWidgetManager.updateAppWidget(appWidgetId, views7);
        }
        else {
            System.out.println("NOGOOD");

        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        System.out.println("update");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            if (context != null)
                updateAppWidget(context, appWidgetManager, appWidgetId, context.getSharedPreferences("WarpSettings",0));
        }
        System.out.println("Updating Clock");
        mHandler = new Handler();
        startRepeatingTask();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        Intent configIntent = new Intent(context, Configure.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.overlayButton, configPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        System.out.println("onDeleteddd");
        for (int appWidgetId : appWidgetIds) {
            //WarpClockConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
        stopRepeatingTask();
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //Intent i = new Intent(context, Settings.class);
        //context.startService(i);
        System.out.println("onENABLED!!");
        mHandler = new Handler();
        if (context != null) {
            set = context.getSharedPreferences("WarpSettings", 0);
            settings = new Settings(set);
            util = new Util(settings);
            startRepeatingTask();
        }
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        stopRepeatingTask();
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.overlayButton: {
                break;
            }
        }

    }
}

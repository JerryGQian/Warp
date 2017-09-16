package ninja.qian.warp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.widget.RemoteViews;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.databinding.*;

/**
 * Implementation of App Widget functionality.
 */
public class ClockWidget extends AppWidgetProvider {

    static Util util;
    static Settings settings;



    private int mInterval = 250; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private static Context context;
    private static AppWidgetManager appWidgetManager;
    private static int appWidgetId;
    private static SharedPreferences set;

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
    }
    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    void updateStatus() {
        updateAppWidget(context, appWidgetManager, appWidgetId, set);
    }

    static String daytimeToString(Util.Daytime dtime) {
        String secString = "";
        if (dtime.min >= 10)
            secString = Integer.toString(dtime.min);
        else if (dtime.min >= 0)
            secString = "0" + Integer.toString(dtime.min);
        return Integer.toString(dtime.hour % 13) + ":" + secString;
    }


    static void updateAppWidget(Context contex, AppWidgetManager appWidgetManage,
                                int appWidgetI, SharedPreferences se) {

        context = contex;
        appWidgetManager = appWidgetManage;
        appWidgetId = appWidgetI;
        set = se;

        settings = new Settings(set);
        util = new Util(settings);


        if (set == null || settings == null) return;

        Util.PrintData data = util.convert();
        System.out.println("SKADKLSADJSAJD " + data.t.hour);

        /*ImageSwitcher sw;
        sw = (ImageSwitcher) findViewById(R.id.imgsw);
        sw.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });
        */


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        views.setTextViewText(R.id.time_text, daytimeToString(data.t));

        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        views2.setTextViewText(R.id.ampm_text, data.t.ampm());

        RemoteViews views3 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        if (data.elapsed != null)views3.setTextViewText(R.id.elapsed_text, daytimeToString(data.elapsed));

        RemoteViews views4 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
        if (data.tot != null) views4.setTextViewText(R.id.total_text, daytimeToString(data.tot));

        RemoteViews views5 = new RemoteViews(context.getPackageName(), R.layout.clock_widget);

        if (data.sync) {
            //views5.setTextColor(R.id.synced_text, Color.GREEN);
            views5.setTextViewText(R.id.synced_text, ".");
        }
        else if (!data.sync) {
            //views5.setTextColor(R.id.synced_text, Color.YELLOW);
            views5.setTextViewText(R.id.synced_text, ".");
        }



        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId, views2);
        appWidgetManager.updateAppWidget(appWidgetId, views3);
        appWidgetManager.updateAppWidget(appWidgetId, views4);
        appWidgetManager.updateAppWidget(appWidgetId, views5);
        //appWidgetManager.updateAppWidget(appWidgetId, image);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, context.getSharedPreferences("WarpSettings",0));
        }
        System.out.println("Updating Clock");
        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //WarpClockConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //Intent i = new Intent(context, Settings.class);
        //context.startService(i);
        mHandler = new Handler();
        startRepeatingTask();
        set = context.getSharedPreferences("WarpSettings",0);
        settings = new Settings(set);
        util = new Util(settings);
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        stopRepeatingTask();
    }
}

package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;


import com.example.andrei.customlauncherdemo.R;

import backend.*;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WarpClockConfigureActivity WarpClockConfigureActivity}
 */
public class WarpClock extends AppWidgetProvider {

    static Util util;
    static Settings settings;

    static String daytimeToString(Util.Daytime dtime) {
        String secString = "";
        if (dtime.min >= 10)
            secString = Integer.toString(dtime.min);
        else if (dtime.min >= 0)
            secString = "0" + Integer.toString(dtime.min);
        return Integer.toString(dtime.hour % 12) + ":" + secString;
    }




    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, SharedPreferences set) {

        settings = new Settings(set);
        util = new Util(settings);

        CharSequence widgetText = WarpClockConfigureActivity.loadTitlePref(context, appWidgetId);
        Util.PrintData data = util.convert();
        System.out.println();
        //data.t = new Util.Daytime(5.5875f);
        //System.out.println(data.t.min);
        /*data.tot = "1:32:40";
        data.elapsed = "0:56:23";
        data.sync = false;
        */

        // Construct the RemoteViews object
        //String finalTime = "";
        String test = "3:38test";
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        views.setTextViewText(R.id.time_text, daytimeToString(data.t));

        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        views2.setTextViewText(R.id.ampm_text, data.t.ampm());

        RemoteViews views3 = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        if (data.elapsed != null)views3.setTextViewText(R.id.elapsed_text, daytimeToString(data.elapsed));

        RemoteViews views4 = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        if (data.tot != null) views4.setTextViewText(R.id.total_text, daytimeToString(data.tot));

        RemoteViews views5 = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        if (data.sync == null) views5.setTextViewText(R.id.synced_text, "N");
        else if (data.sync) views5.setTextViewText(R.id.synced_text, "S");
        else if (!data.sync) views5.setTextViewText(R.id.synced_text, "F");
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId, views2);
        appWidgetManager.updateAppWidget(appWidgetId, views3);
        appWidgetManager.updateAppWidget(appWidgetId, views4);
        appWidgetManager.updateAppWidget(appWidgetId, views5);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, context.getSharedPreferences("WarpSettings",0));
        }
        System.out.println("Updating Clock");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WarpClockConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        //Intent i = new Intent(context, Settings.class);
        //context.startService(i);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


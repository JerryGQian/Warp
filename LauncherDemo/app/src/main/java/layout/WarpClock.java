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
        views.setTextViewText(R.id.appwidget_text, test);

        RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.warp_clock);
        views2.setTextViewText(R.id.appwidget_text, daytimeToString(data.t));
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.updateAppWidget(appWidgetId, views2);
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


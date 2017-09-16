package layout;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.andrei.customlauncherdemo.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WarpClockConfigureActivity WarpClockConfigureActivity}
 */
public class WarpClock extends AppWidgetProvider {

    public static class PrintData {
        Daytime t;
        Daytime tot;
        Daytime elapsed;
        Boolean sync;
    }

    public static class Daytime {
        int hour;
        int min;
        int sec;
        int mil;

        public Daytime(float t) {
            while (t > 24) t -= 24;
            hour = (int) t;
            min = (int) ((t - hour) * 60);
            sec = (int)(t - hour) - (min / 60) * 60 * 60;
        }

        public int getModHour() {
            return hour % 12;
        }

        public float toFloat() {
            return hour + (min / 60) + (sec / 60 / 60);
        }

        public String ampm() {
            if (hour < 12) return "AM";
            return "PM";
        }

        @Override
        public int hashCode() {
            return hour * 10000 + min * 100 + sec;
        }
    }

    static String daytimeToString(Daytime dtime) {
        String secString = "";
        if (dtime.min >= 10)
            secString = Integer.toString(dtime.min);
        else if (dtime.min >= 0)
            secString = "0" + Integer.toString(dtime.min);
        return Integer.toString(dtime.hour) + ":" + secString;
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WarpClockConfigureActivity.loadTitlePref(context, appWidgetId);
        PrintData data = new PrintData();//util.convert();
        data.t = new Daytime(5.5875f);
        System.out.println(data.t.min);
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
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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
        /*Settings settings = new Settings();
        Util util = new Util(settings);

         */
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


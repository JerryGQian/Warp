package ninja.qian.warp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class LockScreenActivity extends Activity {
    private int mInterval = 250; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private static SharedPreferences set;
    private Util.PrintData data;
    private TextView timeText;
    private TextView elapsedText;
    private TextView totText;
    private TextView ampmText;
    private TextView syncText;
    private TextView slashText;
    private ProgressBar extraTimeBar;

    static Util util;
    static Settings settings;



    static String daytimeToString(Util.Daytime dtime) {
        String secString = "";
        if (dtime.min >= 10)
            secString = Integer.toString(dtime.min);
        else if (dtime.min >= 0)
            secString = "0" + Integer.toString(dtime.min);
        return Integer.toString(dtime.hour % 12 == 0 ? 12 : dtime.hour % 12) + ":" + secString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("EQWEQWE" + getSharedPreferences("WarpSettings",0));
        settings = new Settings(getSharedPreferences("WarpSettings",0));
        util = new Util(settings);
        //Set up our Lockscreen
        makeFullScreen();
        data = util.convert();
        startService(new Intent(this, LockscreenService.class));
        System.out.println("1");
        setContentView(R.layout.activity_lockscreen);
        mHandler = new Handler();
        System.out.println("2");
        System.out.println("3");
        timeText = (TextView) findViewById(R.id.time_text);
        elapsedText = (TextView) findViewById(R.id.elapsed_text);
        totText = (TextView) findViewById(R.id.total_text);
        ampmText = (TextView) findViewById(R.id.ampm_text);
        syncText = (TextView) findViewById(R.id.synced_text);
        slashText = (TextView) findViewById(R.id.slash_text);
        extraTimeBar  = (ProgressBar) findViewById(R.id.extraTimeBar);
        startRepeatingTask();
        //daytimeToString(data.t);
        System.out.println("4");
    }


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
        //updateAppWidget(context, appWidgetManager, appWidgetId, set);
        if (data.t != null) {
            timeText.setText(daytimeToString(data.t) );
            System.out.println("time is updating!");
        }
        if (data.tot != null) {
            timeText.setTextColor(Color.parseColor("#22ffffff"));
        } else if (data.tot == null) {
            timeText.setTextColor(Color.parseColor("#ffffffff"));
        }
        if (data.t != null) ampmText.setText(data.t.ampm() );

        if (data.elapsed != null) elapsedText.setText(daytimeToString(data.elapsed));
        else if (data.elapsed == null) elapsedText.setText("- : --");

        if (data.tot != null) totText.setText(daytimeToString(data.tot));
        else if (data.tot == null) totText.setText("- : --");

        //slashText.setText(R.id.slash_text, "/");

        syncText.setText(".");
        if (data.sync) { //sets color of dot
            syncText.setTextColor(Color.GREEN);
        }
        else if (!data.sync) {
            syncText.setTextColor(Color.YELLOW);
        }

        // Progress bar
        //ProgressBar bar = (ProgressBar)findViewById(R.id.extraTimeBar);
        if (data.tot != null && data.elapsed != null) extraTimeBar.setProgress((((data.elapsed.hour*3600)+(data.elapsed.min*60)+data.elapsed.sec) / ((data.tot.hour*3600)+(data.tot.min*60)+data.tot.sec)) * 100);
        else extraTimeBar.setProgress(1);
        //update clock stuff
    }

    /**
     * A simple method that sets the screen to fullscreen.  It removes the Notifications bar,
     *   the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        return; //Do nothing!
    }

    public void unlockScreen(View view) {
        //Instead of using finish(), this totally destroys the process
        finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }
}
package ninja.qian.warp;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Configure extends Activity implements View.OnClickListener {
    Settings settings;
    private SeekBar seekh1;
    private SeekBar seekh2;
    private SeekBar seekh1N;
    private SeekBar seekh2N;
    private TextView texth1;
    private TextView texth2;
    private TextView texth1N;
    private TextView texth2N;

    private Button extendButton;
    private Button stretchButton;
    private Button smoothButton;


    class SeekBarListener implements OnSeekBarChangeListener {
        TextView txt;
        int progress = 0;
        Settings settings;
        String type;
        public SeekBarListener(TextView tv, Settings set, String type) {
            txt = tv;
            settings = set;
            this.type = type;
        }
        @Override public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
            progress = progressValue;
            txt.setText("" + (progress / 4.0f));
            if (settings == null) return;
            Settings.Handles hand = null;
            switch (settings.mode) {
                case "Extend": {
                    hand = settings.hExtend;
                    break;
                }
                case "Stretch": {
                    hand = settings.hStretch;
                    break;
                }
                case "Smooth": {
                    hand = settings.hSmooth;
                    break;
                }
            }
            float val = (progress / 4.0f);
            switch (type) {
                case "h1": hand.h1 = val; break;
                case "h2": hand.h2 = val; break;
                case "h1N": hand.h1N = val; break;
                case "h2N": hand.h2N = val; break;
            }
            settings.save();
        }
        @Override public void onStopTrackingTouch(SeekBar seekBar) {
            txt.setText("" + (progress / 4.0f));
            if (settings == null) return;
            Settings.Handles hand;
            switch (settings.mode) {
                case "Extend":
                    hand = settings.hExtend;
                    break;
                case "Stretch":
                    hand = settings.hStretch;
                    break;
                case "Smooth":
                    hand = settings.hSmooth;
                    break;
                default:
                    hand = settings.hExtend;
                    break;
            }
            float val = (progress / 4.0f);
            switch (type) {
                case "h1": hand.h1 = val; break;
                case "h2": hand.h2 = val; break;
                case "h1N": hand.h1N = val; break;
                case "h2N": hand.h2N = val; break;
            }
            settings.save();
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
        settings = new Settings(getSharedPreferences("WarpSettings", 0));


        seekh1 = (SeekBar) findViewById(R.id.seekh1);
        seekh2 = (SeekBar) findViewById(R.id.seekh2);
        seekh1N = (SeekBar) findViewById(R.id.seekh1N);
        seekh2N = (SeekBar) findViewById(R.id.seekh2N);
        texth1 = (TextView) findViewById(R.id.labelh1);
        texth2 = (TextView) findViewById(R.id.labelh2);
        texth1N = (TextView) findViewById(R.id.labelh1N);
        texth2N = (TextView) findViewById(R.id.labelh2N);

        extendButton = (Button) findViewById(R.id.extend_button);
        stretchButton = (Button) findViewById(R.id.stretch_button);
        smoothButton = (Button) findViewById(R.id.smooth_button);

        switch(settings.mode) {
            case "Extend": extendButton.setText("(Extend)"); break;
            case "Stretch": stretchButton.setText("(Stretch)"); break;
            case "Smooth": smoothButton.setText("(Synced)"); break;
        }

        seekh1.setOnSeekBarChangeListener(new SeekBarListener(texth1, settings, "h1"));
        seekh2.setOnSeekBarChangeListener(new SeekBarListener(texth2, settings, "h2"));
        seekh1N.setOnSeekBarChangeListener(new SeekBarListener(texth1N, settings, "h1N"));
        seekh2N.setOnSeekBarChangeListener(new SeekBarListener(texth2N, settings, "h2N"));

        onClick(null);

    }

    public void onClick(View v) {
        Settings.Handles hand = null;
        switch (settings.mode) {
            case "Extend":
                hand = settings.hExtend;
                break;
            case "Stretch":
                hand = settings.hStretch;
                break;
            case "Smooth":
                hand = settings.hSmooth;
                break;
            default:
                hand = settings.hExtend;
                break;
        }
        if (v != null) {
            final int id = v.getId();
            switch (id) {
                case R.id.extend_button:
                    settings.mode = "Extend";
                    extendButton.setText("(Extend)");
                    stretchButton.setText(" Stretch ");
                    smoothButton.setText("Synced");
                    break;
                case R.id.stretch_button:
                    settings.mode = "Stretch";
                    extendButton.setText("Extend");
                    stretchButton.setText("(Stretch)");
                    smoothButton.setText("Synced");
                    break;
                case R.id.smooth_button:
                    settings.mode = "Smooth";
                    extendButton.setText("Extend");
                    stretchButton.setText(" Stretch ");
                    smoothButton.setText("(Synced)");
                    break;
                case R.id.syncbutton: {
                    hand.h1N = hand.h1;
                    hand.h2N = hand.h2;
                    break;
                }
                case R.id.exitbutton: {
                    finish();
                    break;
                }
            }
        }
        settings.save();

        texth1 .setText("" + hand.h1 );
        texth2 .setText("" + hand.h2 );
        texth1N.setText("" + hand.h1N);
        texth2N.setText("" + hand.h2N);

        seekh1.setProgress((int) (4 * hand.h1));
        seekh2.setProgress((int) (4 * hand.h2));
        seekh1N.setProgress((int)(4 * hand.h1N));
        seekh2N.setProgress((int)(4 * hand.h2N));

    }
}

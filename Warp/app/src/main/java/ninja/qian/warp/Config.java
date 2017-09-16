package ninja.qian.warp;

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

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Config extends AppCompatActivity implements View.OnClickListener  {

    Settings settings;
    private SeekBar seekh1;
    private SeekBar seekh2;
    private SeekBar seekh1N;
    private SeekBar seekh2N;
    private TextView texth1;
    private TextView texth2;
    private TextView texth1N;
    private TextView texth2N;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    class SeekBarListener implements OnSeekBarChangeListener {
        TextView txt;
        int progress = 0;
        public SeekBarListener(TextView tv) {txt = tv;}
        @Override public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) { progress = progressValue;}
        @Override public void onStopTrackingTouch(SeekBar seekBar) { texth1.setText("" + (progress / 4.0f));}
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjust_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        seekh1 = (SeekBar) findViewById(R.id.seekh1);
        seekh2 = (SeekBar) findViewById(R.id.seekh2);
        seekh1N = (SeekBar) findViewById(R.id.seekh1N);
        seekh2N = (SeekBar) findViewById(R.id.seekh2N);
        texth1 = (TextView) findViewById(R.id.labelh1);
        texth2 = (TextView) findViewById(R.id.labelh2);
        texth1N = (TextView) findViewById(R.id.labelh1N);
        texth2N = (TextView) findViewById(R.id.labelh2N);

        SeekBarListener sbl = new SeekBarListener(texth1);
        seekh1.setOnSeekBarChangeListener(sbl);

        /*seekh2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) { progress = progressValue;}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { texth2.setText("" + (progress / 4.0f));}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        });

        seekh1N.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) { progress = progressValue;}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { texth1N.setText("" + (progress / 4.0f));}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        });

        seekh2N.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) { progress = progressValue;}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { texth2N.setText("" + (progress / 4.0f));}
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        });*/



        settings = new Settings(getSharedPreferences("WarpSettings", 0));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.adjust_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    AdjustTab atb = new AdjustTab();
                    atb.setSettings(settings);
                    return atb;
                case 1:
                    AdjustTab tb = new AdjustTab();
                    tb.setSettings(settings);
                    return tb;
                case 2:
                    AdjustTab t = new AdjustTab();
                    t.setSettings(settings);
                    return t;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Time Settings";
                case 1:
                    return "Alarms";
                case 2:
                    return "Settings";
            }
            return null;
        }
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.extend_button:
                settings.mode = "Extend";
                break;
            case R.id.stretch_button:
                settings.mode = "Stretch";
                break;
            case R.id.smooth_button:
                settings.mode = "Smooth";
                break;
        }
        settings.save();
    }
}

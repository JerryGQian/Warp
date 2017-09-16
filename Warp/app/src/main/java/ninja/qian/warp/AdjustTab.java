package ninja.qian.warp;

/**
 * Created by gary on 9/16/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class AdjustTab extends Fragment implements View.OnClickListener {

    Settings settings;

    public void setSettings(Settings set) {
        settings = set;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adjust_tab, container, false);

        return rootView;
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

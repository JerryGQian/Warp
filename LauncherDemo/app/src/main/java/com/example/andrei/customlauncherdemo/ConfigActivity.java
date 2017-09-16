package com.example.andrei.customlauncherdemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import backend.*;

public class ConfigActivity extends Activity {

    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config3);

        settings = new Settings(getSharedPreferences("WarpSettings", 0));
    }
}

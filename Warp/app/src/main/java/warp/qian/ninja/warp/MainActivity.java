package warp.qian.ninja.warp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button button = (Button) findViewById(R.id.buttom);
        button.setOnClickListener(new View.OnClickListener()
            @Override
            public void onClick (View v){
                Intent intent = new Intent(getApplicationContext(), AppsListActivity.class);
                starActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}

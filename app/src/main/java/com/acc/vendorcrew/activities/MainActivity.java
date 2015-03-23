package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.acc.vendorcrew.R;


public class MainActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static int SPLASH_TIME_OUT = 5000;
    TextView text;
    boolean isLoggedIn;

    private SharedPreferences preferences;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        Typeface custom_font_regular = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Reg.ttf");
        Typeface custom_font_bold = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        isLoggedIn = preferences.getBoolean("logIN",false);

        text.setTypeface(custom_font_bold);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer and displaying website name.
             */
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app next activity
                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

                if(hasLoggedIn){
                    Intent i = new Intent(MainActivity.this, AddVendorCategoryActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}

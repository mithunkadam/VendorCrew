package com.acc.vendorcrew.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.adapters.BrandImageAdapter;


public class AddBrandsActivity extends Activity {

    private GridView gridView;
    private BrandImageAdapter brandImageAdapter;
    private TextView textView;
    private ImageView imageView;

    private Context mContext;

    final int imageList[]={
            R.drawable.electonics,
            R.drawable.telecom,
            R.drawable.finance,
            R.drawable.medical,
            R.drawable.groceries,
            R.drawable.banks,
            R.drawable.mobiles,
            R.drawable.computers,
            R.drawable.other};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brands);

        mContext = this;

        ActionBar mActionBar = getActionBar();
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        gridView = (GridView) findViewById(R.id.gridView);
        textView = (TextView) findViewById(R.id.textView2);
        imageView  = (ImageView) findViewById(R.id.imageView3);

        Typeface custom_font = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");
        textView.setTypeface(custom_font);

        String title = getIntent().getExtras().getString("VENDOR_NAME");
        int imageId = getIntent().getExtras().getInt("RESOURCE_ID");

        textView.setText(title);
        imageView.setBackgroundResource(imageList[imageId]);

        int[] vendorId = {R.drawable.logo_sony , R.drawable.logo_samsung, R.drawable.logo_philips,
                R.drawable.logo_toshiba , R.drawable.logo_lg , R.drawable.logo_sharp,
                R.drawable.logo_onida , R.drawable.logo_videocon , R.drawable.logo_mitashi};

        brandImageAdapter = new BrandImageAdapter(AddBrandsActivity.this , vendorId);
        gridView.setAdapter(brandImageAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_vendor_category, menu);
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

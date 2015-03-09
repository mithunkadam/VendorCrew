package com.acc.vendorcrew.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.adapters.VendorImageAdapter;


public class AddVendorCategoryActivity extends Activity {

    private Context mContext;
    TextView browse;
    String[] web = {
            "Electronics",
            "Telecom",
            "Finance",
            "Medical",
            "Groceries",
            "Banks",
            "Mobiles",
            "Computers",
            "Others"
    };

    int[] imageId = {
            R.drawable.electonics,
            R.drawable.telecom,
            R.drawable.finance,
            R.drawable.medical,
            R.drawable.groceries,
            R.drawable.banks,
            R.drawable.mobiles,
            R.drawable.computers,
            R.drawable.other
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor_category);

        mContext = this;

        ActionBar mActionBar = getActionBar();
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);

        ImageView imageView1 = (ImageView)findViewById(R.id.imageView2);
        imageView1.setBackgroundResource(R.drawable.browse);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        browse = (TextView) findViewById(R.id.category_id);
        Typeface custom_font = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");
        browse.setTypeface(custom_font);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new VendorImageAdapter(this , web , imageId));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext, AddBrandsActivity.class);
                i.putExtra("VENDOR_NAME" , web[position]);
                i.putExtra("RESOURCE_ID",position);
                startActivity(i);
            }
        });
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

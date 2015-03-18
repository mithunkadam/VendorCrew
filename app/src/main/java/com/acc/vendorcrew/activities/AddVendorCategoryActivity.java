package com.acc.vendorcrew.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.adapters.VendorImageAdapter;
import com.acc.vendorcrew.json.JSONParser;
import com.getbase.floatingactionbutton.FloatingActionButton;


public class AddVendorCategoryActivity extends Activity {

    JSONParser jParser;
    private Context mContext;
    TextView browse;
    GridView gridView;


    String[] web = {
            "Electronics",
            "Telecom",
            "Finance",
            "Medical",
            "Groceries",
            "Banks",
            "Mobiles",
            "Computers",
            "Others",
            "Air Conditioning",
            "Broadband Services",
            "Colleges",
            "Consumer Goods",
            "Credit Cards",
            "Dry Cleaning",
            "Fast Food",
            "Fitness & Gym",
            "Florist",
            "Furniture",
            "Hospitals",
            "Insurance",
            "Magazines & Periodicals",
            "Mobile Shoppee",
            "Newspaper",
            "PUC",
            "Pastries",
            "Personal Care",
            "Plumbing & Electricals",
            "Retail",
            "Schools",
            "Service Station 2W",
            "Service Station 4W",
            "Sports Center",
            "Stationary, Gift & Articles",
            "Stock Trading",
            "Taxi & Radio Cabs",
            "Transport - Air",
            "Transport - Bus",
            "Transport - Rail",
            "Utilities - Others",
            "Utilities - Power"
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
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
            R.drawable.other,
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

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.setter);
//        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setColorNormalResId(R.color.orange);
        button.setColorPressedResId(R.color.orange_pressed);
        button.setIcon(R.drawable.ic_add186);
        button.setStrokeVisible(false);

        browse = (TextView) findViewById(R.id.category_id);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "font/ProximaNova-Bold.ttf");
        browse.setTypeface(custom_font);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new VendorImageAdapter(this , web , imageId));
//        new GetData().execute();

//        gridView.setAdapter(new VendorImageAdapter(mContext, new ArrayList<CategoriesModel>(), imageId));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(mContext, AddBrandsActivity.class);
                i.putExtra("VENDOR_NAME", web[position]);
                i.putExtra("RESOURCE_ID", position);
                startActivity(i);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_vendor_category, menu);


        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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
/*
    class GetData extends AsyncTask<String, String, ArrayList<Object>> {
        JSONObject json = null;
        String str = "";
        HttpResponse response;


        @Override
        protected void onPostExecute(ArrayList<Object> objects) {
            super.onPostExecute(objects);


            try {
                JSONArray jArray = new JSONArray(str);
                json = jArray.getJSONObject(0);

                CategoriesModel c = new CategoriesModel();
                c.set_id(json.getString("_id"));
                c.setName(json.getString("name"));
                c.setCategoryId(json.getInt("categoryid"));
                c.setImagePath(json.getString("imagepath"));

                gridView.setAdapter(new VendorImageAdapter(mContext, new ArrayList<CategoriesModel>(), imageId));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent i = new Intent(mContext, AddBrandsActivity.class);
                 i.putExtra("VENDOR_NAME", web[position]);
                 i.putExtra("RESOURCE_ID", position);
                 startActivity(i);
            }
        });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<Object> doInBackground(String... params) {

            HttpClient myClient = new DefaultHttpClient();
            HttpGet myConnection = new HttpGet(Constant.CATEGORIES_URL);

            try {
                response = myClient.execute(myConnection);
                str = EntityUtils.toString(response.getEntity(), "UTF-8");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/
}
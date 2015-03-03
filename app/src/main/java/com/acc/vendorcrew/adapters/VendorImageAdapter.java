package com.acc.vendorcrew.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.acc.vendorcrew.R;

/**
 * Created by Sagar on 3/2/2015.
 */
public class VendorImageAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] web;
    private final int[] imageid;

    public VendorImageAdapter(Context context, String[] web, int[] Imageid) {
        mContext = context;
        this.imageid = Imageid;
        this.web = web;
    }

    @Override
    public int getCount() {
        return web.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets() , "font/ProximaNova-Reg.ttf");
            textView.setTypeface(custom_font);
            textView.setText(web[position]);
            imageView.setImageResource(imageid[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;

    };
}

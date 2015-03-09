package com.acc.vendorcrew.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acc.vendorcrew.R;

/**
 * Created by Sagar on 3/3/2015.
 */
public class BrandImageAdapter extends BaseAdapter{
    Context mContext;
    int[] brandId;

    public BrandImageAdapter(Context context, int[] brandId){
        mContext = context;
        this.brandId = brandId;
    }
    @Override
    public int getCount() {
        return brandId.length;
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
            grid = inflater.inflate(R.layout.brand_grid, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.imageView4);
            imageView.setImageResource(brandId[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;

    }
}

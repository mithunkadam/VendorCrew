package com.acc.vendorcrew.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.model.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

/*
public class VendorImageAdapter extends ArrayAdapter<CategoriesModel>{

    private List<CategoriesModel> categoriesModels;
    private int[] imageid;
    private Context mContext;

    public VendorImageAdapter(Context context, List<CategoriesModel> cm , int [] imageid) {
        super(context, 0 ,cm);
        this.mContext = context;
        this.categoriesModels = cm;
        this.imageid = imageid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoriesModel categoriesModel = this.getItem(position);
        View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
        } else {
            grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets() , "font/ProximaNova-Reg.ttf");
        textView.setTypeface(custom_font);
        textView.setText(categoriesModel.getName());
        imageView.setImageResource(imageid[position]);

        return grid;
    }
}*/
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
        } else {
            grid = (View) convertView;
        }
        TextView textView = (TextView) grid.findViewById(R.id.grid_text);
        ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);

        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets() , "font/ProximaNova-Bold.ttf");
        textView.setTypeface(custom_font);
        textView.setText(web[position]);
        imageView.setImageResource(imageid[position]);

        return grid;

    };


}

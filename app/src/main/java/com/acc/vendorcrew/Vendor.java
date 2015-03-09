package com.acc.vendorcrew;

import android.app.Application;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;

import java.io.IOException;

/**
 * Created by Sagar on 3/9/2015.
 */
public class Vendor extends Application {
    private Manager manager;
    private Database database;
    private static final String DATABASE_NAME = "vendorcrew";

    private void initDatabase(){
        try {
            manager = new Manager(new AndroidContext(getApplicationContext()), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            database = manager.getDatabase(DATABASE_NAME);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDatabase();
    }

    public Database getDatabase() {
        return this.database;
    }

    public void setDatabase(){
        try{
            database = manager.getDatabase(DATABASE_NAME);
        }catch (CouchbaseLiteException e){
            e.printStackTrace();
        }
    }

}

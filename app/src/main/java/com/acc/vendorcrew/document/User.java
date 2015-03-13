package com.acc.vendorcrew.document;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sagar on 3/9/2015.
 */
public class User {
    private static final String DOC_TYPE = "user";
    private static final String VIEW_NAME = "users";

    public static Document createUser(Database database, String email, String name , String pass , String contact , String external_id , String updateTime)
            throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("type", DOC_TYPE);
        properties.put("name", name);
        properties.put("email",email);
        properties.put("password",pass);
        properties.put("mobile_number",contact);
        properties.put("created_at", currentTimeString);
        properties.put("external_id",external_id);
        properties.put("updated_at",updateTime);

        Document document = database.createDocument();
        document.putProperties(properties);

        return document;
    }
}

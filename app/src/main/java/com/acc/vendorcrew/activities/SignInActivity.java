package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.activities.constant.Constant;
import com.acc.vendorcrew.connectivity.ConnectionDetector;
import com.acc.vendorcrew.json.JSONParser;
import com.acc.vendorcrew.model.SignUpModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends Activity {

    Button signUp, signIn;
    EditText email, password;
    ArrayList<SignUpModel> userList;
    TextView Ok;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;

    private JSONParser jParser;
    JSONArray jsonArray = null;
    //final String TAG = "CouchbaceDB";
    //private Manager manager;
    //Database database;
    // create a name for the database and make sure the name is legal
    //String dbname = "cms_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signUp = (Button) findViewById(R.id.sign_up);
        signIn = (Button) findViewById(R.id.sign_in);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MTLogin().execute();

                if (isInternetPresent) {

                    new MTLogin().execute();

                } else {

                    // create an object that contains data for a document
                    Map<String, Object> docContent = new HashMap<String, Object>();
                    docContent.put("LOGINID", email.getText().toString());
                    docContent.put("PASSWORD", password.getText().toString());
                    // display the data for the new document
             /*       Log.d(TAG, "docContent=" + String.valueOf(docContent));
                    // create an empty document
                    Document document = database.createDocument();
                    // add content to document and write the document to the
                    // database
                    try {
                        document.putProperties(docContent);
                        Log.d(TAG, "Document written to database named " + dbname
                                + " with ID = " + document.getId());
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Cannot write document to database", e);
                    }
                    // save the ID of the new document
                    String docID = document.getId();

                    // retrieve the document from the database
                    Document retrievedDocument = database.getDocument(docID);
                    // display the retrieved document
                    Log.d(TAG,"retrievedDocument="+ String.valueOf(retrievedDocument.getProperties()));
            */
                    Toast.makeText(getBaseContext(), " Internet Connection Failed", Toast.LENGTH_SHORT).show();
                }

			/*
			 * Intent intent = new Intent(LoginActivity.this,
			 * HomeActivity.class); startActivity(intent);
			 */

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    class MTLogin extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;
        String response;
        String is_logged;
        String username;
        String code;
        String loginID;
        String result;
        String message;
        int flag = 0;

        private String webAddressToPost;

        @Override
        protected ArrayList<Object> doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Loging...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

			/*
			 * TelephonyManager TelephonyMgr =
			 * (TelephonyManager)getSystemService(TELEPHONY_SERVICE); String
			 * m_deviceId = TelephonyMgr.getDeviceId();
			 */

			/*
			 * String m_androidId = Secure.getString(getContentResolver(),
			 * Secure.ANDROID_ID);
			 *
			 * System.out.println("Device ID " + m_androidId);
			 */
            webAddressToPost = Constant.LOGIN_URL + Constant.LOGIN_EMAIL_ID + email.getText().toString() + "&" + Constant.LOGIN_PASS + password.getText().toString();

        }
    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
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
package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.connectivity.ConnectionDetector;
import com.acc.vendorcrew.constant.Constant;
import com.acc.vendorcrew.json.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ValidateUserActivity extends Activity {

    public static final String PREFS_NAME = "RegisterdPrefs";
    EditText uPassword, uPin;
    TextView errorPassword, errorPin;
    Button submit;
    String password, pin, registerdUserID;
    JSONParser jParser;
    JSONArray jsonArray = null;
    boolean isInternetPresent = false;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_user);

        uPassword = (EditText) findViewById(R.id.valid_password);
        uPin = (EditText) findViewById(R.id.valid_pin);

        errorPassword = (TextView) findViewById(R.id.password_error_msg);
        errorPin = (TextView) findViewById(R.id.pin_error_msg);

        submit = (Button) findViewById(R.id.continue_btn);

        password = uPassword.getText().toString();
        pin = uPin.getText().toString();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        registerdUserID = settings.getString("registeredUser", "");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUserInfo()) {
                    if (isInternetPresent) {
                        new MTValidate().execute();
                    } else {
                        Toast.makeText(getBaseContext(), "You don't have internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private boolean isValidUserInfo(){
        boolean bpassword, bpin;

        final String upassword = uPassword.getText().toString();
        if (!isValidPassword(upassword)) {
            errorPassword.setVisibility(View.VISIBLE);
            bpassword = false;
        }else {
            errorPassword.setVisibility(View.GONE);
            bpassword = true;
        }

        final String upin = uPin.getText().toString();
        if (!isValidPin(upin)) {
            errorPin.setVisibility(View.VISIBLE);
            bpin = false;
        }else {
            errorPin.setVisibility(View.GONE);
            bpin = true;
        }
        if(bpassword && bpin){
            return true;
        }else {
            return false;
        }
    }

    private boolean isValidPassword(String uPassword) {
        if(uPassword.equals("") || uPassword.length()<7){
            return false;
        }else{
            return true;
        }
    }

    private boolean isValidPin(String uPin) {
        if(uPin.equals("") || uPin.length()<6){
            return false;
        }else{
            return true;
        }
    }

    class MTValidate extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;
        String response;
        String is_logged;
        String username;
        String code;

        String webAddressToPost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ValidateUserActivity.this);
            pDialog.setMessage("Validating user...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            webAddressToPost = Constant.VALIDATE_URL
                    + Constant.VALIDATION_ACCOUNT_ID + registerdUserID + "&"
                    + Constant.VALIDATION_PASSWORD + uPassword.getText().toString() + "&"
                    + Constant.VALIDATE_PIN + uPin.getText().toString();

            System.out.println("Response in onPreExecute " + webAddressToPost);
        }

        @Override
        protected ArrayList<Object> doInBackground(String... arg0) {

            jParser = new JSONParser();

            ArrayList<Object> jsnObject = jParser.getJSONFromUrl(webAddressToPost);

            return jsnObject;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> result) {

            String StatusCode = result.get(1).toString();

            System.out.println("Response in validation " + StatusCode);

            try {
                JSONObject jsnObject = (JSONObject) result.get(0);

                if (StatusCode.equals("200")) {
                    String result1 = jsnObject.getString("result");

                    Intent intent = new Intent(ValidateUserActivity.this, AddVendorCategoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    System.out.println("Response in onPostExecute" + result1);
                } else {
                    System.out.println("Response in registration else "+ jsnObject);

					String message = jsnObject.getString("error");
					Toast.makeText(ValidateUserActivity.this, "" + message,Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_validate_user, menu);
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
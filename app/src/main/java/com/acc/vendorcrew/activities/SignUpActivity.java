package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
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

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends Activity {

    private Button signUp;
    private EditText name, email, password, mobNo;
    private TextView errorName, errorEmail, errorPassword, errorMobileNo, login;

    private Boolean isInternetPresent = false;
    private JSONParser jParser;
    JSONArray jsonArray = null;
    public static final String PREFS_NAME = "RegisterPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SpannableString str = new SpannableString("Login Here");
        str.setSpan(new UnderlineSpan(), 0, str.length(), Spanned.SPAN_PARAGRAPH);
        login = (TextView) findViewById(R.id.login_here);
        login.setText(str);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mobNo = (EditText) findViewById(R.id.mob_no);

        errorName = (TextView) findViewById(R.id.name_error);
        errorEmail = (TextView) findViewById(R.id.email_error);
        errorPassword = (TextView) findViewById(R.id.password_error);
        errorMobileNo = (TextView) findViewById(R.id.mob_no_error);

//        login = (TextView) findViewById(R.id.login_here);

        signUp = (Button) findViewById(R.id.sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(getBaseContext());
                if (isValidUserInfo()) {
                    new MTRegistration().execute();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });

    }

    private boolean isValidUserInfo(){
        boolean bname, bemail, bpassword, bmobno;
        final String uName = name.getText().toString();
        if (!isValidName(uName)) {
            errorName.setVisibility(View.VISIBLE);
            bname = false;
        }else {
            errorName.setVisibility(View.GONE);
            bname = true;
        }

        final String uEmail = email.getText().toString();
        if (!isValidEmail(uEmail)) {
            errorEmail.setVisibility(View.VISIBLE);
            bemail = false;
        }else {
            errorEmail.setVisibility(View.GONE);
            bemail = true;
        }

        final String uPassword = password.getText().toString();
        if (!isValidPassword(uPassword)) {
            errorPassword.setVisibility(View.VISIBLE);
            bpassword = false;
        }else {
            errorPassword.setVisibility(View.GONE);
            bpassword = true;
        }

        final String uMobNo = mobNo.getText().toString();
        if (!isValidMobNo(uMobNo)) {
            errorMobileNo.setVisibility(View.VISIBLE);
            bmobno = false;
        }else {
            errorMobileNo.setVisibility(View.GONE);
            bmobno = true;
        }

        if(bname && bemail && bpassword && bmobno){
            return true;
        }else {
            return false;
        }
    }

    private boolean isValidName(String uName) {
        if(uName.equals("")){
            return false;
        }else {
            return true;
        }
    }

    private boolean isValidEmail(String uEmail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(uEmail);
        return matcher.matches();
    }

    private boolean isValidPassword(String uPassword) {
        if(uPassword.length()<7){
            return false;
        }else{
            return true;
        }
    }

    private boolean isValidMobNo(String uMobNo) {
        if(uMobNo.equals("") || uMobNo.length()<12){
            return false;
        }else{
            return true;
        }
    }


    class MTRegistration extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;
        String response;
        String is_logged;
        String username;
        String code;

        String webAddressToPost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage("Registering user...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String uEmail = email.getText().toString();
            String uPassword = password.getText().toString();
            String uMobNo = mobNo.getText().toString();

		    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String m_deviceId = telephonyManager.getDeviceId();

            webAddressToPost = Constant.REGISTRATION_URL
                    + Constant.REGISTRATION_EMAIL_ID + uEmail + "&"
                    + Constant.REGISTRATION_PASS + uPassword + "&"
                    + Constant.REGISTRATION_MOBILE_NUMBER + uMobNo + "&"
                    + Constant.REGISTRATION_DEVICE_ID + m_deviceId;

            System.out.println("Response in onPreExecute " + webAddressToPost);
        }

        @Override
        protected ArrayList<Object> doInBackground(String... arg0) {

            jParser = new JSONParser();
            ArrayList<Object> jsnObject = jParser.getJSONFromUrl(webAddressToPost);

            System.out.println("Response in doInBackground " + jsnObject);

            return jsnObject;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> result) {

            String StatusCode = result.get(1).toString();
            System.out.println("Response in registration" + StatusCode);

            try {

                JSONObject jsnObject = (JSONObject) result.get(0);

                if (StatusCode.equals("201")) {
                    String registrationID = null;
                    try {
                        registrationID = jsnObject.getString(Constant.REGISTRATION_ID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("registeredUser", registrationID);
                    editor.commit();

                    Intent intent = new Intent(SignUpActivity.this, AddVendorCategoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {

                    System.out.println("Response in registration else "+ jsnObject);

                        String message = jsnObject.getString("error");
                        Toast.makeText(SignUpActivity.this, "" + message, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();
        }
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
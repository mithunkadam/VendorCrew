package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.Vendor;
import com.acc.vendorcrew.connectivity.ConnectionDetector;
import com.acc.vendorcrew.constant.Constant;
import com.acc.vendorcrew.document.User;
import com.acc.vendorcrew.json.JSONParser;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends Activity {

    private String TAG = "Response";
    private Button signUp;
    private EditText name, email, password, mobNo;
    private TextView errorName, errorEmail, errorPassword, errorMobileNo, login, register_user, header_text, bottom_text;

    private Context mContext = this;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    public static final String PREFS_NAME = "RegisterPrefs";

    private Database getDatabase() {
        Vendor vendor = (Vendor) getApplication();
        return vendor.getDatabase();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface custom_font_regular = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");
        Typeface custom_font_bold = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Reg.ttf");

        SpannableString str = new SpannableString("Login Here");
        str.setSpan(new UnderlineSpan(), 0, str.length(), Spanned.SPAN_PARAGRAPH);
        login = (TextView) findViewById(R.id.login_here);
        login.setTypeface(custom_font_bold);
        login.setText(str);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        mobNo = (EditText) findViewById(R.id.mob_no);

        mobNo.setText(GetCountryZipCode()+" ");
        mobNo.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        name.setTypeface(custom_font_regular);
        email.setTypeface(custom_font_regular);
        password.setTypeface(custom_font_regular);
        mobNo.setTypeface(custom_font_regular);

        errorName = (TextView) findViewById(R.id.name_error);
        errorEmail = (TextView) findViewById(R.id.email_error);
        errorPassword = (TextView) findViewById(R.id.password_error);
        errorMobileNo = (TextView) findViewById(R.id.mob_no_error);
        register_user = (TextView) findViewById(R.id.register_user);
        header_text = (TextView) findViewById(R.id.header_text);
        bottom_text = (TextView) findViewById(R.id.bottom_text);


        errorName.setTypeface(custom_font_regular);
        errorEmail.setTypeface(custom_font_regular);
        errorPassword.setTypeface(custom_font_regular);
        errorMobileNo.setTypeface(custom_font_regular);
        register_user.setTypeface(custom_font_regular);
        header_text.setTypeface(custom_font_regular);
        bottom_text.setTypeface(custom_font_regular);

        signUp = (Button) findViewById(R.id.sign_up);
        signUp.setTypeface(custom_font_bold);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidName()) {
                    if(isValidEmail()){
                        if(isValidPassword()){
                            if(isValidMobNo()){
                                if (isInternetPresent) {
                                    new MTRegistration().execute();
                                }else{
                                    Toast.makeText(getBaseContext(), "You don't have internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean isValidName() {
        final String uName = name.getText().toString();
        if (!isValidName(uName)) {
            errorName.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorName.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidEmail() {
        final String uEmail = email.getText().toString();
        if (!isValidEmail(uEmail)) {
            errorEmail.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorEmail.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidPassword() {
        final String uPassword = password.getText().toString();
        if (!isValidPassword(uPassword)) {
            errorPassword.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorPassword.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidMobNo() {
        final String uMobNo = mobNo.getText().toString();
        if (!isValidMobNo(uMobNo)) {
            errorMobileNo.setVisibility(View.VISIBLE);
            return false;
        } else {
            errorMobileNo.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidName(String uName) {
        return !uName.equals("");
    }

    private boolean isValidEmail(String uEmail) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(uEmail);
        return matcher.matches();
    }

    private boolean isValidPassword(String uPassword) {
        return uPassword.length() >= 7;
    }

    private boolean isValidMobNo(String uMobNo) {
        return !(uMobNo.equals("") || uMobNo.length() != 12);
    }

    class MTRegistration extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;
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

            System.out.println("Response in onPreExecute" + webAddressToPost);
        }

        @Override
        protected ArrayList<Object> doInBackground(String... arg0) {

            JSONParser jParser = new JSONParser();
            ArrayList<Object> jsnObject = jParser.getJSONFromUrl(webAddressToPost);

            System.out.println("Response in doInBackground " + jsnObject);

            return jsnObject;
        }

        @Override
        protected void onPostExecute(ArrayList<Object> result) {

            String uName = name.getText().toString();
            String uEmail = email.getText().toString();
            String uContact = mobNo.getText().toString();
            String uPass = password.getText().toString();

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Calendar calendar = GregorianCalendar.getInstance();
            String updateTime = dateFormatter.format(calendar.getTime());

            String StatusCode = result.get(1).toString();
            System.out.println("Response in registration" + StatusCode);

            try {
                JSONObject jsnObject = (JSONObject) result.get(0);

                if (StatusCode.equals("201")) {
                    String registrationID = null;
                    try {
                        registrationID = jsnObject.getString(Constant.REGISTRATION_ID);
                        User.createUser(getDatabase(), uEmail, uName, uPass, uContact, registrationID, updateTime);
                        Log.e( TAG, registrationID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("registeredUser", registrationID);
                    editor.commit();


                    Intent intent = new Intent(SignUpActivity.this, ValidateUserActivity.class);
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
            } catch (CouchbaseLiteException e) {
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

    public String GetCountryZipCode(){
        String CountryID="";
        String CountryZipCode="";

        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID= manager.getSimCountryIso().toUpperCase();
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String aRl = rl[i];
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}
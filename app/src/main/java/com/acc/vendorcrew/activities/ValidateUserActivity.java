package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acc.vendorcrew.R;
import com.acc.vendorcrew.connectivity.ConnectionDetector;
import com.acc.vendorcrew.constant.Constant;
import com.acc.vendorcrew.json.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ValidateUserActivity extends Activity implements Animation.AnimationListener{

    public static final String PREFS_NAME = "RegisteredPrefs";
    EditText uPassword, uPin;
    TextView errorPassword, errorPin;
    Button submit;
    String password, pin, code;
    JSONParser jParser;
    boolean isInternetPresent = false;
    ConnectionDetector cd;
    Animation animSlideUp, animSlideDown;
    private SharedPreferences preferences;
    private Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_user);

        Typeface custom_font_regular = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Reg.ttf");
        Typeface custom_font_bold = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");

        uPassword = (EditText) findViewById(R.id.valid_password);
        uPassword.setTypeface(custom_font_regular);

        uPin = (EditText) findViewById(R.id.valid_pin);
        uPin.setTypeface(custom_font_regular);

        errorPassword = (TextView) findViewById(R.id.password_error_msg);
        errorPin = (TextView) findViewById(R.id.pin_error_msg);

        submit = (Button) findViewById(R.id.continue_btn);
        submit.setTypeface(custom_font_bold);

        password = uPassword.getText().toString();
        pin = uPin.getText().toString();

        uPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidPassword();
            }
        });

        uPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isValidPin();
            }
        });

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animSlideUp.setAnimationListener(this);

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animSlideDown.setAnimationListener(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        code = preferences.getString("registeredUser", "");

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidPassword()) {
                    if (isValidPin()) {
                        if (isInternetPresent) {
                            new MTValidate().execute();
                        } else {
                            Toast.makeText(getBaseContext(), "You don't have internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private boolean isValidPassword() {
        final String upassword = uPassword.getText().toString();
        if (!isValidPassword(upassword)) {
            errorPassword.setVisibility(View.VISIBLE);
            errorPassword.startAnimation(animSlideDown);
            return false;
        } else {
            errorPassword.startAnimation(animSlideUp);
            errorPassword.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidPin(){
        final String upin = uPin.getText().toString();
        if (!isValidPin(upin)) {
            errorPin.setVisibility(View.VISIBLE);
            errorPin.startAnimation(animSlideDown);
            return false;
        }else {
            errorPin.startAnimation(animSlideUp);
            errorPin.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidPassword(String uPassword) {
        if (!(uPassword.equals("") || uPassword.length() < 7)) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isValidPin(String uPin) {
        if (!(uPin.equals("") || uPin.length() < 6)) {
            return true;
        }
        else return false;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    class MTValidate extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;
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
                    + Constant.VALIDATION_ACCOUNT_ID + code + "&"
                    + Constant.VALIDATION_PASSWORD + uPassword.getText().toString() + "&"
                    + Constant.VALIDATE_PIN + uPin.getText().toString();

            System.out.println("Response in onPreExecute " + webAddressToPost);
        }

        @Override
        protected ArrayList<Object> doInBackground(String... arg0) {

            jParser = new JSONParser();

            return jParser.getJSONFromUrl(webAddressToPost);
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
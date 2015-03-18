package com.acc.vendorcrew.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.acc.vendorcrew.constant.Constant;
import com.acc.vendorcrew.connectivity.ConnectionDetector;
import com.acc.vendorcrew.json.JSONParser;
import com.acc.vendorcrew.model.SignUpModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends Activity implements Animation.AnimationListener {

    Button signUp, signIn;
    EditText uEmail, uPassword;
    TextView headerText, or, forgotPassword;
    ArrayList<SignUpModel> userList;
    private TextView errorEmail, errorPassword;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Animation animSlideUp, animSlideDown;

    private JSONParser jParser;
    JSONArray jsonArray = null;
    final String TAG = "CouchbaseDB";
    /*private Manager manager;
    Database database;*/
    // create a name for the database and make sure the name is legal
//    String dbname = "cms_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Typeface custom_font_regular = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Reg.ttf");
        Typeface custom_font_bold = Typeface.createFromAsset(getAssets() , "font/ProximaNova-Bold.ttf");

        headerText = (TextView) findViewById(R.id.header_text);
        headerText.setTypeface(custom_font_regular);
        or = (TextView) findViewById(R.id.or);
        or.setTypeface(custom_font_regular);
        forgotPassword = (TextView) findViewById(R.id.forget_pwd);
        forgotPassword.setTypeface(custom_font_regular);

        signUp = (Button) findViewById(R.id.sign_up);
        signIn = (Button) findViewById(R.id.sign_in);
        signUp.setTypeface(custom_font_bold);
        signIn.setTypeface(custom_font_bold);

        uEmail = (EditText) findViewById(R.id.email);
        uPassword = (EditText) findViewById(R.id.password);
        uEmail.setTypeface(custom_font_regular);
        uPassword.setTypeface(custom_font_regular);

        errorEmail = (TextView) findViewById(R.id.email_error_msg);
        errorPassword = (TextView) findViewById(R.id.password_error_msg);
        errorEmail.setTypeface(custom_font_regular);
        errorPassword.setTypeface(custom_font_regular);

        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animSlideUp.setAnimationListener(this);

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animSlideDown.setAnimationListener(this);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail()) {
                    if (isValidPassword()) {
                        if (isInternetPresent) {
                            uEmail.setVisibility(View.GONE);
                            uPassword.setVisibility(View.GONE);
                            new MTLogin().execute();
                        } else {
                            Toast.makeText(getBaseContext(), "You don't have internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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

    private boolean isValidEmail() {
        final String uemail = uEmail.getText().toString();
        if (isValidEmail(uemail)) {
            errorEmail.setVisibility(View.VISIBLE);
            errorEmail.startAnimation(animSlideDown);
            return false;
        } else {
            errorEmail.startAnimation(animSlideUp);
            errorEmail.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidPassword() {
        final String upassword = uPassword.getText().toString();
        if (isValidPassword(upassword)) {
            errorPassword.setVisibility(View.VISIBLE);
            errorPassword.startAnimation(animSlideDown);
            return false;
        } else {
            errorPassword.startAnimation(animSlideUp);
            errorPassword.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean isValidEmail(String uemail) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(uemail);
        return matcher.matches();
    }

    private boolean isValidPassword(String upassword) {
        return upassword.length() >= 7;
    }

    @Override
    protected void onResume() {
        super.onResume();
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


    class MTLogin extends AsyncTask<String, String, ArrayList<Object>> {

        ProgressDialog pDialog;

        private String webAddressToPost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage("Loging...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

	 	    webAddressToPost = Constant.LOGIN_URL + Constant.LOGIN_EMAIL_ID + uEmail.getText().toString() + "&" + Constant.LOGIN_PASS + uPassword.getText().toString();

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

            if (StatusCode.equals("200")) {

                JSONObject jsnObject = (JSONObject) result.get(0);
                try {

                    String _id = jsnObject.getString("_id");

                    Intent intent = new Intent(SignInActivity.this, AddVendorCategoryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                JSONObject jsnObject = (JSONObject) result.get(0);

                try {
                    String message = jsnObject.getString("message");
                    Toast.makeText(SignInActivity.this, "" + message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pDialog.dismiss();
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
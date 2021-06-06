package com.softwaregiants.careertinder.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.LoginModel;
import com.softwaregiants.careertinder.models.LoginSuccessModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import org.apache.commons.validator.routines.EmailValidator;

public class LoginActivity extends Activity {

    //region globals
    Button btnHit;
    Context mContext;
    RetrofitClient mRetrofitClient;

    EditText username;
    EditText password;

    String usernameText = "";
    String passwordText = "";

    LoginModel loginModel;

    Intent nextIntent;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        btnHit = findViewById(R.id.btnHit);
        btnHit.setOnClickListener(ocl);
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());

        username = findViewById(R.id.ETUsername);
        password = findViewById(R.id.ETPass);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }

    //region onclick listener
    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch( v.getId() ) {
                case R.id.btnHit: {
                    login();
                    UtilityMethods.hideKeyboardFrom(mContext,v);
                    break;
                }
            }
        }
    };
    //endregion

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {

        @Override
        public void onSuccess(BaseBean baseBean) {
            LoginSuccessModel loginSuccessModel = (LoginSuccessModel) baseBean;
            if (loginSuccessModel.getStatusCode().equals(Constants.SC_SUCCESS)) {
                PreferenceManager.getInstance(getApplicationContext()).putString(Constants.PK_AUTH_CODE, loginSuccessModel.getAuth_code());
                PreferenceManager.getInstance(getApplicationContext()).putString(Constants.PK_ID, loginSuccessModel.getAuth_code());
                PreferenceManager.getInstance(getApplicationContext()).putString(Constants.PK_EMAIL, usernameText);
                PreferenceManager.getInstance(getApplicationContext()).putString(Constants.PK_USER_TYPE, loginSuccessModel.getUser_type());
                PreferenceManager.getInstance(getApplicationContext()).putBoolean(Constants.PK_LOGIN_STATE, true);

                if (loginSuccessModel.getUser_type().equals(Constants.USER_TYPE_JOB_SEEKER)){
                    if (loginSuccessModel.getIs_profile_created().equalsIgnoreCase("no")) {
//                        nextIntent = new Intent(mContext, CreateCandidateProfileActivity.class);
                        nextIntent = new Intent(mContext, OnboardingActivity.class);
                    } else {
                        PreferenceManager.getInstance(getApplicationContext()).putBoolean(Constants.PK_PROFILE_CREATED,true);
                        nextIntent = new Intent(mContext, CandidateDashboardActivity.class);
                    }
                }
                else if (loginSuccessModel.getUser_type().equals(Constants.USER_TYPE_EMPLOYER)){
                    nextIntent = new Intent(mContext, JobOpeningsListActivity.class);
                }
                startActivity(nextIntent);
                finish();
            } else {
                Toast.makeText(mContext, Constants.MSG_ERROR,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };


    public boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

    public void register(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void login(){
        usernameText = username.getText().toString();
        passwordText = password.getText().toString();

        if (usernameText.equals("")){
            Toast.makeText(mContext,"Please enter Username",Toast.LENGTH_SHORT).show();
        }
        else if (!validateEmail(usernameText)){
            Toast.makeText(mContext,"Username is not valid",Toast.LENGTH_SHORT).show();
        }
        else if (passwordText.equals("")){
            Toast.makeText(mContext,"Please enter Password",Toast.LENGTH_SHORT).show();
        }
        else if (passwordText.length() < 8){
            Toast.makeText(mContext,"Please should be at least 8 characters",Toast.LENGTH_SHORT).show();
        }
        else {
            loginModel = new LoginModel();
            loginModel.setEmailid(usernameText);
            loginModel.setPassword(UtilityMethods.sha224Hash(passwordText));
            if ( UtilityMethods.isConnected(mContext) ) {
                mRetrofitClient.mApiInterface.login(loginModel).enqueue(mRetrofitClient.createProgress(mContext));
            }
        }
    }
}
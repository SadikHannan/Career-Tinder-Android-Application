package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.SignUpModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import org.apache.commons.validator.routines.EmailValidator;

public class SignUpActivity extends AppCompatActivity {

    Button createMyAccount;
    Context mContext;
    RetrofitClient mRetrofitClient;

    EditText fullName;
    EditText emailAddress;
    EditText password;
    EditText confirmPassword;

    RadioButton jobseeker;
    RadioButton employer;

    String userTypeString = "not set";
    String name = "";
    String email = "";
    String pass = "";
    String confirmPass = "";
    SignUpModel signUpModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        createMyAccount = findViewById(R.id.createMyAccountBtn);
        createMyAccount.setOnClickListener(ocl);
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());

        fullName = findViewById(R.id.fullName);
        emailAddress = findViewById(R.id.emailAddress);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        jobseeker = findViewById(R.id.jobSeekerRadioButton);
        jobseeker.setOnClickListener(js_radio_listener);
        employer = findViewById(R.id.employerRadioButton);
        employer.setOnClickListener(e_radio_listener);
    }

    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = fullName.getText().toString();
            email = emailAddress.getText().toString();
            pass = password.getText().toString();
            confirmPass = confirmPassword.getText().toString();
            if(name.equals("")){
                Toast.makeText(mContext,"Please enter your Full Name", Toast.LENGTH_SHORT).show();
            }
            else if (email.equals("") || (!validateEmail(email))) {
                if(email.equals("")){
                    Toast.makeText(mContext, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateEmail(email)) {
                    Toast.makeText(mContext, "Your Email address is not valid", Toast.LENGTH_SHORT).show();
                }
            }
            else if (!checkPasswordLength(pass) || pass.equals("")){
                    if(pass.equals("")){
                        Toast.makeText(mContext,"Please enter a password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!checkPasswordLength(pass)){
                        Toast.makeText(mContext,"Password should be at least 8 characters", Toast.LENGTH_SHORT).show();
                    }
            }
            else if (!matchPasswords(pass, confirmPass) || confirmPass.equals("")){
                if (confirmPass.equals("")){
                    Toast.makeText(mContext,"Please re-enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matchPasswords(pass, confirmPass)){
                    Toast.makeText(mContext,"Password don't match", Toast.LENGTH_SHORT).show();
                }
            }
            else if(userTypeString.equals("not set")){
                    Toast.makeText(mContext,"SELECT ONE : Job Seeker or Employer?", Toast.LENGTH_SHORT).show();
            }
            else{
                signUpModel = new SignUpModel();
                signUpModel.setName(name);
                signUpModel.setEmail(email);
                signUpModel.setPassword(UtilityMethods.sha224Hash(pass));
                signUpModel.setUserType(userTypeString);
                if ( UtilityMethods.isConnected(mContext) ) {
                    mRetrofitClient.mApiInterface.signUp(signUpModel).enqueue(mRetrofitClient.createProgress(mContext));
                }
            }
        }

    };

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getStatusCode().equals(Constants.SC_SUCCESS)) {
                Toast.makeText(mContext,"Account Created",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
            } else {
                Toast.makeText(mContext,"Account already exists with this email",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    View.OnClickListener js_radio_listener = new View.OnClickListener(){
        public void onClick(View view) {
            userTypeString = Constants.USER_TYPE_JOB_SEEKER;
            UtilityMethods.hideKeyboardFrom(mContext,view);
        }
    };
    View.OnClickListener e_radio_listener = new View.OnClickListener(){
        public void onClick(View view) {
            userTypeString = Constants.USER_TYPE_EMPLOYER;
            UtilityMethods.hideKeyboardFrom(mContext,view);
        }
    };

    public boolean matchPasswords(String p, String c){
        return p.equals(c);
    }

    public boolean checkPasswordLength(String pas){
        return pas.length() >= 8;
    }

    public boolean validateEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }

}

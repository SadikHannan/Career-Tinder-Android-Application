package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;

public class SplashActivity extends AppCompatActivity {

    Context mContext;
    Intent nextActivity;
    TextView TVSW;
    Boolean devModeStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        TVSW = findViewById( R.id.TVSW);
        TVSW.setOnClickListener(onClickListener);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( !devModeStarted ) {
                    if (PreferenceManager.getInstance(getApplicationContext()).getBoolean(Constants.PK_LOGIN_STATE, false)) {
                        if (PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_USER_TYPE, "").equals(Constants.USER_TYPE_JOB_SEEKER)) {
                            if ( PreferenceManager.getInstance(getApplicationContext()).getBoolean(Constants.PK_PROFILE_CREATED, false) ) {
                                nextActivity = new Intent(mContext, CandidateDashboardActivity.class);
                            } else {
//                                nextActivity = new Intent(mContext, CreateCandidateProfileActivity.class);
                                nextActivity = new Intent(mContext, OnboardingActivity.class);
                            }
                        } else if (PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_USER_TYPE, "").equals(Constants.USER_TYPE_EMPLOYER)) {
                            nextActivity = new Intent(mContext, JobOpeningsListActivity.class);
                        } else {
                            Toast.makeText(mContext, Constants.MSG_LOGIN_ERROR, Toast.LENGTH_SHORT).show();
                            nextActivity = new Intent(mContext, LoginActivity.class);
                        }
                    } else {
                        nextActivity = new Intent(mContext, LoginActivity.class);
                    }
                    startActivity(nextActivity);
                    finish();
                } else if (Constants.isDeveloperBuild) {
                    showURLChangeAlert();
                }

            }
        }, 1500);

    }

    private void showURLChangeAlert() {
        final View view = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.alert_new_base_url, null);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("New URL");
        alertDialog.setCancelable(true);
//                    alertDialog.setMessage("New URL");

        final EditText etURL =  view.findViewById(R.id.etURL);
        etURL.setText(Constants.BASE_URL);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.getInstance(mContext).putString(Constants.PK_CUSTOM_URL,etURL.getText().toString());
                startActivity(new Intent(mContext,SplashActivity.class));
                finish();
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(mContext, SplashActivity.class));
                finish();
            }
        });

        alertDialog.setView(view);
        alertDialog.show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            devModeStarted = true;
        }
    };
}

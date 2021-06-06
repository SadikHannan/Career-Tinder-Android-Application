package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.callback.ACTION_PERFORMED;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.squareup.picasso.Picasso;

public class JobDetailActivity extends BaseActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    JobOpeningModel jobOpeningModel;

    ImageView picture;
    TextView TVCompanyName;
    TextView TVJobTitle;
    TextView TVJobType;
    TextView TVJobDescription;

    TextView TVDesiredQualification;
    TextView TVDesiredWorkExperience;
    TextView TVPlaceOfWork;

    TextView TVSkill1;
    TextView TVSkill2;
    TextView TVSkill3;

    TextView TVLanguage1;
    TextView TVLanguage2;

    boolean matched;

    Button btnAccept;
    Button btnReject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        jobOpeningModel = getIntent().getParcelableExtra("job");
        matched = getIntent().getBooleanExtra("matched", false);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setTitle("Job Details");

        picture = findViewById(R.id.picture);
        TVCompanyName = findViewById(R.id.TVCompanyName);
        TVJobTitle = findViewById(R.id.TVJobTitle);
        TVJobType = findViewById(R.id.TVJobType);
        TVJobDescription = findViewById(R.id.TVJobDescription);

        TVDesiredQualification = findViewById(R.id.TVDesiredQualification);
        TVDesiredWorkExperience = findViewById(R.id.TVDesiredWorkExperience);
        TVPlaceOfWork = findViewById(R.id.TVPlaceOfWork);

        TVSkill1 = findViewById(R.id.TVSkill1);
        TVSkill2 = findViewById(R.id.TVSkill2);
        TVSkill3 = findViewById(R.id.TVSkill3);

        TVLanguage1 = findViewById(R.id.TVLanguage1);
        TVLanguage2 = findViewById(R.id.TVLanguage2);

        TVCompanyName.setText(jobOpeningModel.getCompanyName());
        TVJobTitle.setText(jobOpeningModel.getJobTitle());
        TVJobType.setText(jobOpeningModel.getJobType());
        TVJobDescription.setText(jobOpeningModel.getJobDescription());

        TVDesiredQualification.setText(jobOpeningModel.getDesiredQualification());
        TVDesiredWorkExperience.setText(jobOpeningModel.getDesiredWorkExperience() + " months");
        TVPlaceOfWork.setText(jobOpeningModel.getPlaceOfWork());

        TVSkill1.setText(jobOpeningModel.getSkill1());
        TVSkill2.setText(jobOpeningModel.getSkill2());
        TVSkill3.setText(jobOpeningModel.getSkill3());

        TVLanguage1.setText(jobOpeningModel.getPreferredlanguage1());
        TVLanguage2.setText(jobOpeningModel.getPreferredlanguage2());

        btnAccept = findViewById(R.id.BtnAccept);
        btnReject = findViewById(R.id.BtnReject);
        btnAccept.setOnClickListener(onClickListener);
        btnReject.setOnClickListener(onClickListener);


        if ( null != jobOpeningModel.getImageUrl() && !jobOpeningModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(jobOpeningModel.getImageUrl());
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit()
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder).into(picture);
                }
            });
        } else {
            picture.setImageResource(R.drawable.image_placeholder);
        }

        if (matched){
            switchView();
        }
    }

    public void switchView(){
        ConstraintLayout constraintLayout = findViewById(R.id.contactDetailsConstraintLayout);
        LinearLayout linearLayout = findViewById(R.id.LLButtons);
        TextView contactDetails = findViewById(R.id.TVEmailAddress);

        contactDetails.setText(jobOpeningModel.geteMail());
        constraintLayout.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent output = new Intent();
            switch ( v.getId() ) {
                case R.id.BtnAccept:
                    output.putExtra("action", ACTION_PERFORMED.SWIPE_RIGHT_ACCEPT);
                    break;
                case R.id.BtnReject:
                    output.putExtra("action", ACTION_PERFORMED.SWIPE_LEFT_REJECT);
                    break;
            }
            setResult(RESULT_OK, output);
            finish();
        }
    };
}

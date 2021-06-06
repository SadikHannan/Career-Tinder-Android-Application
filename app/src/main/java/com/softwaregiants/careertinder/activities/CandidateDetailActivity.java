package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.softwaregiants.careertinder.models.CandidateProfileModel;
import com.squareup.picasso.Picasso;

public class CandidateDetailActivity extends BaseActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    CandidateProfileModel candidateProfileModel;

    ImageView picture;
    TextView TVCompanyName;
    TextView TVJobType;
    TextView TVJobDescription;

    TextView TVDesiredQualification;
    TextView TVDesiredWorkExperience;
    TextView TVPlaceOfWork;

    TextView TVSkill1;
    TextView TVSkill2;
    TextView TVSkill3;
    TextView TVSkillAddl;

    TextView TVLanguage1;
    TextView TVLanguage2;

    Button btnAccept;
    Button btnReject;
    TextView TVEmail;
    TextView TVPhone;
    boolean matched;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_detail);
        candidateProfileModel = getIntent().getParcelableExtra("job");
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
        TVJobType = findViewById(R.id.TVJobType);
        TVJobDescription = findViewById(R.id.TVJobDescription);

        TVDesiredQualification = findViewById(R.id.TVDesiredQualification);
        TVDesiredWorkExperience = findViewById(R.id.TVDesiredWorkExperience);
        TVPlaceOfWork = findViewById(R.id.TVPlaceOfWork);

        TVSkill1 = findViewById(R.id.TVSkill1);
        TVSkill2 = findViewById(R.id.TVSkill2);
        TVSkill3 = findViewById(R.id.TVSkill3);
        TVSkillAddl = findViewById(R.id.TVSkillAddl);

        TVLanguage1 = findViewById(R.id.TVLanguage1);
        TVLanguage2 = findViewById(R.id.TVLanguage2);

        TVEmail = findViewById(R.id.TVEmail);
        TVPhone = findViewById(R.id.TVPhone);

        TVCompanyName.setText(candidateProfileModel.getName());
        TVJobType.setText( candidateProfileModel.getJobType() );
        TVJobDescription.setText(candidateProfileModel.getAboutme());

        TVDesiredQualification.setText(candidateProfileModel.getHighest_education());
        TVDesiredWorkExperience.setText(candidateProfileModel.getWork_experience() + " months");
        TVPlaceOfWork.setText(candidateProfileModel.getPlace());

        if (candidateProfileModel.getSkill_one() != null && !candidateProfileModel.getSkill_one().isEmpty() ) {
            TVSkill1.setText(candidateProfileModel.getSkill_one());
        } else {
            TVSkill1.setVisibility(View.GONE);
        }
        if (candidateProfileModel.getSkill_two() != null && !candidateProfileModel.getSkill_two().isEmpty() ) {
            TVSkill2.setText(candidateProfileModel.getSkill_two());
        } else {
            TVSkill2.setVisibility(View.GONE);
        }
        if (candidateProfileModel.getSkill_three() != null && !candidateProfileModel.getSkill_three().isEmpty() ) {
            TVSkill3.setText(candidateProfileModel.getSkill_three());
        } else {
            TVSkill3.setVisibility(View.GONE);
        }
        if (candidateProfileModel.getSkill_three() != null && !candidateProfileModel.getAdditional_skill().isEmpty() ) {
            TVSkillAddl.setText(candidateProfileModel.getAdditional_skill());
        } else {
            TVSkillAddl.setVisibility(View.GONE);
        }

        TVLanguage1.setText(candidateProfileModel.getFirst_language());
        TVLanguage2.setText(candidateProfileModel.getSecond_language());
        TVEmail.setText(candidateProfileModel.getEmail());
        TVPhone.setText(candidateProfileModel.getPhone());

        btnAccept = findViewById(R.id.BtnAccept);
        btnReject = findViewById(R.id.BtnReject);
        btnAccept.setOnClickListener(onClickListener);
        btnReject.setOnClickListener(onClickListener);

        if ( null != candidateProfileModel.getImageUrl() && !candidateProfileModel.getImageUrl().isEmpty()) {
            StorageReference storageRef = storage.getReference(candidateProfileModel.getImageUrl());
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
        matched = getIntent().getBooleanExtra("matched", false);
        if (matched) {
            LinearLayout linearLayout = findViewById(R.id.LLButtons);
            linearLayout.setVisibility(View.GONE);
        }
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

package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import org.apache.commons.validator.routines.EmailValidator;

import static com.softwaregiants.careertinder.utilities.UtilityMethods.isNumeric;

public class AddNewJobOpening extends ImagePickerActivity {

    //region class variables
    Button btn;
    RetrofitClient mRetrofitClient;

    EditText ETCompanyName;
    EditText ETJobTitle;
    EditText ETJobDescription;
    EditText ETDesiredQualification;
    EditText ETDesiredWorkExperience;
    Spinner spinnerPlaceOfWork;
    EditText ETSkill1;
    EditText ETSkill2;
    EditText ETSkill3;
    Spinner spinnerLanguage1;
    Spinner spinnerLanguage2;
    EditText ETMobileNo;
    EditText ETEmail;
    Spinner jobTypeSpinner;

    String CompanyName = "";
    String JobTitle = "";
    String JobDescription = "";
    String DesiredQualification = "";
    String DesiredWorkExperience = "";
    String PlaceOfWOrk = "";
    String Skill1 = "";
    String Skill2 = "";
    String Skill3 = "";
    String Language1 = "";
    String Language2 = "";
    String MobileNo;
    String Email;
    String JobType = "";

    String authCode = "";
    JobOpeningModel jobOpeningModel;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job_opening);

        setupSpinners();
        init();
    }

    public void init() {
        authCode = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE, "");

        btn = findViewById(R.id.createJobOpeningBtn);
        btn.setOnClickListener(ocl);
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());

        ETCompanyName = findViewById(R.id.ETCompanyName);
        ETJobTitle = findViewById(R.id.ETJobTitle);
        ETJobDescription = findViewById(R.id.ETJobDescription);
        ETDesiredQualification = findViewById(R.id.ETDesiredQualification);
        ETDesiredWorkExperience = findViewById(R.id.ETDesiredWorkExperience);
        spinnerPlaceOfWork = findViewById(R.id.spinnerPlaceOfWork);
        ETSkill1 = findViewById(R.id.ETSkill1);
        ETSkill2 = findViewById(R.id.ETSkill2);
        ETSkill3 = findViewById(R.id.ETSkill3);
        spinnerLanguage1 = findViewById(R.id.spinnerLanguage1);
        spinnerLanguage2 = findViewById(R.id.spinnerLanguage2);
        ETMobileNo = findViewById(R.id.ETmobileNo);
        ETEmail = findViewById(R.id.ETemail);
        jobTypeSpinner = findViewById(R.id.spinnerJobType);

        ETJobDescription.setText("What We Expect:\nDuration:\nStart Date:");

        imageUser = findViewById(R.id.picture);
        updateImageButton = findViewById(R.id.updatePicture);
        updateImageButton.setOnClickListener(updateImageListener);
        requestMultiplePermissions();
    }

    //region onclick listener
    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CompanyName = ETCompanyName.getText().toString();
            JobTitle = ETJobTitle.getText().toString();
            JobDescription = ETJobDescription.getText().toString();
            DesiredQualification = ETDesiredQualification.getText().toString();
            DesiredWorkExperience = ETDesiredWorkExperience.getText().toString();
            PlaceOfWOrk = spinnerPlaceOfWork.getSelectedItem().toString();
            Skill1 = ETSkill1.getText().toString();
            Skill2 = ETSkill2.getText().toString();
            Skill3 = ETSkill3.getText().toString();
            Language1 = spinnerLanguage1.getSelectedItem().toString();
            Language2 = spinnerLanguage2.getSelectedItem().toString();
            MobileNo = ETMobileNo.getText().toString();
            Email = ETEmail.getText().toString();
            JobType = jobTypeSpinner.getSelectedItem().toString();
            if (CompanyName.equals("")){
                Toast.makeText(mContext,"Please enter your Company Name", Toast.LENGTH_SHORT).show();
            }
            else if (JobTitle.equals("")){
                Toast.makeText(mContext,"Please enter Job Title", Toast.LENGTH_SHORT).show();
            }
            else if (JobDescription.equals("")){
                Toast.makeText(mContext,"Please enter job description", Toast.LENGTH_SHORT).show();
            }
            else if (JobType.equals("[SELECT JOB TYPE]")){
                Toast.makeText(mContext,"Please select job type", Toast.LENGTH_SHORT).show();
            }
            else if (DesiredQualification.equals("")){
                Toast.makeText(mContext,"Please enter desired qualification", Toast.LENGTH_SHORT).show();
            }
            else if (DesiredWorkExperience.equals("") || !isNumeric(DesiredWorkExperience)){
                if (DesiredWorkExperience.equals("")) {
                    Toast.makeText(mContext, "Please enter desired Work Experience", Toast.LENGTH_SHORT).show();
                }
                if (!isNumeric(DesiredWorkExperience)){
                    Toast.makeText(mContext, "Please enter desired Work Experience (in Months)", Toast.LENGTH_SHORT).show();
                }
            }
            else if (PlaceOfWOrk.equals("[SELECT PLACE OF WORK]")){
                Toast.makeText(mContext,"Please select a Place of Work", Toast.LENGTH_SHORT).show();
            }
            else if (Skill1.equals("")){
                Toast.makeText(mContext,"Please enter a skill", Toast.LENGTH_SHORT).show();
            }
            else if (Language1.equals("")){
                Toast.makeText(mContext,"Please enter a language", Toast.LENGTH_SHORT).show();
            }
            else if (!validateEmail(Email)){
                Toast.makeText(mContext,"Contact Email invalid", Toast.LENGTH_SHORT).show();
            }
            else if (!UtilityMethods.validatePhone(MobileNo)){
                Toast.makeText(mContext,"Contact number invalid", Toast.LENGTH_SHORT).show();
            }
            else{
                jobOpeningModel = new JobOpeningModel();
                jobOpeningModel.setCompanyName(CompanyName);
                jobOpeningModel.setJobTitle(JobTitle);
                jobOpeningModel.setJobDescription(JobDescription);
                jobOpeningModel.setDesiredQualification(DesiredQualification);
                jobOpeningModel.setDesiredWorkExperience(DesiredWorkExperience);
                jobOpeningModel.setPlaceOfWork(PlaceOfWOrk);
                jobOpeningModel.setSkill1(Skill1);
                jobOpeningModel.setSkill2(Skill2);
                jobOpeningModel.setSkill3(Skill3);
                jobOpeningModel.setPreferredLanguage1(Language1);
                jobOpeningModel.setPreferredLanguage2(Language2);
                jobOpeningModel.setMobileNo(MobileNo);
                jobOpeningModel.seteMail(Email);
                jobOpeningModel.setJobType(JobType);
                if ( UtilityMethods.isConnected(mContext) ) {
                    submit();
                }
            }
        }
    };

    private void submit() {
        mRetrofitClient.createProgress(mContext);
        if ( imageSelected ) {
            uploadImageFile(bitmap,osl,ofl);
        } else {
            mRetrofitClient.mApiInterface.addNewJobOpening(jobOpeningModel, authCode).enqueue(mRetrofitClient);
        }
    }
    //endregion

    OnSuccessListener osl = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Toast.makeText(mContext, "Image Successfully Uploaded!", Toast.LENGTH_SHORT).show();
            jobOpeningModel.setImageUrl(fileName);
            mRetrofitClient.mApiInterface.addNewJobOpening(jobOpeningModel, authCode).enqueue(mRetrofitClient);
        }
    };

    OnFailureListener ofl = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(mContext, "Failed to upload image, please try again!", Toast.LENGTH_SHORT).show();
        }
    };

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            Intent returnIntent = new Intent();
            setResult(RESULT_OK,returnIntent);
            finish();
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    public boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public void setupSpinners(){
        Spinner jobType = findViewById(R.id.spinnerJobType);
        ArrayAdapter<CharSequence> jobTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.job_type, android.R.layout.simple_spinner_item);
        jobTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobType.setAdapter(jobTypeAdapter);

        Spinner place = findViewById(R.id.spinnerPlaceOfWork);
        ArrayAdapter<CharSequence> placeOfWorkAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        placeOfWorkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        place.setAdapter(placeOfWorkAdapter);

        Spinner language1 = findViewById(R.id.spinnerLanguage1);
        ArrayAdapter<CharSequence> language1Adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array_1, android.R.layout.simple_spinner_item);
        language1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language1.setAdapter(language1Adapter);

        Spinner language2 = findViewById(R.id.spinnerLanguage2);
        ArrayAdapter<CharSequence> language2Adapter = ArrayAdapter.createFromResource(this,
                R.array.language_array_2, android.R.layout.simple_spinner_item);
        language2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language2.setAdapter(language2Adapter);
    }
}

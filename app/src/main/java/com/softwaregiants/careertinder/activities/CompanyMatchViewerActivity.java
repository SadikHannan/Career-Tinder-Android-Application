package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.adapters.CompanyMatchViewerAdapter;
import com.softwaregiants.careertinder.callback.OnItemClickListener;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.CandidateProfileModel;
import com.softwaregiants.careertinder.models.JobWiseMatchesModel;
import com.softwaregiants.careertinder.models.ListOfJobWiseMatchesModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyMatchViewerActivity extends BaseActivity {

    String authCode;
    RetrofitClient mRetrofitClient;
    TextView TVNoItems;

    RecyclerView recyclerView;
    CompanyMatchViewerAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    private List<JobWiseMatchesModel> jobWiseMatchesModelList;
    private Map<Integer,List<CandidateProfileModel>> positionToCandidateListMap;
    private List<CandidateProfileModel> currentMatches;

    Spinner spinnerJob;
    int currentSpinnerPosition;

    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_matchviewer);
        mContext = this;
        addDrawer("Your Matches", R.id.nav_view_matches);
//        nextActivity = new Intent(this, CandidateDetailActivity.class);
        nextActivity = new Intent(this, ChatActivity.class);

        init();
    }

    public void init(){
        authCode = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE, "");
        mContext = this;
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());
        recyclerView = findViewById(R.id.my_recycler_view);
        TVNoItems = findViewById(R.id.TVNoItems);

        if ( UtilityMethods.isConnected(mContext) ) {
            mRetrofitClient.mApiInterface.getMatchesForCompany(authCode).enqueue(mRetrofitClient.createProgress(mContext));
        }
        spinnerJob = findViewById(R.id.spinnerJob);
    }

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            ListOfJobWiseMatchesModel listOfJobWiseMatchesModel = (ListOfJobWiseMatchesModel) baseBean;
                if ( listOfJobWiseMatchesModel != null && listOfJobWiseMatchesModel.getJobList() != null &&
                        !listOfJobWiseMatchesModel.getJobList().isEmpty()){
                    jobWiseMatchesModelList = listOfJobWiseMatchesModel.getJobList();

                    TVNoItems.setVisibility(View.INVISIBLE);
                    setUpSpinner(true);
                } else {
                    setUpSpinner(false);
                }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    private void setUpSpinner(Boolean haveData) {
        if (haveData) {
            List<CharSequence> jobTitles = new ArrayList<>();
            positionToCandidateListMap = new HashMap<>();
            int i=0;
            for (JobWiseMatchesModel jobWiseMatchesModel : jobWiseMatchesModelList) {
                jobTitles.add(jobWiseMatchesModel.getJobTitle());
                positionToCandidateListMap.put(i,jobWiseMatchesModel.getApplicantList());
                i++;
            }

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, jobTitles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerJob.setAdapter(adapter);
            spinnerJob.setOnItemSelectedListener(onItemSelectedListener);
        } else {
            CharSequence[] noJobsArray = {"No Jobs To Show"};
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, noJobsArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerJob.setAdapter(adapter);
        }
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentSpinnerPosition = position;
            buildRV();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            currentSpinnerPosition = 0;
            buildRV();
        }
    };

    public void buildRV(){
        currentMatches = positionToCandidateListMap.get(currentSpinnerPosition);
        if ( currentMatches!=null && !currentMatches.isEmpty() ) {
            TVNoItems.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new CompanyMatchViewerAdapter(currentMatches,onItemClickListener);
            recyclerView.setAdapter(mAdapter);
        } else {
            TVNoItems.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if(UtilityMethods.isConnected(mContext)) {
                nextActivity.putExtra("matched", true);
                nextActivity.putExtra("applicant", Long.toString(currentMatches.get(position).getId()));
                nextActivity.putExtra("company", jobWiseMatchesModelList.get(currentSpinnerPosition).getJobId());
                nextActivity.putExtra("currentUser", jobWiseMatchesModelList.get(currentSpinnerPosition).getJobId());
                startActivity(nextActivity.putExtra("job", currentMatches.get(position)));
            }
        }
    };
}

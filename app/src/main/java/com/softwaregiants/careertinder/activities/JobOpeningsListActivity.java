package com.softwaregiants.careertinder.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.adapters.JobOpeningsAdapter;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.JobOpeningsListModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;


public class JobOpeningsListActivity extends BaseActivity {


    Button btn;
    Intent nextActivity;
    Context mContext;
    RetrofitClient mRetrofitClient;

    String authCode;

    RecyclerView recyclerView;
    JobOpeningsAdapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    private JobOpeningsListModel jobOpeningsListModel;
    TextView TVNoItems;

    Intent companyDashboardIntent;
    Intent editJobOpeningIntent;

    int deletePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_openings_list);

        init();
        btn = findViewById(R.id.addJobOpeningBtn);
        btn.setOnClickListener(onClickListener);
        nextActivity = new Intent(this, AddNewJobOpening.class);
        companyDashboardIntent = new Intent(this, CompanyDashboardActivity.class);
        editJobOpeningIntent = new Intent(this, EditJobOpeningActivity.class);


    }

    public void init(){
        mContext = this;
        authCode = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE, "");
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());
        TVNoItems = findViewById(R.id.TVNoItems);

        if ( UtilityMethods.isConnected(mContext) ) {
            mRetrofitClient.mApiInterface.getJobOpenings(authCode).enqueue(mRetrofitClient.createProgress(mContext));
        }
        addDrawer("Your Vacancies",R.id.nav_job_list);
    }

    public void buildRV(){
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new JobOpeningsAdapter(jobOpeningsListModel.getJobOpeningModelList());
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new JobOpeningsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivityForResult(companyDashboardIntent.putExtra("job", jobOpeningsListModel.getJobOpeningModelList().get(position)),
                        Constants.NEED_RESULT_COMPANY_DASHBOARD);
            }

            @Override
            public void onEditClick(int position) {
                startActivityForResult(editJobOpeningIntent.putExtra("jobOpeningObject", jobOpeningsListModel.getJobOpeningModelList().get(position)),
                        Constants.NEED_RESULT_EDIT_COMPANY);
            }

            @Override
            public void onDeleteClick(int position) {
               deletePos = position;
               showDeleteDialog();
            }
        });
    }

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getApiMethod().equals("delete_job_opening")){
                if (baseBean.getStatusCode().equals("Success")){
                    deleteJobOpeningFromView(deletePos);
                    Toast.makeText(mContext, "Job Opening Deleted",Toast.LENGTH_SHORT).show();
                }
            }
            else if(baseBean.getApiMethod().equals("get_job_list")) {
                if (baseBean.getStatusCode().equals("Success")) {
                    jobOpeningsListModel = (JobOpeningsListModel) baseBean;
                    if (jobOpeningsListModel.getJobOpeningModelList() != null &&
                            !jobOpeningsListModel.getJobOpeningModelList().isEmpty()) {
                        TVNoItems.setVisibility(View.INVISIBLE);
                        buildRV();
                    }
                }
            }
            else {
                Toast.makeText(mContext, Constants.MSG_ERROR,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if ( mRetrofitClient != null ) {
            mRetrofitClient.cancelProgress();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(nextActivity,Constants.NEED_RESULT_JOB_OPENING_CREATION);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.NEED_RESULT_EDIT_COMPANY:
                case Constants.NEED_RESULT_JOB_OPENING_CREATION:
                    if (UtilityMethods.isConnected(mContext)) {
                        mRetrofitClient.mApiInterface.getJobOpenings(authCode).enqueue(mRetrofitClient.createProgress(mContext));
                    }
                    break;
            }
        } else {
            switch (requestCode) {
                case Constants.NEED_RESULT_EDIT_COMPANY:
                case Constants.NEED_RESULT_COMPANY_DASHBOARD:
                    finish();
            }
        }
    }

    public void deleteJobOpeningFromView(int pos){
        jobOpeningsListModel.getJobOpeningModelList().remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    public void showDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("DELETE JOB OPENING");
        builder.setMessage("Are you sure you want to delete " + jobOpeningsListModel.getJobOpeningModelList().get(deletePos).getJobTitle() + "?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                mRetrofitClient.mApiInterface.deleteJobOpening(jobOpeningsListModel.getJobOpeningModelList().get(deletePos), authCode).enqueue(mRetrofitClient.createProgress(mContext));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

package com.softwaregiants.careertinder.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.listeners.ItemRemovedListener;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.callback.ACTION_PERFORMED;
import com.softwaregiants.careertinder.callback.BaseListener;
import com.softwaregiants.careertinder.customViews.TinderJobCard;
import com.softwaregiants.careertinder.models.ApplicantSwipeModel;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.JobMatchesListModel;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import java.util.List;

public class CandidateDashboardActivity extends BaseActivity {

    private static final String TAG = "CandidateDashboard";

    private SwipePlaceHolderView swipePlaceHolderView;
    JobMatchesListModel jobMatchesListModel;
    List<JobOpeningModel> jobOpeningModelList;
    RetrofitClient mRetrofitClient;
    TextView TVNoItems;
    int items = 0;
    int swipedItems = 0;
    static final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_dashboard);
        addDrawer("Job Search",R.id.nav_job_search);
        init();
    }

    private void init() {
        mContext = this;
        swipePlaceHolderView = findViewById(R.id.swipeView);
        TVNoItems = findViewById(R.id.TVNoItems);
        initSwipeView();
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());
        if ( UtilityMethods.isConnected(mContext) ) {
            String authToken = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE,"");
            mRetrofitClient.mApiInterface.getMatchedJobOpenings(authToken).enqueue(mRetrofitClient.createProgress(mContext));
        }
    }

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if (baseBean.getStatusCode().equals("Success")) {
                if ( baseBean.getApiMethod().equals(Constants.API_METHOD_GET_JOB_MATCHES) ) {
                    jobMatchesListModel = (JobMatchesListModel) baseBean;
                    jobOpeningModelList = jobMatchesListModel.getJobOpeningModelList();
                    if (null != jobOpeningModelList && !jobOpeningModelList.isEmpty()) {
                        addNextItems();
                        TVNoItems.setVisibility(View.GONE);
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

    private void initSwipeView() {
        swipePlaceHolderView.disableTouchSwipe();

        Point windowSize = UtilityMethods.getDisplaySize(mContext);

        swipePlaceHolderView.addItemRemoveListener(new ItemRemovedListener() {

            @Override
            public void onItemRemoved(int count) {
                Log.d(TAG, "onItemRemoved: " + count);
//                if(count < 3){
//                    addNextItems(5);
//                }
                swipedItems++;
                if ( swipedItems == jobOpeningModelList.size() ) {
                    TVNoItems.setVisibility(View.VISIBLE);
                }
            }
        });
        swipePlaceHolderView.getBuilder()
//                .setSwipeType(SwipePlaceHolderView.SWIPE_TYPE_VERTICAL)
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setWidthSwipeDistFactor(4)
                .setHeightSwipeDistFactor(6)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - getActionBarHeight())
//                        .setMarginTop(300)
//                        .setMarginLeft(100)
//                        .setViewGravity(Gravity.CENTER)
//                        .setPaddingTop(20)
                        .setSwipeMaxChangeAngle(2f)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));
    }

    private void addNextItems() {
        int iterator = 1;
        JobOpeningModel jobOpeningModel;
        while ( iterator <= PAGE_SIZE && items < jobOpeningModelList.size() ) {
            jobOpeningModel = jobOpeningModelList.get(items);
            swipePlaceHolderView.addView(new TinderJobCard(jobOpeningModel,
                                                            mBaseListener, items));
            iterator++;
            items++;
        }
        swipePlaceHolderView.enableTouchSwipe();
    }

    BaseListener mBaseListener = new BaseListener() {
        @Override
        public void callback(ACTION_PERFORMED action, int pos, Object... args) {
            switch (action) {
                case JOB_CLICK:
                    Intent jobDetail = new Intent(mContext,JobDetailActivity.class);
                    jobDetail.putExtra("job",jobOpeningModelList.get(pos));
                    startActivityForResult(jobDetail,Constants.NEED_RESULT_COMPANY_DETAIL);
                    break;
                case SWIPE_LEFT_REJECT://REJECT
                    if ( UtilityMethods.isConnected(mContext) ) {
                        String authToken = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE,"");
                        ApplicantSwipeModel applicantSwipeModel = new ApplicantSwipeModel();
                        applicantSwipeModel.setApplicantSwipe(Constants.SWIPE_LEFT_KEY);
                        applicantSwipeModel.setCompanyId( Long.toString(jobOpeningModelList.get(pos).getUserId()) );
                        applicantSwipeModel.setApplicantId( Long.toString( jobMatchesListModel.getApplicant().getId() ) );
                        mRetrofitClient.mApiInterface.swipeForApplicant(authToken,applicantSwipeModel).enqueue(mRetrofitClient);
                    } else {
                        swipePlaceHolderView.undoLastSwipe();
                    }
                    break;
                case SWIPE_RIGHT_ACCEPT://ACCEPT
                    if ( UtilityMethods.isConnected(mContext) ) {
                        String authToken = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE,"");
                        ApplicantSwipeModel applicantSwipeModel = new ApplicantSwipeModel();
                        applicantSwipeModel.setApplicantSwipe(Constants.SWIPE_RIGHT_KEY);
                        applicantSwipeModel.setCompanyId( Long.toString(jobOpeningModelList.get(pos).getUserId()) );
                        applicantSwipeModel.setApplicantId( Long.toString( jobMatchesListModel.getApplicant().getId() ) );
                        mRetrofitClient.mApiInterface.swipeForApplicant(authToken,applicantSwipeModel).enqueue(mRetrofitClient);
                    } else {
                        swipePlaceHolderView.undoLastSwipe();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.NEED_RESULT_COMPANY_DETAIL) {
            if ( data != null ) {
                ACTION_PERFORMED action_performed = (ACTION_PERFORMED) data.getSerializableExtra("action");
                swipePlaceHolderView.doSwipe(action_performed == ACTION_PERFORMED.SWIPE_RIGHT_ACCEPT);
            }
        }
    }
}

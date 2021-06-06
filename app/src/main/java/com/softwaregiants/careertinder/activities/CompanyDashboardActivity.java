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
import com.softwaregiants.careertinder.customViews.TinderCandidateCard;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.CandidateListModel;
import com.softwaregiants.careertinder.models.CandidateProfileModel;
import com.softwaregiants.careertinder.models.CompanySwipeModel;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.softwaregiants.careertinder.networking.ApiResponseCallback;
import com.softwaregiants.careertinder.networking.RetrofitClient;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;
import com.softwaregiants.careertinder.utilities.UtilityMethods;

import java.util.List;

import static com.softwaregiants.careertinder.utilities.Constants.API_GET_CANDIDATE_MATCHES;
import static com.softwaregiants.careertinder.utilities.Constants.SC_SUCCESS;

public class CompanyDashboardActivity extends BaseActivity {

    private static final String TAG = "CompanyDashboard";

    private SwipePlaceHolderView swipePlaceHolderView;
    List<CandidateProfileModel> candidateProfileModelList;
    RetrofitClient mRetrofitClient;
    TextView TVNoItems;
    int items = 0;
    int swipedItems = 0;
    static final int PAGE_SIZE = 100;
    JobOpeningModel jobOpeningModel;
    Boolean needUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);
        addDrawer("",0);
        init();
    }

    private void init() {
        mContext = this;
        needUndo = false;
        jobOpeningModel = getIntent().getParcelableExtra("job");
        swipePlaceHolderView = findViewById(R.id.swipeView);
        TVNoItems = findViewById(R.id.TVNoItems);
        initSwipeView();
        mRetrofitClient = RetrofitClient.getRetrofitClient(mApiResponseCallback,getApplicationContext());
        if ( UtilityMethods.isConnected(mContext) ) {
            mRetrofitClient.mApiInterface.getMatchedCandidates(jobOpeningModel.getJobId()).enqueue(mRetrofitClient.createProgress(mContext));
        }
    }

    ApiResponseCallback mApiResponseCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(BaseBean baseBean) {
            if ( baseBean.getStatusCode().equals(SC_SUCCESS) ) {
                if ( baseBean.getApiMethod().equals(API_GET_CANDIDATE_MATCHES) ) {
                    candidateProfileModelList = ((CandidateListModel) baseBean).getApplicantProfiles();
                    if (null != candidateProfileModelList && !candidateProfileModelList.isEmpty()) {
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
                if ( swipedItems == candidateProfileModelList.size() ) {
                    TVNoItems.setVisibility(View.VISIBLE);
                }
                if (needUndo) {
                    needUndo = false;
                    swipePlaceHolderView.undoLastSwipe();
//                    undoSwipe();
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
        CandidateProfileModel candidateProfileModel;
        while ( iterator <= PAGE_SIZE && items < candidateProfileModelList.size() ) {
            candidateProfileModel = candidateProfileModelList.get(items);
            TinderCandidateCard tcc = new TinderCandidateCard(candidateProfileModel,
                    mBaseListener, items);
            swipePlaceHolderView.addView(tcc);
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
                    Intent jobDetail = new Intent(mContext,CandidateDetailActivity.class);
                    jobDetail.putExtra("job", candidateProfileModelList.get(pos));
                    startActivityForResult(jobDetail,Constants.NEED_RESULT_CANDIDATE_DETAIL);
                    break;
                case SWIPE_LEFT_REJECT://REJECT
                    if ( UtilityMethods.isConnected(mContext) ) {
                        String authToken = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE,"");
                        CompanySwipeModel companySwipeModel = new CompanySwipeModel();
                        companySwipeModel.setCompanySwipe(Constants.SWIPE_LEFT_KEY);
                        companySwipeModel.setCompanyId( Long.toString( jobOpeningModel.getUserId() ) );
                        companySwipeModel.setApplicantId( Long.toString( candidateProfileModelList.get(pos).getId() ) );
                        mRetrofitClient.mApiInterface.swipeForCompany(authToken,companySwipeModel).enqueue(mRetrofitClient);
                    } else {
                        //TODO Undo
                        needUndo = true;
                    }
                    break;
                case SWIPE_RIGHT_ACCEPT://ACCEPT
                    if ( UtilityMethods.isConnected(mContext) ) {
                        String authToken = PreferenceManager.getInstance(getApplicationContext()).getString(Constants.PK_AUTH_CODE,"");
                        CompanySwipeModel companySwipeModel = new CompanySwipeModel();
                        companySwipeModel.setCompanySwipe(Constants.SWIPE_RIGHT_KEY);
                        companySwipeModel.setCompanyId( Long.toString( jobOpeningModel.getUserId() ) );
                        companySwipeModel.setApplicantId( Long.toString( candidateProfileModelList.get(pos).getId() ) );
                        mRetrofitClient.mApiInterface.swipeForCompany(authToken,companySwipeModel).enqueue(mRetrofitClient);
                    } else {
                        //TODO Undo
                        needUndo = true;
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.NEED_RESULT_CANDIDATE_DETAIL:
                    if ( data != null ) {
                        ACTION_PERFORMED action_performed = (ACTION_PERFORMED) data.getSerializableExtra("action");
                        swipePlaceHolderView.doSwipe(action_performed == ACTION_PERFORMED.SWIPE_RIGHT_ACCEPT);
                    }
                    break;
            }
        }
    }

}

package com.softwaregiants.careertinder.networking;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.BaseBean;
import com.softwaregiants.careertinder.models.CandidateListModel;
import com.softwaregiants.careertinder.models.GetCandidateDetailModel;
import com.softwaregiants.careertinder.models.JobMatchesListModel;
import com.softwaregiants.careertinder.models.JobOpeningsListModel;
import com.softwaregiants.careertinder.models.ListOfJobWiseMatchesModel;
import com.softwaregiants.careertinder.models.LoginSuccessModel;
import com.softwaregiants.careertinder.preferences.PreferenceManager;
import com.softwaregiants.careertinder.utilities.Constants;

import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient implements Callback<ResponseBody> {

    private static Retrofit retrofit;
    private ApiResponseCallback mApiResponseCallBack;
    public ApiInterface mApiInterface;
    private String TAG = RetrofitClient.class.getSimpleName();
    private Context applicationContext;
    AlertDialog alertDialog;

    public static RetrofitClient getRetrofitClient(ApiResponseCallback mApiResponseCallBack, Context preferenceContext) {
        if ( retrofit == null ) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            // set your desired log level
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            String url = PreferenceManager.getInstance(preferenceContext.getApplicationContext()).getString(Constants.PK_CUSTOM_URL,Constants.BASE_URL);
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl( url )
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        RetrofitClient mRetrofitClient = new RetrofitClient();
        mRetrofitClient.mApiInterface = retrofit.create(ApiInterface.class);
        mRetrofitClient.mApiResponseCallBack = mApiResponseCallBack;
        mRetrofitClient.applicationContext = preferenceContext;
        return mRetrofitClient;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        cancelProgress();
        if ( mApiResponseCallBack != null ) {
            if (response.isSuccessful()) {
                Log.d(TAG, response.toString());
                try {
                    String rawResponse = response.body().string();
                    Log.i(TAG, "onResponse: JSON\n\n" + rawResponse + "\n\n");
                    BaseBean baseBean = new Gson().fromJson(rawResponse, BaseBean.class);
                    if (baseBean.getStatusCode().equalsIgnoreCase(Constants.SC_SUCCESS)) {
                        if (baseBean.getApiMethod().contains(Constants.API_CREATE_CANDIDATE)) {
                            mApiResponseCallBack.onSuccess(baseBean);
                            return;
                        }
                        switch (baseBean.getApiMethod()) {
                            case Constants.API_METHOD_LOGIN: {
                                LoginSuccessModel loginSuccessModel = new Gson().fromJson(rawResponse, LoginSuccessModel.class);
                                mApiResponseCallBack.onSuccess(loginSuccessModel);
                                break;
                            }
                            case Constants.API_METHOD_SIGN_UP: {
                                mApiResponseCallBack.onSuccess(baseBean);
                                break;
                            }
                            case Constants.API_METHOD_ADD_NEW_JOB_OPENING: {
                                mApiResponseCallBack.onSuccess(baseBean);
                                break;
                            }
                            case Constants.API_METHOD_GET_JOB_OPENINGS:
                                JobOpeningsListModel jobOpeningsListModel = new Gson().fromJson(rawResponse, JobOpeningsListModel.class);
                                mApiResponseCallBack.onSuccess(jobOpeningsListModel);
                                break;
                            case Constants.API_METHOD_GET_JOB_MATCHES:
                                JobMatchesListModel jobMatchesListModel = new Gson().fromJson(rawResponse, JobMatchesListModel.class);
                                mApiResponseCallBack.onSuccess(jobMatchesListModel);
                                break;
                            case Constants.API_METHOD_EDIT_JOB_OPENING:
                                mApiResponseCallBack.onSuccess(baseBean);
                                break;
                            case Constants.API_METHOD_DELETE_JOB_OPENING:
                                mApiResponseCallBack.onSuccess(baseBean);
                                break;
                            case Constants.API_METHOD_GET_CANDIDATE_PROFILE:
                                GetCandidateDetailModel candidateProfileModel = new Gson().fromJson(rawResponse, GetCandidateDetailModel.class);
                                mApiResponseCallBack.onSuccess(candidateProfileModel);
                                break;
                            case Constants.API_GET_CANDIDATE_MATCHES: CandidateListModel candidateListModel = new Gson().fromJson(rawResponse, CandidateListModel.class);
                                mApiResponseCallBack.onSuccess(candidateListModel);
                                break;
                            case Constants.API_METHOD_GET_MATCHES_FOR_CANDIDATE:
                                JobOpeningsListModel jobOpeningsList = new Gson().fromJson(rawResponse, JobOpeningsListModel.class);
                                mApiResponseCallBack.onSuccess(jobOpeningsList);
                                break;
                            case Constants.API_METHOD_GET_MATCHES_FOR_COMPANY:
                                ListOfJobWiseMatchesModel listOfJobWiseMatchesModel = new Gson().fromJson(rawResponse, ListOfJobWiseMatchesModel.class);
                                mApiResponseCallBack.onSuccess( listOfJobWiseMatchesModel );
                                break;
                            default: {
                                mApiResponseCallBack.onSuccess(baseBean);
                                break;
                            }
                        }
                    } else {
                        if (null != baseBean.getErrorMsg() && !baseBean.getErrorMsg().isEmpty()) {
                            Toast.makeText(applicationContext, baseBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(applicationContext, Constants.MSG_TECHNICAL_ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Toast.makeText(applicationContext, Constants.MSG_TECHNICAL_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        cancelProgress();
        Log.e(TAG, t.toString());
        mApiResponseCallBack.onFailure(t);
        if ( t instanceof UnknownHostException) {
            Toast.makeText(applicationContext, Constants.MSG_CONNECTION_ERROR, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(applicationContext, Constants.MSG_TECHNICAL_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    public RetrofitClient createProgress(Context activityContext) {
        final View view = ((LayoutInflater) applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.alert_progress, null);
        alertDialog = new AlertDialog.Builder(activityContext).create();
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        return this;
    }

    public void cancelProgress() {
        if (alertDialog!=null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

}

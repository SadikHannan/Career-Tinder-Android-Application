package com.softwaregiants.careertinder.networking;

import com.softwaregiants.careertinder.models.ApplicantSwipeModel;
import com.softwaregiants.careertinder.models.CandidateProfileModel;
import com.softwaregiants.careertinder.models.CompanySwipeModel;
import com.softwaregiants.careertinder.models.JobOpeningModel;
import com.softwaregiants.careertinder.models.LoginModel;
import com.softwaregiants.careertinder.models.SignUpModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("api/signUp")
    Call<ResponseBody> signUp(@Body SignUpModel signUpModel);

    @POST("api/login")
    Call<ResponseBody> login(@Body LoginModel loginModel);

    @POST("candidate/create/{authToken}")
    Call<ResponseBody> postSignUp(@Body CandidateProfileModel candidateProfileModel, @Path("authToken") String auth_code);

    @POST("api/createProfileCompany/{authToken}")
    Call<ResponseBody> addNewJobOpening(@Body JobOpeningModel jobOpeningModel, @Path("authToken") String auth_code);

    @GET("api/listJobsCompany/{authToken}")
    Call<ResponseBody> getJobOpenings(@Path("authToken") String auth_code);

    @GET("api/allJobsCompany")
    Call<ResponseBody> getMatchedJobOpenings();

    @GET("applicant/listOfJobs/{authToken}")
    Call<ResponseBody> getMatchedJobOpenings(@Path("authToken") String auth_code);

    @GET("candidate/all")
    Call<ResponseBody> getMatchedCandidates();

    @GET("company/listOfApplicants/{jobId}")
    Call<ResponseBody> getMatchedCandidates(@Path("jobId") String jobId);

    @PUT("api/updateJobsCompany/{authToken}")
    Call<ResponseBody> updateJobOpening(@Body JobOpeningModel jobOpeningModel, @Path("authToken") String auth_code);

    @PUT("api/company/deleteJobProfile/{authToken}")
    Call<ResponseBody> deleteJobOpening(@Body JobOpeningModel jobOpeningModel, @Path("authToken") String auth_code);

    @GET("candidate/display/{authToken}")
    Call<ResponseBody> getCandidateProfile(@Path("authToken") String auth_code);

    @GET("applicant/getMatches/{authToken}")
    Call<ResponseBody> getMatchesForCandidate(@Path("authToken") String auth_code);

    @GET("api/company/getMatches/{authToken}")
    Call<ResponseBody> getMatchesForCompany(@Path("authToken") String auth_code);

    @PUT("companySwipe/{authToken}")
    Call<ResponseBody> swipeForCompany(@Path("authToken") String auth_code,@Body CompanySwipeModel companySwipeModel);

    @PUT("applicantSwipe/{authToken}")
    Call<ResponseBody> swipeForApplicant(@Path("authToken") String auth_code,@Body ApplicantSwipeModel applicantSwipeModel);


}

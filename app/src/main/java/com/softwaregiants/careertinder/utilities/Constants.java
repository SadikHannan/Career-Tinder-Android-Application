package com.softwaregiants.careertinder.utilities;

public interface Constants {
    String BASE_URL = "https://career-tinder.cfapps.io/" ;

    //API Methods
    String API_METHOD_LOGIN = "login";
    String API_METHOD_SIGN_UP = "sign_up";
    String API_METHOD_ADD_NEW_JOB_OPENING = "add_job_opening";
    String API_METHOD_GET_JOB_OPENINGS = "get_job_list";
    String API_METHOD_GET_JOB_MATCHES = "getPerspectiveJobMatches";
    String API_METHOD_EDIT_JOB_OPENING = "edit_job_opening";
    String API_METHOD_DELETE_JOB_OPENING = "delete_job_opening";
    String API_METHOD_GET_CANDIDATE_PROFILE = "GET_CANDIDATE_PROFILE";
    String API_METHOD_GET_MATCHES_FOR_CANDIDATE = "get_matches_for_candidates";
    String API_METHOD_GET_MATCHES_FOR_COMPANY = "get_matches_for_company";
    String API_GET_CANDIDATE_MATCHES = "getPerspectiveMatches";
    String API_APPLICANT_SWIPE = "update_applicant_swipe";
    String API_COMPANY_SWIPE = "update_company_swipe";

    //API methods for non conforming APIs
    String API_CREATE_CANDIDATE = "candidate/create/";

    //Success Codes
    String SC_SUCCESS = "Success";

    //Messages
    String MSG_CONNECTION_ERROR = "Error connecting to the server. Please check your connection.";
    String MSG_TECHNICAL_ERROR = "There is a technical error. Please try again.";
    String MSG_ERROR = "Error";
    String MSG_LOGIN_ERROR = "Please login again.";

    //Preference Keys
    String PK_AUTH_CODE = "PK_AUTH_CODE";
    String PK_ID = "PK_ID";
    String PK_LOGIN_STATE = "PK_LOGIN_STATE";
    String PK_USER_TYPE = "PK_USER_TYPE";
    String PK_EMAIL = "PK_EMAIL";
    String PK_APPLICANT_PROFILE_ID = "PK_APPLICANT_PROFILE_ID";
    String PK_PROFILE_CREATED = "PK_PROFILE_CREATED";

    //Misc
    String USER_TYPE_JOB_SEEKER = "jobseeker";
    String USER_TYPE_EMPLOYER = "employer";

    //Dev Mode
    boolean isDeveloperBuild = true;
    String PK_CUSTOM_URL = "PK_CUSTOM_URL";

    //Activity result codes
    int NEED_RESULT_JOB_OPENING_CREATION = 12345;
    int NEED_RESULT_EDIT_COMPANY = 12346;
    int NEED_RESULT_COMPANY_DASHBOARD = 12347;
    int NEED_RESULT_COMPANY_DETAIL = 12348;
    int NEED_RESULT_CANDIDATE_DETAIL = 12349;
    int SWIPE_LEFT_KEY = 2;
    int SWIPE_RIGHT_KEY = 1;
}

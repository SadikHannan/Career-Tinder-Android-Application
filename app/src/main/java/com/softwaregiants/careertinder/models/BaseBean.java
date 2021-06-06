package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

public class BaseBean {

    @SerializedName("response_code")
    private String statusCode;

    @SerializedName("api_method")
    private String apiMethod;

    @SerializedName("message")
    private String errorMsg;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String status) {
        this.statusCode = statusCode;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

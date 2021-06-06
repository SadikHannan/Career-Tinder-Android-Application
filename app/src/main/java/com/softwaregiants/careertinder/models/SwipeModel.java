package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

public class SwipeModel {
    @SerializedName("company_id")
    String companyId;

    @SerializedName("applicant_id")
    String applicantId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
}

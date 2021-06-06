package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

public class ApplicantSwipeModel extends SwipeModel {
    @SerializedName("applicant_swipe")
    int applicantSwipe;

    public int getApplicantSwipe() {
        return applicantSwipe;
    }

    public void setApplicantSwipe(int applicantSwipe) {
        this.applicantSwipe = applicantSwipe;
    }
}

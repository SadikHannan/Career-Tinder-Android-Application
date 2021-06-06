package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

public class CompanySwipeModel extends SwipeModel {
    @SerializedName("company_swipe")
    int companySwipe;

    public int getCompanySwipe() {
        return companySwipe;
    }

    public void setCompanySwipe(int companySwipe) {
        this.companySwipe = companySwipe;
    }
}

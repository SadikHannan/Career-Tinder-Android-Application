package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobOpeningsListModel extends BaseBean {

    @SerializedName("joblist")
    List<JobOpeningModel> jobOpeningModelList;

    public List<JobOpeningModel> getJobOpeningModelList() {
        return jobOpeningModelList;
    }

    public void setJobOpeningModelList(List<JobOpeningModel> jobOpeningModelList) {
        this.jobOpeningModelList = jobOpeningModelList;
    }
}

package com.softwaregiants.careertinder.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfJobWiseMatchesModel extends BaseBean {

    @SerializedName("joblist")
    List<JobWiseMatchesModel> jobList;

    public List<JobWiseMatchesModel> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobWiseMatchesModel> jobList) {
        this.jobList = jobList;
    }
}

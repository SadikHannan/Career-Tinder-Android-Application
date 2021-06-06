package com.softwaregiants.careertinder.models;

import java.util.List;

public class JobWiseMatchesModel extends JobOpeningModel {
    List<CandidateProfileModel> applicantList;

    public List<CandidateProfileModel> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(List<CandidateProfileModel> applicantList) {
        this.applicantList = applicantList;
    }
}

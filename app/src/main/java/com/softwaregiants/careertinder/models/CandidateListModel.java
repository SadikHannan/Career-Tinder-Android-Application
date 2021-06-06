package com.softwaregiants.careertinder.models;

import java.util.List;

public class CandidateListModel extends BaseBean {

    List<CandidateProfileModel> applicantProfiles;

    public List<CandidateProfileModel> getApplicantProfiles() {
        return applicantProfiles;
    }

    public void setApplicantProfiles(List<CandidateProfileModel> applicantProfiles) {
        this.applicantProfiles = applicantProfiles;
    }
}

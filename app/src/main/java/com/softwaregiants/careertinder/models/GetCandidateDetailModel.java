package com.softwaregiants.careertinder.models;

public class GetCandidateDetailModel extends BaseBean {
    CandidateProfileModel applicant;

    public CandidateProfileModel getApplicant() {
        return applicant;
    }

    public void setApplicant(CandidateProfileModel applicant) {
        this.applicant = applicant;
    }
}

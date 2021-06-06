package com.softwaregiants.careertinder.models;

public class JobMatchesListModel extends JobOpeningsListModel {
    CandidateProfileModel applicant;

    public CandidateProfileModel getApplicant() {
        return applicant;
    }

    public void setApplicant(CandidateProfileModel applicant) {
        this.applicant = applicant;
    }
}

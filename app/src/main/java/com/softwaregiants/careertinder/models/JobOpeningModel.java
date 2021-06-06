package com.softwaregiants.careertinder.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class JobOpeningModel implements Parcelable {

    @SerializedName("jobid")
    private String jobId;

    @SerializedName("id")
    private long userId;

    public JobOpeningModel() {}

    protected JobOpeningModel(Parcel in) {
        jobId = in.readString();
        userId = in.readLong();
        companyName = in.readString();
        jobTitle = in.readString();
        jobDescription = in.readString();
        desiredQualification = in.readString();
        desiredWorkExperience = in.readString();
        placeOfWork = in.readString();
        skill1 = in.readString();
        skill2 = in.readString();
        skill3 = in.readString();
        preferredLanguage1 = in.readString();
        preferredLanguage2 = in.readString();
        jobType = in.readString();
        mobileNo = in.readString();
        eMail = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<JobOpeningModel> CREATOR = new Creator<JobOpeningModel>() {
        @Override
        public JobOpeningModel createFromParcel(Parcel in) {
            return new JobOpeningModel(in);
        }

        @Override
        public JobOpeningModel[] newArray(int size) {
            return new JobOpeningModel[size];
        }
    };

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @SerializedName("companyname")
    private String companyName;

    @SerializedName("jobtitle")
    private String jobTitle;

    @SerializedName("jobdescription")
    private String jobDescription;

    @SerializedName("education")
    private String desiredQualification;

    @SerializedName("workexperience")
    private String desiredWorkExperience;

    @SerializedName("location")
    private String placeOfWork;

    private String skill1;

    private String skill2;

    private String skill3;

    @SerializedName("preferedlanguage1")
    private String preferredLanguage1;

    @SerializedName("preferedlanguage2")
    private String preferredLanguage2;

    @SerializedName("jobType")
    private String jobType;

    private String mobileNo;

    private String eMail;

    private String imageUrl;

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getPreferredLanguage1() {
        return preferredLanguage1;
    }

    public String getPreferredLanguage2() {
        return preferredLanguage2;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getDesiredQualification() {
        return desiredQualification;
    }

    public void setDesiredQualification(String desiredQualification) {
        this.desiredQualification = desiredQualification;
    }

    public String getDesiredWorkExperience() {
        return desiredWorkExperience;
    }

    public void setDesiredWorkExperience(String desiredWorkExperience) {
        this.desiredWorkExperience = desiredWorkExperience;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    public String getSkill1() {
        return skill1;
    }

    public void setSkill1(String skill1) {
        this.skill1 = skill1;
    }

    public String getSkill2() {
        return skill2;
    }

    public void setSkill2(String skill2) {
        this.skill2 = skill2;
    }

    public String getSkill3() {
        return skill3;
    }

    public void setSkill3(String skill3) {
        this.skill3 = skill3;
    }

    public String getPreferredlanguage1() {
        return preferredLanguage1;
    }

    public void setPreferredLanguage1(String preferredLanguage1) {
        this.preferredLanguage1 = preferredLanguage1;
    }

    public String getPreferredlanguage2() {
        return preferredLanguage2;
    }

    public void setPreferredLanguage2(String preferredLanguage2) {
        this.preferredLanguage2 = preferredLanguage2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeLong(userId);
        dest.writeString(companyName);
        dest.writeString(jobTitle);
        dest.writeString(jobDescription);
        dest.writeString(desiredQualification);
        dest.writeString(desiredWorkExperience);
        dest.writeString(placeOfWork);
        dest.writeString(skill1);
        dest.writeString(skill2);
        dest.writeString(skill3);
        dest.writeString(preferredLanguage1);
        dest.writeString(preferredLanguage2);
        dest.writeString(jobType);
        dest.writeString(mobileNo);
        dest.writeString(eMail);
        dest.writeString(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

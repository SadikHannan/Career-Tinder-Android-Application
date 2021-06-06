package com.softwaregiants.careertinder.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CandidateProfileModel extends BaseBean implements Parcelable {

    private String university;

    @SerializedName("name")
    private String Name;

    @SerializedName("qualification")
    private String highest_education;

    @SerializedName("workexperience")
    private String work_experience;

    @SerializedName("firstskill")
    private String skill_one;

    @SerializedName("secondskill")
    private String skill_two;

    @SerializedName("thirdskill")
    private String skill_three;

    private String address;

    @SerializedName("bio")
    private String aboutme;

    @SerializedName("birthday")
    private String dateBirth;

    @SerializedName("nationality")
    private String place;

    @SerializedName("eu")
    private Boolean eu_citizen;

    @SerializedName("additionalskill")
    private String additional_skill;

    @SerializedName("firstlanguage")
    private String first_language;

    @SerializedName("secondlanguage")
    private String second_language;

    private String imageUrl;
    private String jobType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phone;
    @SerializedName("emailid")
    private String email;
    private long id;

    protected CandidateProfileModel(Parcel in) {
        university = in.readString();
        Name = in.readString();
        highest_education = in.readString();
        work_experience = in.readString();
        skill_one = in.readString();
        skill_two = in.readString();
        skill_three = in.readString();
        address = in.readString();
        aboutme = in.readString();
        dateBirth = in.readString();
        place = in.readString();
        byte tmpEu_citizen = in.readByte();
        eu_citizen = tmpEu_citizen == 0 ? null : tmpEu_citizen == 1;
        additional_skill = in.readString();
        first_language = in.readString();
        second_language = in.readString();
        imageUrl = in.readString();
        jobType = in.readString();
        phone = in.readString();
        email = in.readString();
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(university);
        dest.writeString(Name);
        dest.writeString(highest_education);
        dest.writeString(work_experience);
        dest.writeString(skill_one);
        dest.writeString(skill_two);
        dest.writeString(skill_three);
        dest.writeString(address);
        dest.writeString(aboutme);
        dest.writeString(dateBirth);
        dest.writeString(place);
        dest.writeByte((byte) (eu_citizen == null ? 0 : eu_citizen ? 1 : 2));
        dest.writeString(additional_skill);
        dest.writeString(first_language);
        dest.writeString(second_language);
        dest.writeString(imageUrl);
        dest.writeString(jobType);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CandidateProfileModel> CREATOR = new Creator<CandidateProfileModel>() {
        @Override
        public CandidateProfileModel createFromParcel(Parcel in) {
            return new CandidateProfileModel(in);
        }

        @Override
        public CandidateProfileModel[] newArray(int size) {
            return new CandidateProfileModel[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public  CandidateProfileModel () {}



    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getHighest_education() {
        return highest_education;
    }
    public void setHighest_education(String highest_education) { this.highest_education = highest_education; }

    public String getWork_experience() { return work_experience; }
    public void setWork_experience(String work_experience) { this.work_experience = work_experience; }

    public String getSkill_one() { return skill_one; }
    public void setSkill_one(String skill_one) { this.skill_one = skill_one; }

    public String getSkill_two() { return skill_two; }
    public void setSkill_two(String skill_two) { this.skill_two = skill_two; }

    public String getSkill_three() { return skill_three; }
    public void setSkill_three(String skill_three) { this.skill_three = skill_three; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAboutme() { return aboutme; }
    public void setAboutme(String aboutme) { this.aboutme = aboutme; }

    public String getDateBirth() { return dateBirth; }
    public void setDateBirth(String dateBirth) { this.dateBirth = dateBirth; }

    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }

    public Boolean getEu_citizen() { return eu_citizen; }
    public void setEu_citizen(Boolean eu_citizen) { this.eu_citizen = eu_citizen; }

    public String getAdditional_skill() {
        return additional_skill;
    }

    public void setAdditional_skill(String additional_skill) {
        this.additional_skill = additional_skill;
    }

    public String getFirst_language() {
        return first_language;
    }

    public void setFirst_language(String first_language) {
        this.first_language = first_language;
    }

    public String getSecond_language() {
        return second_language;
    }

    public void setSecond_language(String second_language) {
        this.second_language = second_language;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
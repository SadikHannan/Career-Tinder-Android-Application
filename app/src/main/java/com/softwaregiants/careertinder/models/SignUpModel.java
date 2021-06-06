package com.softwaregiants.careertinder.models;

public class SignUpModel extends BaseBean {
    private String name;
    private String emailid;
    private String password;
    private String userType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return emailid;
    }

    public void setEmail(String email) {
        this.emailid = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

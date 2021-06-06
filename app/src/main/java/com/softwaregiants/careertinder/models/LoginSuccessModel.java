package com.softwaregiants.careertinder.models;

public class LoginSuccessModel extends BaseBean {
    private String auth_code;
    private String user_type;
    private String is_profile_created;

    public String getIs_profile_created() {
        return is_profile_created;
    }

    public void setIs_profile_created(String is_profile_created) {
        this.is_profile_created = is_profile_created;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}

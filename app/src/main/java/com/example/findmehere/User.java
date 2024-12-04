package com.example.findmehere;

public class User {
    String fullName;
    String email;
    String phone;
    String password;
    String id;
    private String profilePic;
    User(){}

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }



    User(String id,String fullName,String email,String phone,String password,String profilePicUrl)
    {
        this.id=id;
        this.fullName=fullName;
        this.email=email;
        this.phone=phone;
        this.password=password;
        this.profilePic =profilePicUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }
}


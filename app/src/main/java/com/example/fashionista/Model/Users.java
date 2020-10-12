package com.example.fashionista.Model;

public class Users {
    private String fName,lName,phone,password,address,profilePic,security1,security2;
    // constructor without paramaters
    public Users(){

    }
    public Users(String fName, String lName, String phone, String password, String address, String profilePic, String security1, String security2) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.profilePic = profilePic;
        this.security1 = security1;
        this.security2 = security2;
    }
    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getSecurity1() {
        return security1;
    }

    public String getSecurity2() {
        return security2;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setSecurity1(String security1) {
        this.security1 = security1;
    }

    public void setSecurity2(String security2) {
        this.security2 = security2;
    }
}

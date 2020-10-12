package com.example.fashionista;

public class Customers{
    private String fname,lname,phone,password,address,profilePic,security1,security2;
    //private byte[] profilePhoto;
    public Customers(){}
    public Customers(String fname, String lname, String phone, String password, String address, String profilePic, String security1, String security2) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.profilePic = profilePic;
        this.security1 = security1;
        this.security2 = security2;
    }
    public void setFName(String fname) {
        this.fname = fname;
    }

    public void setLName(String lname) {
        this.lname = lname;
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

    public String getFName() {
        return fname;
    }

    public String getLName() {
        return lname;
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
}

package com.example.splitpay;
import java.io.Serializable;

public class User implements Serializable{
    private String Name,Gender,Email_id,Pic_url,Username,Password,Phone_no;
    private Integer Age;


    public User(String Name, Integer Age, String Gender, String Email_id, String Phone_no,String Pic_url,String Username, String Password) {
        this.Name = Name;
        this.Age = Age;
        this.Gender = Gender;
        this.Email_id=Email_id;
        this.Phone_no=Phone_no;
        this.Pic_url=Pic_url;
        this.Username=Username;
        this.Password=Password;
    }

    // Add getter and setter methods


    public String getName() {
        return Name;
    }

    public Integer getAge() {
        return Age;
    }

    public String getGender() {
        return Gender;
    }

    public String getEmail_id() {
        return Email_id;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public String getPic_url() {
        return Pic_url;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }


}

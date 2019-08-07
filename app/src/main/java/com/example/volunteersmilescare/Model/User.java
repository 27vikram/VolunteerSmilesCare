package com.example.volunteersmilescare.Model;

public class User {
    private String  Name, Phone, IsVolunteer, Email;

    public User(String name, String phone, String isVolunteer, String email) {
        Name = name;
        Phone = phone;
        IsVolunteer = isVolunteer;
        Email = email;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsVolunteer() {
        return IsVolunteer;
    }

    public void setIsVolunteer(String isVolunteer) {
        IsVolunteer = isVolunteer;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

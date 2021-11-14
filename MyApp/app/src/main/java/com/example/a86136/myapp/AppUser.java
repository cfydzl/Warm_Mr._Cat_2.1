package com.example.a86136.myapp;

/**
 * Created by 86136 on 2021/5/2.
 */

public class AppUser {
    private String user,password,name,phone,address;
    public AppUser(String user, String password, String name, String phone, String address)
    {
        this.user=user;
        this.password=password;
        this.name=name;
        this.phone=phone;
        this.address=address;
    }
    public String get_user()
    {
        return this.user.trim();
    }
    public String get_password()
    {
        return this.password.trim();
    }
    public String get_name()
    {
        return this.name.trim();
    }
    public String get_phone()
    {
        return this.phone.trim();
    }
    public String get_address()
    {
        return this.address.trim();
    }
}

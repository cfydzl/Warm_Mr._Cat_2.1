package com.example.a86136.myapp;

/**
 * Created by 86136 on 2021/5/6.
 */

public class MenuObject {
    private String Menuid,Species,Name,Price,Pciture;
    public MenuObject(String Menuid,String Specise,String Name,String Price,String Pciture)
    {
        this.Menuid=Menuid;
        this.Species=Specise;
        this.Name=Name;
        this.Price=Price;
        this.Pciture=Pciture;
    }

    public String getSpecies() {
        return Species;
    }

    public String getPrice() {
        return Price;
    }

    public String getName() {
        return Name;
    }

    public String getMenuid() {
        return Menuid;
    }

    public String getPciture() {
        return Pciture;
    }
}

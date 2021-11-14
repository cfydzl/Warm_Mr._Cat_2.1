package com.example.a86136.myapp;

import android.net.Uri;

/**
 * Created by 86136 on 2021/5/7.
 */

public class BookObject {
    private String id,menuid,user,text,sum,type;
    public BookObject(String id,String menuid,String user,String text,String sum,String type)
    {
        this.id=id;
        this.menuid=menuid;
        this.user=user;
        this.text=text;
        this.sum=sum;
        this.type= type;
    }

    public String getMenuid() {
        return menuid;
    }

    public String getId() {
        return id;
    }

    public String getSum() {
        return sum;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }
}

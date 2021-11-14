package com.example.a86136.myapp;


import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 86136 on 2021/5/1.
 */

public class Cookie {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    public boolean input_lenth(String qString) {
        qString=qString.trim();
        if (qString.length()<6)
        {
            return true;
        }
        return false;
    }
//    判断字符串空
    public boolean input_empty(String qString) {
        qString=qString.trim();
        if (qString.length()==0)
        {
            return true;
        }
        return false;
    }
    public boolean input_num(String qString){
        qString=qString.trim();
        return (!qString.matches("[0-9]+"));
    }
//    字符串非法字符
    public boolean input_error(String qString) {
        qString=qString.trim();
        String regx="!|！|@|◎|#|＃|(\\$)|￥|%|％|(\\^)|……|(\\&)|※|(\\*)|×|(\\()|（|(\\))|）|_|——|(\\+)|＋|(\\|)|§";
        if (qString!=null) {
            qString = qString.trim();
            Pattern p = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(qString);
            return m.find();
        }
        return false;
    }
}

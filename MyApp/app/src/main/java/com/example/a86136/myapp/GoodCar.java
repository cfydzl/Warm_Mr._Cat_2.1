package com.example.a86136.myapp;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 86136 on 2021/5/6.
 */

public class GoodCar {
    static Map<String, Good> map = new HashMap<String, Good>();
    static float sum=0;
//  初始化
    public void GoodCar_Set(String id,String name,String price,String num,int opt)
    {
        if (opt==1)
        {
            if (get_exist(id))
            {
                exist_increase(id);
            }else
            {
                Good My=new Good(id,name,price,num);
                no_exist_increase(id,My);
            }
        }else
        {
            exist_reduce(id);
        }
    }
//    清除
    public void clear_map()
    {
        map.clear();
        sum=0;
    }
//    存在否？
    public boolean get_exist(String id)
    {
        if (map.containsKey(id))
        {
            return  true;
        }
        return false;
    }
//    存在增加
    public void exist_increase(String id)
    {
        Good now=map.get(id);
        now.setNum(String.valueOf(Integer.parseInt(now.getNum())+1));
        map.put(id,now);
        sum+=Float.parseFloat(now.getPrice());
    }
//    存在减少
    public void exist_reduce(String id)
    {
        Good now=map.get(id);
        now.setNum(String.valueOf(Integer.parseInt(now.getNum())-1));
        if (now.getNum().equals("0"))
        {
            map.remove(id);
        }else
        {
            map.put(id,now);
        }
        sum-=Float.parseFloat(now.getPrice());
    }
//    不存在增加
    public void no_exist_increase(String id,Good good)
    {
        map.put(id,good);
        sum+=Float.parseFloat(good.getPrice());
    }
//    获取总价
    public static String getSum() {
        return String.valueOf(sum);
    }
//    加载菜单
    public String get_num(String id)
    {
        if (map.containsKey(id))
        {
            Good now=map.get(id);
            return now.getNum();
        }
        return "0";
    }
    public String get_list()
    {
        List<Good> list=new ArrayList<Good>();
        int i=0;
        for (String key : map.keySet()) {
            Good value = map.get(key);
            list.add(i,value);
            i++;
        }
        String buy_menu="";
        for (int j=0;j<list.size();j++)
        {
            GoodCar.Good now=list.get(j);
            buy_menu=buy_menu+ Uri.encode(now.getName())+Uri.encode("￥")+Uri.encode(now.getPrice())+Uri.encode("*")+Uri.encode(now.getNum());
            if (j<list.size()-1)
            {
                buy_menu=buy_menu+Uri.encode(",");
            }
        }
        return buy_menu;
    }
    //    菜品类
    class Good{
        private String name,price,num,id;

        public Good(String id,String name,String price,String num)
        {
            this.id=id;
            this.name=name;
            this.price=price;
            this.num=num;
        }
        public void setName(String name) {
            this.name = name;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getNum() {
            return num;
        }
    }
}

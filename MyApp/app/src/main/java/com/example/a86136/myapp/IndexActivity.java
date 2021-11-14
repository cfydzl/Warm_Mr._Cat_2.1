package com.example.a86136.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity {
    TextView sum_price;
    Button user_onclick,book_onclick,buy;
    Button tuijian,zhushi,shisu,shihun,xiaoshi,nongtang,jiushui,nowbutton;
    List<List> all_list;
    List<MenuObject>list;
    private long exitTime = 0;
    DBconnection jdbc;
    ListView food_show;
    Toast toast;
    MyListData_Adapter adapter;
    Message msg;
    GoodCar mycar;
    AlertDialog.Builder builder;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    //线程处理
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    adapter  = new MyListData_Adapter();
                    food_show.setAdapter(adapter);
                    break;
                case 1:
                    builder.setTitle("下单提示" ) ;
                    builder.setMessage("下单成功" ) ;
                    builder.setPositiveButton("确认" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mycar.clear_map();
                            handler.removeCallbacksAndMessages(null);
                            Intent intent = new Intent(IndexActivity.this, BookActivity.class);
                            startActivity(intent);
                            finish();
                        };
                    });
                    builder.show();
                    break;
                case 2:
                    toast=Toast.makeText(getApplicationContext(),"下单失败", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 3:
                    toast=Toast.makeText(getApplicationContext(),"购物车为空", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
    };
    public String getprice(String A,int B)
    {
        return String.valueOf(Integer.parseInt(A)+B);
    }
    public String getprice(String A,String B,int num)
    {
        A=A.trim();
        A=A.substring(3,A.length()-2);
        return String.valueOf(Float.parseFloat(A)+num*Float.parseFloat(B));
    }
//    图片名称转Id
    public int  getResource(String imageName){
        imageName=imageName.trim();
        imageName=imageName.substring(7,imageName.length()-4);
        Field field = null;
        int res_ID = 0;
        try {
            field = R.drawable.class.getField(imageName);
            res_ID = field.getInt(field.getName());
        } catch (Exception e) {}
        return  res_ID;
    }
//    适配器
    class MyListData_Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyListViewHolder holder;
            mycar=new GoodCar();
            if (convertView==null)
            {
                convertView=View.inflate(IndexActivity.this,R.layout.menu_layout,null);
                holder=new MyListViewHolder();
                holder.food_img=convertView.findViewById(R.id.food_img);
                holder.food_name=convertView.findViewById(R.id.food_name);
                holder.food_price=convertView.findViewById(R.id.food_price);
                holder.food_num=convertView.findViewById(R.id.food_num);
                holder.food_jian=convertView.findViewById(R.id.food_jian);
                holder.food_jia=convertView.findViewById(R.id.food_jia);
                convertView.setTag(holder);
            }else
            {
                holder=(MyListViewHolder) convertView.getTag();
            }
            final MenuObject obj=list.get(position);
            holder.food_name.setText(obj.getName());
                    holder.food_price.setText(obj.getPrice());
                    holder.food_num.setText(mycar.get_num(obj.getMenuid()));
                    holder.food_img.setImageResource(getResource(obj.getPciture()));
                    holder.food_jia.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                    holder.food_num.setText(getprice(holder.food_num.getText().toString(),1));
                    sum_price.setText("总价: "+getprice(sum_price.getText().toString(),obj.getPrice(),1)+" ￥");
                    mycar.GoodCar_Set(obj.getMenuid(),obj.getName(),obj.getPrice(),"1",1);
                }
            });
            holder.food_jian.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(holder.food_num.getText().toString())==0)
                    {
                        return;
                    }
                    holder.food_num.setText(String.valueOf(Integer.parseInt(holder.food_num.getText().toString())-1));;
                    sum_price.setText("总价: "+getprice(sum_price.getText().toString(),obj.getPrice(),-1)+" ￥");
                    mycar.GoodCar_Set(obj.getMenuid(),obj.getName(),obj.getPrice(),"1",-1);
                }
            });
            return convertView;
        }
        class  MyListViewHolder{
            ImageView food_img;
            TextView food_name,food_price,food_num;
            Button food_jian,food_jia;
        }
    }
//    退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        tuijian = (Button) findViewById(R.id.tuijian);
        zhushi = (Button) findViewById(R.id.zhushi);
        shisu = (Button) findViewById(R.id.shisu);
        shihun = (Button) findViewById(R.id.shihun);
        nongtang = (Button) findViewById(R.id.nongtang);
        xiaoshi = (Button) findViewById(R.id.xiaoshi);
        jiushui = (Button) findViewById(R.id.jiushui);
        nowbutton=tuijian;
        buy = (Button) findViewById(R.id.buy);
        food_show=(ListView) findViewById(R.id.food_show);
        jdbc=new DBconnection();
        msg=new Message();
        all_list =new ArrayList<List>();
        list =new ArrayList<MenuObject>();
        sum_price=(TextView) findViewById(R.id.sum_price);
        builder = new AlertDialog.Builder(IndexActivity.this);
        sum_price.setText("总价: "+mycar.getSum()+" ￥");
        sp=getSharedPreferences("sp", MODE_PRIVATE);
        edit=sp.edit();
        //        用户跳转
        user_onclick = (Button) findViewById(R.id.user_onclick);
        user_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(IndexActivity.this, UserActivity.class);
                startActivity(next);
            }
        });
//        订单面跳转
        book_onclick = (Button) findViewById(R.id.book_onclick );
        book_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(IndexActivity.this, BookActivity.class);
                startActivity(next);
            }
        });
        //        推荐按钮
        tuijian.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                tuijian.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(0);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=tuijian;
            }
        });
//        主食按钮
        zhushi.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                zhushi.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(1);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=zhushi;
            }
        });
        //        时素按钮
        shisu.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                shisu.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(2);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=shisu;
            }
        });
        //        时荤按钮
        shihun.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                shihun.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(3);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=shihun;
            }
        });
        //        浓汤按钮
        nongtang.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                nongtang.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(4);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=nongtang;
            }
        });
        //        小食按钮
        xiaoshi.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                xiaoshi.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(5);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=xiaoshi;
            }
        });
        //        酒水 按钮
        jiushui.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                nowbutton.setBackgroundColor(Color.parseColor("#F8ECD6"));
                jiushui.setBackgroundColor(Color.parseColor("#FFFAE8"));
                list=(List<MenuObject>)all_list.get(6);
                adapter  = new MyListData_Adapter();
                food_show.setAdapter(adapter);
                nowbutton=jiushui;
            }
        });
//        菜单推荐加载-数据适配器
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<7;i++)
                {
                    all_list.add(i,jdbc.Query_Menu(i));
                }
                list=(List<MenuObject>)all_list.get(0);
                handler.sendEmptyMessage(0);
            }
        }).start();
        // 下单按钮
        buy.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (sum_price.getText().equals("总价: 0.0 ￥"))
                        {
                            handler.sendEmptyMessage(3);
                            return ;
                        }
                        if (jdbc.Insert_Menu(sp.getString("user","")))
                        {
                            handler.sendEmptyMessage(1);
                        }else
                        {
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();
            }
        });
    }
}

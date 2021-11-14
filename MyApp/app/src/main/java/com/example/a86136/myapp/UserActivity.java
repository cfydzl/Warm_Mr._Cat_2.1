package com.example.a86136.myapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 86136 on 2021/5/1.
 */

public class UserActivity extends AppCompatActivity {
    Button index_onclick,book_onclick,set,set_back,restaurant,restaurant_back,packaging,packaging_back,order,order_back,contact,contact_back,exit;
    Button name_update,password_update,phone_update,address_update;
    LinearLayout set_show,user_show,restaurant_show,packaging_show,order_show,contact_show;
    TextView top_user_show,top_name_show,bottom_name_show, bottom_user_show, bottom_password_show, bottom_phone_show, bottom_address_show;
    AlertDialog.Builder builder;
//    数据库的类封装（组长）
    DBconnection jdbc=new DBconnection();

//    类似网站的cookie，进行少量数据存储
    SharedPreferences sp;
    SharedPreferences.Editor edit;

//    常用一些函数的封装（组长写的）
    Cookie mycookie =new Cookie();


    //多线程异步接受消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            Toast toast;
            switch (msg.what)
            {
                case 0:
                    toast=Toast.makeText(getApplicationContext(),"输入为空", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 1://修改失败
                    toast=Toast.makeText(getApplicationContext(),"修改失败", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 2://用户名修改成功
                    top_name_show.setText(msg.obj.toString());
                    bottom_name_show.setText(msg.obj.toString());
                    toast=Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 3://密码修改成功;
                    toast=Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 4://电话修改成功;
                    bottom_phone_show.setText(msg.obj.toString());
                    toast=Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 5://地址修改成功
                    bottom_address_show.setText(msg.obj.toString());
                    toast=Toast.makeText(getApplicationContext(),"修改成功", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 6:
                    //密码太短
                    toast=Toast.makeText(getApplicationContext(),"密码长度需大于5", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 7:
                    //电话存在非法字符
                    toast=Toast.makeText(getApplicationContext(),"号码存在非法字符", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        builder = new AlertDialog.Builder(UserActivity.this);
        sp=getSharedPreferences("sp", MODE_PRIVATE);
        edit=sp.edit();
        user_show=(LinearLayout)findViewById(R.id.user_show);
        set_show=(LinearLayout)findViewById(R.id.set_show);
        restaurant_show=(LinearLayout)findViewById(R.id.restaurant_show);
        packaging_show=(LinearLayout)findViewById(R.id.packaging_show);
        order_show=(LinearLayout)findViewById(R.id.order_show);
        contact_show=(LinearLayout)findViewById(R.id.contact_show);
        top_name_show=(TextView) findViewById(R.id.top_name_show);
        top_user_show=(TextView) findViewById(R.id.top_user_show);
        bottom_name_show=(TextView) findViewById(R.id. bottom_name_show);
        bottom_user_show=(TextView) findViewById(R.id.bottom_user_show);
        bottom_password_show=(TextView) findViewById(R.id.bottom_password_show);
        bottom_phone_show=(TextView) findViewById(R.id.bottom_phone_show);
        bottom_address_show=(TextView) findViewById(R.id.bottom_address_show);

//        信息显示
        top_user_show.setText("账号:"+(sp.getString("user","").length()==0 ? "请完善信息":sp.getString("user","")));
        top_name_show.setText("用户名:"+(sp.getString("name","").length()==0 ? "请完善信息":sp.getString("name","")));
        bottom_name_show.setText((sp.getString("name","").length()==0 ? "请完善信息":sp.getString("name","")));
        bottom_user_show.setText((sp.getString("user","").length()==0 ? "请完善信息":sp.getString("user","")));
        bottom_password_show.setText("**********");
        bottom_phone_show.setText((sp.getString("phone","").length()==0 ? "请完善信息":sp.getString("phone","")));
        bottom_address_show.setText((sp.getString("address","").length()==0 ? "请完善信息":sp.getString("address","")));
//        主页面跳转
        index_onclick = (Button) findViewById(R.id.index_onclick);
        index_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(UserActivity.this, IndexActivity.class);
                startActivity(next);
            }
        });
//        订单面跳转
        book_onclick = (Button) findViewById(R.id.book_onclick );
        book_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(UserActivity.this, BookActivity.class);
                startActivity(next);
            }
        });
//个人信息
        set = (Button) findViewById(R.id.set );
        set.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_show.setVisibility(View.GONE);
                set_show.setVisibility(View.VISIBLE);
            }
        });
//        个人返回
        set_back = (Button) findViewById(R.id.set_back );
        set_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_show.setVisibility(View.GONE);
                user_show.setVisibility(View.VISIBLE);
            }
        });
        //堂食信息
        restaurant = (Button) findViewById(R.id.restaurant );
        restaurant.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_show.setVisibility(View.GONE);
                restaurant_show.setVisibility(View.VISIBLE);
            }
        });
//        堂食返回
        restaurant_back = (Button) findViewById(R.id.restaurant_back );
        restaurant_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                restaurant_show.setVisibility(View.GONE);
                user_show.setVisibility(View.VISIBLE);
            }
        });
        //外卖信息
        packaging = (Button) findViewById(R.id.packaging );
        packaging.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_show.setVisibility(View.GONE);
                packaging_show.setVisibility(View.VISIBLE);
            }
        });
//        外卖返回
        packaging_back = (Button) findViewById(R.id.packaging_back );
        packaging_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                packaging_show.setVisibility(View.GONE);
                user_show.setVisibility(View.VISIBLE);
            }
        });
        //订单信息
        order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_show.setVisibility(View.GONE);
                order_show.setVisibility(View.VISIBLE);
            }
        });
//        订单返回
        order_back = (Button) findViewById(R.id.order_back );
        order_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                order_show.setVisibility(View.GONE);
                user_show.setVisibility(View.VISIBLE);
            }
        });
        //联系信息
        contact = (Button) findViewById(R.id.contact );
        contact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_show.setVisibility(View.GONE);
                contact_show.setVisibility(View.VISIBLE);
            }
        });
//        联系返回
        contact_back = (Button) findViewById(R.id.contact_back );
        contact_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_show.setVisibility(View.GONE);
                user_show.setVisibility(View.VISIBLE);
            }
        });
//                退出
        exit = (Button) findViewById(R.id.exit );
        exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                Intent next = new Intent(UserActivity.this, MainActivity.class);
                startActivity(next);
            }
        });
//        名字修改
        name_update = (Button) findViewById(R.id.name_update );
        name_update.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserActivity.this);
                builder.setTitle("请输入");
                builder.setView(editText);
                builder.setPositiveButton("修改" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(mycookie.input_empty(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }
                                if(jdbc.Insert_and_Update("update INFORMATION set NAME = ? where USERNAME = ?",editText.getText().toString(),sp.getString("user","")))
                                {
                                    Message msg=new Message();
                                    msg.what=2;
                                    msg.obj=editText.getText().toString();
                                    edit.putString("name", editText.getText().toString());
                                    handler.sendMessage(msg);
                                }else
                                {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        //        密码修改
        password_update = (Button) findViewById(R.id.password_update );
        password_update.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserActivity.this);
                editText.setTransformationMethod(new PasswordTransformationMethod());
                builder.setTitle("请输入");
                builder.setView(editText);
                builder.setPositiveButton("修改" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(mycookie.input_empty(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }else if (mycookie.input_lenth(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(6);
                                    return;
                                }
                                if(jdbc.Insert_and_Update("update INFORMATION set PASSWORD = ? where USERNAME = ?",editText.getText().toString(),sp.getString("user","")))
                                {
                                    Message msg=new Message();
                                    msg.what=3;
                                    msg.obj=editText.getText().toString();
                                    edit.putString("password", editText.getText().toString());
                                    handler.sendMessage(msg);
                                }else
                                {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        //        电话修改
        phone_update = (Button) findViewById(R.id.phone_update );
        phone_update.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_DATETIME);
                builder.setTitle("请输入");
                builder.setView(editText);
                builder.setPositiveButton("修改" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(mycookie.input_empty(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }else if(mycookie.input_num(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(7);
                                    return;
                                }
                                if(jdbc.Insert_and_Update("update INFORMATION set PHONE = ? where USERNAME = ?",editText.getText().toString(),sp.getString("user","")))
                                {
                                    Message msg=new Message();
                                    msg.what=4;
                                    msg.obj=editText.getText().toString();
                                    edit.putString("phone", editText.getText().toString());
                                    handler.sendMessage(msg);
                                }else
                                {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        //        地址修改
        address_update = (Button) findViewById(R.id.address_update );
        address_update.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(UserActivity.this);
                builder.setTitle("请输入");
                builder.setView(editText);
                builder.setPositiveButton("修改" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(mycookie.input_empty(editText.getText().toString()))
                                {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }
                                if(jdbc.Insert_and_Update("update INFORMATION set ADDRESS = ? where USERNAME = ?",editText.getText().toString(),sp.getString("user","")))
                                {
                                    Message msg=new Message();
                                    msg.what=5;
                                    msg.obj=editText.getText().toString();
                                    edit.putString("address", editText.getText().toString());
                                    handler.sendMessage(msg);
                                }else
                                {
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }
}

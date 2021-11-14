package com.example.a86136.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 86136 on 2021/4/24.
 */

public class RegisterActivity extends AppCompatActivity {
    String user,password1,password2;
    DBconnection jdbc=new DBconnection();
    Cookie mycookie =new Cookie();
    Button back,register;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    //多线程异步接受消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(RegisterActivity.this);
            switch (msg.what)
            {
                case 1://用户已存在
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("用户已存在" ) ;
                    builder.setPositiveButton("确认" ,  null );
                    builder.show();
                    break;
                case 2://注册成功
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("注册成功" ) ;
                    builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.removeCallbacksAndMessages(null);
                            Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
                            startActivity(intent);
                            finish();
                        };
                    });
                    builder.show();
                    break;
                case 3://注册失败
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("注册失败" ) ;
                    builder.setPositiveButton("确认",null);
                    builder.show();
                    break;
                case 4://输入空
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("账号或密码不为空" ) ;
                    builder.setPositiveButton("确认",null);
                    builder.show();
                    break;
                case 5://输入非法
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("账号或密码存在非法字符" ) ;
                    builder.setPositiveButton("确认",null);
                    builder.show();
                    break;
                case 6://两次密码不同
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("密码确认不同" ) ;
                    builder.setPositiveButton("确认",null);
                    builder.show();
                    break;
                case 7://长度大于5
                    builder.setTitle("注册提示" ) ;
                    builder.setMessage("用户与密码长度需大于5" ) ;
                    builder.setPositiveButton("确认",null);
                    builder.show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sp=getSharedPreferences("sp", MODE_PRIVATE);;
        edit=sp.edit();
//      登录跳转
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent next = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(next);
            }
        });
        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                user = ((EditText) findViewById(R.id.user)).getText().toString();
                password1 = ((EditText) findViewById(R.id.password1)).getText().toString();
                password2 = ((EditText) findViewById(R.id.password2)).getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mycookie.input_empty(user)||mycookie.input_empty(password1)||mycookie.input_empty(password2))
                        {
//                            输入为空
                            handler.sendEmptyMessage(4);
                            return;
                        }else if (mycookie.input_error(user)||mycookie.input_error(password1)||mycookie.input_error(password2))
                        {
//                            存在非法字符
                            handler.sendEmptyMessage(5);
                            return;
                        }else if (!password1.equals(password2))
                        {
//                            密码两次不同
                            handler.sendEmptyMessage(6);
                            return;
                        }else if(mycookie.input_lenth(user)||mycookie.input_lenth(password1))
                        {
//                            长度必须均大于5
                            handler.sendEmptyMessage(7);
                            return;
                        }
                        if (jdbc.Query("select *from INFORMATION where USERNAME = ? and MANAGEMENT = ?",user,0)) {
//                            账户存在
                            handler.sendEmptyMessage(1);
                        } else {
                            if (jdbc.Insert_and_Update("insert into INFORMATION values (?,?,NULL,NULL,NULL,0)",user,password1))
                            {
//                                注册成功
                                List<AppUser> list=jdbc.Query_AppUser("select *from INFORMATION where USERNAME = ?",user);
                                edit.putString("user",list.get(0).get_user());
                                edit.putString("password",list.get(0).get_password());
                                edit.putString("name",list.get(0).get_name());
                                edit.putString("phone",list.get(0).get_phone());
                                edit.putString("address",list.get(0).get_address());
                                edit.commit();
                                handler.sendEmptyMessage(2);
                            }else
                            {
//                                注册失败
                                handler.sendEmptyMessage(3);
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
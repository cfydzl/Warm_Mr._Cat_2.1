package com.example.a86136.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private String user,password;
    DBconnection jdbc;
    Cookie mycookie =new Cookie();
    Button login,register;
    private long exitTime = 0;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    //多线程异步接受消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(MainActivity.this);
            switch (msg.what)
            {
                case 1://登录成功
                    builder.setTitle("登录提示" ) ;
                    builder.setMessage("登录成功" ) ;
                    builder.setPositiveButton("确认" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.removeCallbacksAndMessages(null);
                            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                            startActivity(intent);
                            finish();
                        };
                    });
                    builder.show();
                    break;
                case 2://密码或用户错误
                    builder.setTitle("登录提示" ) ;
                    builder.setMessage("用户或密码错误" ) ;
                    builder.setPositiveButton("确认" ,  null );
                    builder.show();
                    break;
                case 3://用户或密码为空
                    builder.setTitle("登录提示" ) ;
                    builder.setMessage("用户或密码为空" ) ;
                    builder.setPositiveButton("确认" ,  null );
                    builder.show();
                    break;
                case 4://非法字符
                    builder.setTitle("登录提示" ) ;
                    builder.setMessage("用户或密码存在非法字符" ) ;
                    builder.setPositiveButton("确认" ,  null );
                    builder.show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("sp", MODE_PRIVATE);
        edit=sp.edit();
        jdbc=new DBconnection();

//      查看是否有登录记录
        if (sp.getString("user","").length()>0)
        {
            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(intent);
            finish();
        }
//      注册跳转
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(next);
            }
        });

//       登录
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = ((EditText) findViewById(R.id.user)).getText().toString();
                password = ((EditText) findViewById(R.id.password)).getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (mycookie.input_empty(user)||mycookie.input_empty(password))
                        {
//                            输入为空
                            handler.sendEmptyMessage(3);
                            return;
                        }else if (mycookie.input_error(user)||mycookie.input_error(password))
                        {
//                            输入存在非法字符
                            handler.sendEmptyMessage(4);
                            return;
                        }
                        if (jdbc.Query("select *from INFORMATION where USERNAME = ? and PASSWORD = ? and MANAGEMENT = 0",user,password)) {
//                            账户存在且密码正确用cookie存储
                            List<AppUser> list=jdbc.Query_AppUser("select *from INFORMATION where USERNAME = ?",user);
                            edit.putString("user",list.get(0).get_user());
                            edit.putString("password",list.get(0).get_password());
                            edit.putString("name",list.get(0).get_name());
                            edit.putString("phone",list.get(0).get_phone());
                            edit.putString("address",list.get(0).get_address());
                            edit.commit();
                            handler.sendEmptyMessage(1);
                        } else {
//                            账户不存在或密码错误
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();
            }
        });
    }

}

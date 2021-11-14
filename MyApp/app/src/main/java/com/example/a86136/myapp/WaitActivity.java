package com.example.a86136.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 86136 on 2021/5/6.
 */

public class WaitActivity extends AppCompatActivity {
    DBconnection jdbc;
    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
    //多线程异步接受消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(WaitActivity.this);
            switch (msg.what)
            {
                case 1:
                    Intent next = new Intent(WaitActivity.this, MainActivity.class);
                    startActivity(next);
                    break;
            }
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        Toast toast;
        jdbc = new DBconnection();
        toast=Toast.makeText(getApplicationContext(),"欢迎使用", Toast.LENGTH_SHORT);
        showMyToast(toast,2*1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1900);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }).start();
    }
}

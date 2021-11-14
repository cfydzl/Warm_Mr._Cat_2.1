package com.example.a86136.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.a86136.myapp.R.id.food_show;

public class BookActivity extends AppCompatActivity {
    Button index_onclick,user_onclick,retail_back;
    private long exitTime = 0;
    List<BookObject> list;
    DBconnection jdbc;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    AlertDialog.Builder builder;
    Toast toast;
    Message msg;
    ListView book_show;
    MyListData_Adapter adapter;
    TextView retail_show,my_order;
    String[] array;
    String retail_text;
    //线程处理
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    adapter  = new MyListData_Adapter();
                    book_show.setAdapter(adapter);
                    break;
                case 1:
                    toast=Toast.makeText(getApplicationContext(),msg.obj.toString(), Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 2:
                    builder.setTitle("取消提示" ) ;
                    builder.setMessage("取消成功" ) ;
                    builder.setPositiveButton("确认" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.removeCallbacksAndMessages(null);
                            Intent intent = new Intent(BookActivity.this, BookActivity.class);
                            startActivity(intent);
                            finish();
                        };
                    });
                    builder.show();
                    break;
                case 3:
                    toast=Toast.makeText(getApplicationContext(),"取消失败", Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 4:
                    retail_show.setText(retail_text);
                    break;
            }
        }
    };
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
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyListViewHolder holder;
            if (convertView==null)
            {
                convertView=View.inflate(BookActivity.this,R.layout.book_layout,null);
                holder=new MyListViewHolder();
                holder.bookid=convertView.findViewById(R.id.bookid);
                holder.opt=convertView.findViewById(R.id.opt);
                holder.retail=convertView.findViewById(R.id.retail);
                holder.booktype=convertView.findViewById(R.id.booktype);
                convertView.setTag(holder);
            }else
            {
                holder=(MyListViewHolder) convertView.getTag();
            }
            final BookObject mybook=list.get(position);
            String orderid=mybook.getMenuid();
            String bookuser=mybook.getUser();
            holder.bookid.setText(orderid.substring(bookuser.length(),orderid.length())+"/"+mybook.getId());
            holder.booktype.setText("订单类型:"+mybook.getType());
            if(mybook.getType().contains("(取消订单)"))
            {
                holder.opt.setText("已结单");
            }
            holder.opt.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.opt.getText().equals("已结单"))
                    {
                        return;
                    }
                    builder.setTitle("取消提示" ) ;
                    builder.setMessage("确定取消？" ) ;
                    builder.setPositiveButton("确认" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (jdbc.Insert_and_Update("update BOOK_MENU set TYPE= ? +TYPE where ID = ?","(取消订单)",mybook.getId()))
                                    {
                                        handler.sendEmptyMessage(2);
                                    }else
                                    {
                                        handler.sendEmptyMessage(3);
                                    }
                                }
                            }).start();
                        };
                    });
                    builder.setNegativeButton("关闭", null);
                    builder.show();
                }
            });
//            菜单详细
            holder.retail.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    my_order.setText("￥"+mybook.getSum());
                    my_order.setTextColor(Color.parseColor("#FF9900"));
                    book_show.setVisibility(View.GONE);
                    retail_show.setVisibility(View.VISIBLE);
                    retail_back.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            retail_text="";
                            array= Uri.decode(mybook.getText().trim()).split(",");
                            for (int i=0;i<array.length;i++)
                            {
                                retail_text=retail_text+array[i]+"\n\n";
                            }
                            handler.sendEmptyMessage(4);
                        }
                    }).start();
                }
            });
            return convertView;
        }
        class  MyListViewHolder{
            TextView bookid,booktype;
            Button retail,opt;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        list=new ArrayList<BookObject>();
        jdbc=new DBconnection();
        sp=getSharedPreferences("sp", MODE_PRIVATE);
        edit=sp.edit();
        msg=new Message();
        book_show=(ListView)findViewById(R.id.book_show);
        builder = new AlertDialog.Builder(BookActivity.this);
        retail_show=(TextView) findViewById(R.id.retail_show);
        retail_back=(Button)findViewById(R.id.retail_back);
        user_onclick = (Button) findViewById(R.id.user_onclick );
        index_onclick = (Button) findViewById(R.id.index_onclick);
        my_order=(TextView)findViewById(R.id.my_order);
        //        主页面跳转
        index_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent next = new Intent(BookActivity.this, IndexActivity.class);
        startActivity(next);
    }
});
//        用户面跳转
        user_onclick.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(BookActivity.this, UserActivity.class);
                startActivity(next);
            }
        });
//        订单推荐加载-数据适配器
        new Thread(new Runnable() {
            @Override
            public void run() {
                list=jdbc.Query_Book(sp.getString("user",""));
                handler.sendEmptyMessage(0);
            }
        }).start();
        retail_back.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_order.setText("我的订单");
                my_order.setTextColor(Color.parseColor("#79776E"));
                book_show.setVisibility(View.VISIBLE);
                retail_show.setVisibility(View.GONE);
                retail_back.setVisibility(View.GONE);
            }
        });
    }
}

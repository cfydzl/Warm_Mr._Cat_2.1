package com.example.a86136.myapp;


import android.net.Uri;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class DBconnection {
    private static LinkedList<Connection> dataSources = new LinkedList<Connection>();
    private static String ip="192.168.59.1";
    private static String database="myweb";
    private static String user="cfydzl";
    private static String password="123506467";

    public DBconnection(){
//      连接池构建
        if(dataSources.size()==0)
        {
            init();
        }
    }
//    创建链接
    public  void init()
    {
        for(int i = 0; i < 20; i++) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:jtds:sqlserver://"+ip+"/"+database+";charset=utf-8",user,password);
                        dataSources.add(con);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.toString());
                    }
                }
            }).start();
        }
    }
//    获取时间
    public String get_time()
    {
        String result="";
        Calendar cal=Calendar.getInstance();
        cal.setTime(new Date());
        int   year=cal.get(Calendar.YEAR);
        result=result+String.valueOf(year);
        int   month=cal.get(Calendar.MONTH)+1;
        if (month<10)
        {
            result=result+"0";
        }
        result=result+String.valueOf(month);
        int   date=cal.get(Calendar.DATE);
        if (date<10)
        {
            result=result+"0";
        }
        result=result+String.valueOf(date);
        return result;
    }
//    获取链接
    public Connection getConnection() throws SQLException {
        if(dataSources.size()==0||dataSources==null)
        {
            init();
        }
        return dataSources.removeFirst();
    }
//  释放链接
    public void releaseConnection(Connection conn) {
        dataSources.add(conn);
        return ;
    }
//    数据库查询是否存在
    public boolean Query(String sql, Object... objectses)
    {
        boolean result=false;
        Connection con= null;
        try {
            con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i=0;i<objectses.length;i++)
            {
                preparedStatement.setObject(i+1, Uri.encode(objectses[i].toString().trim()));
            }
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                result=true;
            }
            releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
//    插入和更新
    public boolean Insert_and_Update(String sql, Object... objectses)
    {
        boolean result=false;
        Connection con= null;
        try {
            con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i=0;i<objectses.length;i++)
            {
                preparedStatement.setObject(i+1,  Uri.encode(objectses[i].toString().trim()));
            }
            if (preparedStatement.executeUpdate()>0)
            {
                result=true;
            }else
            {
                result=false;
            }
            releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
//    数据库查询
    public List Query_AppUser(String sql, Object... objectses)
    {
        List<AppUser> list=new ArrayList<AppUser>();
        Connection con= null;
        try {
            con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i=0;i<objectses.length;i++)
            {
                 preparedStatement.setObject(i+1, Uri.encode(objectses[i].toString().trim()));
            }
            ResultSet resultSet=preparedStatement.executeQuery();
            for (int i=0;resultSet.next();i++)
            {
                AppUser people=new AppUser(Uri.decode(resultSet.getString(2).trim()),
                        Uri.decode(resultSet.getString(3).trim()),
                        Uri.decode(resultSet.getString(4).trim()),
                        Uri.decode(resultSet.getString(5).trim()),
                        Uri.decode(resultSet.getString(6).trim())
                );
                list.add(i,people);
            }
            releaseConnection(con);
            } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    //    数据库查询菜单
    public List Query_Menu(int species_num)
    {
        String[] species=new String[]{"推荐","主食","时素","时荤","浓汤","小食","酒水"};
        String sql="select *from MENU where SPECIES = ?";
        List<MenuObject> list=new ArrayList<MenuObject>();
        Connection con= null;
        try {
            con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setObject(1, Uri.encode(species[species_num]));
            ResultSet resultSet=preparedStatement.executeQuery();
            for (int i=0;resultSet.next();i++)
            {
                MenuObject menu=new MenuObject(Uri.decode(resultSet.getString(1).trim()),
                        Uri.decode(resultSet.getString(2).trim()),
                        Uri.decode(resultSet.getString(3).trim()),
                        Uri.decode(resultSet.getString(4).trim()),
                        Uri.decode(resultSet.getString(5).trim())
                );
                list.add(i,menu);
            }
            releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
//    插入订单
    public  boolean Insert_Menu(String user)
    {
        GoodCar mycar=new GoodCar();
        String buy_menu=mycar.get_list();
        boolean result=false;
        Connection con= null;
        try {
            con = getConnection();
            String sql="insert into BOOK_MENU values (?,?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,  Uri.encode(user.trim()+get_time().trim()));
            preparedStatement.setString(2,  Uri.encode(user.trim()));
            preparedStatement.setString(3,  Uri.encode(buy_menu.trim()));
            preparedStatement.setString(4,  Uri.encode(mycar.getSum().trim()));
            preparedStatement.setString(5,  Uri.encode("外卖"));
            if (preparedStatement.executeUpdate()>0)
            {
                result=true;
            }else
            {
                result=false;
            }
            releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
//    查询订单
    public List Query_Book(String user)
    {
        List<BookObject> list=new ArrayList<BookObject>();
        Connection con= null;
        String sql="select *from BOOK_MENU where USERNAME = ? ORDER BY LEN(TYPE) ASC,ID";
        try {
            con = getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setObject(1, Uri.encode(user.trim()));
            ResultSet resultSet=preparedStatement.executeQuery();
            for (int i=0;resultSet.next();i++)
            {
                BookObject book=new BookObject(Uri.decode(resultSet.getString(1).trim()),
                        Uri.decode(resultSet.getString(2).trim()),
                        Uri.decode(resultSet.getString(3).trim()),
                        Uri.decode(resultSet.getString(4).trim()),
                        Uri.decode(resultSet.getString(5).trim()),
                        Uri.decode(resultSet.getString(6).trim())
                );
            list.add(i,book);
            }
            releaseConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

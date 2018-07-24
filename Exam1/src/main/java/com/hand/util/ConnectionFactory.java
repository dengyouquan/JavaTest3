package com.hand.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public enum  ConnectionFactory {
    INSTANCE;
    private static String driver;
    private static String url;
    private static String name;
    private static String pwd;
    static {
        /*Properties properties = new Properties();
        InputStream is = ConnectionFactory.class.getClassLoader().
                getResourceAsStream("dbconfig.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            System.out.println("配置文件读取失败");
            e.printStackTrace();
        }
        driver = properties.getProperty("mysqldriver");
        url = properties.getProperty("mysqlurl");
        name = properties.getProperty("mysqlname");
        pwd = properties.getProperty("mysqlpwd");*/

        //从环境变量中读取
        String ip = System.getenv("ip");
        String port = System.getenv("port");
        String dbname = System.getenv("dbname");
        url="jdbc:mysql://"+ip+":"+port+"/"+dbname+"?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT&allowPublicKeyRetrieval=true";
        name = System.getenv("name");
        pwd = System.getenv("pwd");
    }
    public Connection getConnection(){
        Connection connection = null;
        try {
            //Class.forName(driver);
            connection = DriverManager.getConnection(url,name,pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void release(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        if(conn!=null && !conn.isClosed()){
            conn.close();
        }
        if(ps!=null && !ps.isClosed()){
            ps.close();
        }
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
    }
    public static void release(PreparedStatement ps, ResultSet rs) throws SQLException {
        if(ps!=null && !ps.isClosed()){
            ps.close();
        }
        if(rs!=null && !rs.isClosed()){
            rs.close();
        }
    }
    public static void release(PreparedStatement ps) throws SQLException {
        if(ps!=null && !ps.isClosed()){
            ps.close();
        }
    }
    public static void release(Connection conn) throws SQLException {
        if(conn!=null && !conn.isClosed()){
            conn.close();
        }
    }

}

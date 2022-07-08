package com.ehang.mysql.mybatis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


import com.mysql.jdbc.CallableStatement;




public class Main {
    private static final String driverName = "com.mysql.jdbc.Driver";
    private static final String dbURL = "jdbc:mysql://192.168.1.237:3306/pageinfo?user=root&password=123456&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";
    private static final String userName = "root";
    private static final String userPassword = "123456";
    private static Connection conn=null;
//    public static JDBCUtil instance;
//    public static JDBCUtil getInstance(){
//        if(instance == null){
//            instance = new JDBCUtil();
//        }
//        return instance;
//    }
    @SuppressWarnings("unused")
    private static Connection getConnection(){
        if(conn==null){
            try {
                Class.forName(driverName);
                conn = DriverManager.getConnection(dbURL, userName, userPassword);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void main(String [] args) throws SQLException {
        String sql="{call pr_multi(?,?)}";
        Connection connection = getConnection();
//        CallableStatement cstm = (CallableStatement) connection.prepareCall(sql); //实例化对象
//        cstm.setInt(1, 1);
//        cstm.registerOutParameter(2, Types.FLOAT);
//        boolean re = cstm.execute();
//        System.out.println(re);
//        System.out.println(cstm.getFloat(2));
//        cstm.close();
//        getConnection().close();

        CallableStatement  cstmt = (CallableStatement) connection.prepareCall("{call add_pro(?,?,?)}");
        cstmt.setInt(1,4);
        cstmt.setInt(2,5);
        cstmt.registerOutParameter(3,Types.INTEGER);
        boolean re = cstmt.execute();
        System.out.println(re);
        System.out.println("执行结果是:"+cstmt.getInt(3));
        ResultSet resultSet = cstmt.getResultSet();
        while (resultSet.next()){
            System.out.print(resultSet.getString("name")+" ");
            System.out.print(resultSet.getString("age")+" ");
        }
    }

}
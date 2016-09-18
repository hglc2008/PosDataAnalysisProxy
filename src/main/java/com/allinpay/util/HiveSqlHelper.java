package com.allinpay.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HiveSqlHelper {

	 private  static String hiveDriverName="org.apache.hive.jdbc.HiveDriver";  
     private  static String hiveUrl="jdbc:hive2://192.168.1.81:10000/posl;auth=noSasl";  
     private  static String hiveUserName="hive";  
     private  static String hivePassword="hive";  
     private  static Connection conn=null;  

     static{  
            try {  
                     Class.forName(hiveDriverName);  
                    } catch (ClassNotFoundException e) {  
                            e.printStackTrace();  
                    }  
        }  

       public static Connection getConn(){  

            try {  
                    conn=DriverManager.getConnection(hiveUrl, hiveUserName, hivePassword);  

                    } catch (SQLException e) {  
                            e.printStackTrace();  
                    }  
            return conn;  
      }  
}

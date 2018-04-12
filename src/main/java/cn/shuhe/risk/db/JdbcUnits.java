package cn.shuhe.risk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * jdbc工具类
 * 
 * @author 
 * 
 */
public class JdbcUnits {
 
    /**
     * 数据库连接地址
     */
    private static String url ;
    private static String userName ;
    private static String password;
    private static String driver;
 
    /**
     * 装载驱动
     */
    static{
        
    }
    
    public static void init(){
    	if(url != null){
    		return;
    	}
    	DbConfig dbConfig = new DbConfig();
        url=dbConfig.getUrl();
        userName=dbConfig.getUserName();
        password=dbConfig.getPassword();
        driver=dbConfig.getDriver();
         
        try {
            Class.forName(driver);
        } catch (Exception e) {
        	System.out.println("init JdbcUtils error.");
        	e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
 
    /**
     * 建立数据库连接
     * 
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        init();
        conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }
 
    /**
     * 释放连接
     * @param conn
     */
    private static void freeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    /**
     * 释放statement
     * @param statement
     */
    private static void freeStatement(Statement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    /**
     * 释放resultset
     * @param rs
     */
    private static void freeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    /**
     * 释放资源
     * 
     * @param conn
     * @param statement
     * @param rs
     */
    public static void free(Connection conn, Statement statement, ResultSet rs) {
        if (rs != null) {
            freeResultSet(rs);
        }
        if (statement != null) {
            freeStatement(statement);
        }
        if (conn != null) {
            freeConnection(conn);
        }
    }
 
}


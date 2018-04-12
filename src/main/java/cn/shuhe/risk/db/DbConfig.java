package cn.shuhe.risk.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
	private static String driver;
    private static String url;
    private static String userName;
    private static String password;
    
    static Properties props = new Properties();
    
    public DbConfig(){   
        try {   
        	InputStream in = DbConfig.class.getClassLoader().getResourceAsStream("config/mysql.properties");
            props.load(in);  
            driver=props.getProperty("driver");
            url=props.getProperty("url");
            userName=props.getProperty("username");
            password=props.getProperty("passwrod");
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
            System.exit(-1);   
        } catch (IOException e) {          
            System.exit(-1);   
        }   
    }   
     
    public String getDriver() {
        return driver;
    }
    public String getUrl() {
        return url;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
}

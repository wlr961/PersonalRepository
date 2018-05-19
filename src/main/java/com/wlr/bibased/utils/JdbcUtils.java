package com.wlr.bibased.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author 
 *
 */
public class JdbcUtils {
	
	/**
	 * @return conn  数据库连接
	 */
	public static Connection  getConn()
	{
		Connection conn=null;
		try {
			Class.forName(ProUtils.getConfigStr("database_driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		try {
			conn = DriverManager.getConnection(ProUtils.getConfigStr("database_connstr"),ProUtils.getConfigStr("database_user"),ProUtils.getConfigStr("database_code"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	
	}
	

	/**
	 * @return 返回UUID
	 */
	public static String randomUUID() {  
		// 2018 UUID.randomUUID()
        return UUID.randomUUID().toString().replace("-", "");  
    }  
	
}

package net.macdidi.mantadia.util;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * 資料庫公用類別
 * 
 * @author macdidi
 */
public class DBUtil {
    
	// 預設的資料來源名稱
    public static final String JDBC_URL = 
    		"java:comp/env/jdbc/mantadiaDB";

    /**
     * 取得Web Container提供的資料庫連線物件
     */
    public static Connection getConnection() {
        return getConnection(JDBC_URL);
    }

    /**
     * 取得Web Container提供的資料庫連線物件
     * 
     * @param jdbcUrl JNDI資源名稱
     * @return
     */
    public static Connection getConnection(String jdbcUrl) {
        try {
            Context context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup(jdbcUrl);
            return dataSource.getConnection();
        }
        catch (Exception e) {
            return null;
        }
    }

}

package net.macdidi.mantadia.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 建立資料庫，表格和預設資料
 * 
 * @author macdidi5
 */
public class DBMain {
    
    // 資料庫伺服器位址、埠號、名稱與字元編碼
    public static final String DB_HOST = "localhost";
    public static final String DB_PORT = "3306";
    public static final String DB_NAME = "mantadia";
    public static final String DB_CHARACTER_SET = "utf8";
    
    // 資料庫帳號與密碼，根據資料庫的設定修改帳號與密碼
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    
    // 建立資料庫的SQL敘述
    public static final String CREATE_DB = String.format( 
        "CREATE DATABASE IF NOT EXISTS %s DEFAULT CHARACTER SET %s",
        DB_NAME, DB_CHARACTER_SET);
    // 建立資料庫用的JDBC連線
    public static final String CONN_STR = String.format(
        "jdbc:mysql://%s:%s/", DB_HOST, DB_PORT);
    // 連接資料庫用的JDBC連線
    public static final String CONN_DB_STR = String.format(
        "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=%s",
        DB_HOST, DB_PORT, DB_NAME, DB_CHARACTER_SET);
    // SQL敘述檔案名稱
    public static final String SQL_FILE = "mantadia.sql";

    public static void main(String[] args) {
        // 建立資料庫
        try (Connection con = DriverManager.getConnection(CONN_STR,
                USERNAME, PASSWORD);
                Statement stmt = con.createStatement()) {
            System.out.println("Create database...");
            stmt.execute(CREATE_DB);
            System.out.println("Create database done!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // SQL敘述檔案物件
        File file = new File(SQL_FILE);

        // 建立資料庫連線
        try (Connection con = DriverManager.getConnection(CONN_DB_STR,
                USERNAME, PASSWORD);
                Statement stmt = con.createStatement()) {

            // 開啟SQL敘述檔案
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file),
                            "utf8"))) {
                // 載入所有SQL敘述檔案內容
                StringBuffer sb = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                // 使用「;」拆解每一個SQL敘述
                String[] exec = sb.toString().split(";");

                // 執行所有SQL敘述
                for (String go : exec) {
                    stmt.execute(go);
                    System.out.println(go);

                    try {
                        Thread.sleep(50);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e.toString());
                    }
                }

                System.out.println("DONE!");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

}

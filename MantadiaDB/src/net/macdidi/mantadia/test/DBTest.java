package net.macdidi.mantadia.test;

import static net.macdidi.mantadia.db.DBMain.CONN_DB_STR;
import static net.macdidi.mantadia.db.DBMain.PASSWORD;
import static net.macdidi.mantadia.db.DBMain.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {

    public static void main(String[] args) {
        // 查詢所有菜單名稱的SQL敘述
        String query = "SELECT name FROM menuitem";
        
        // 建立資料庫連線與執行查詢
        try (Connection con = DriverManager.getConnection(CONN_DB_STR,
                USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // 讀取與顯示菜單名稱欄位
                String name = rs.getString("name");
                System.out.println(name);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

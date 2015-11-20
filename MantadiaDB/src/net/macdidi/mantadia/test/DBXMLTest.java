package net.macdidi.mantadia.test;

import static net.macdidi.mantadia.db.DBMain.CONN_DB_STR;
import static net.macdidi.mantadia.db.DBMain.PASSWORD;
import static net.macdidi.mantadia.db.DBMain.USERNAME;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.macdidi.mantadia.domain.User;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class DBXMLTest {

    public static void main(String[] args) {

        // 查詢指定帳號、密碼使用者資料的SQL敘述
        String query = "SELECT * FROM user WHERE account=? AND password=?";
        User user = null;

        try (Connection con = DriverManager.getConnection(CONN_DB_STR,
                USERNAME, PASSWORD);
                PreparedStatement pstmt = con.prepareStatement(query)) {
            // 設定使用者帳號與密碼
            pstmt.setString(1, "sam");
            pstmt.setString(2, "hello");

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 讀取使用者編號、帳號、密碼、姓名、性別、職務、備註與圖片編號
                    int id = rs.getInt(1);
                    String account = rs.getString(2);
                    String password = rs.getString(3);
                    String name = rs.getString(4);
                    int gender = rs.getInt(5);
                    int role = rs.getInt(6);
                    String note = rs.getString(7);
                    int imageId = rs.getInt(8);

                    // 建立使用者物件
                    user = new User(id, account, password, name, gender,
                            role, note, imageId);
                }
            }
            
            // 如果使用者讀取成功 
            if (user != null) {
                // 建立物件轉為XML的物件
                Serializer serializer = new Persister();
                // 建立輸出檔案物件
                File out = new File("user.xml");
                
                try {
                    // 把使用者物件轉換為XML文件後儲存為指定的檔案
                    serializer.write(user, out);
                    
                    // 讀取指定的XML檔案轉換為User物件
                    User read = serializer.read(User.class, out);
                    System.out.println(read);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

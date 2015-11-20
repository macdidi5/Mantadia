package net.macdidi.mantadia.test;

import static net.macdidi.mantadia.db.DBMain.CONN_DB_STR;
import static net.macdidi.mantadia.db.DBMain.PASSWORD;
import static net.macdidi.mantadia.db.DBMain.USERNAME;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.User;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class DBXMLTest2 {

    public static void main(String[] args) {
        // 查詢所有使用者的SQL敘述
        String query = "SELECT * FROM user ORDER BY id";
        ArrayList<User> result = new ArrayList<User>();

        try (Connection con = DriverManager.getConnection(CONN_DB_STR,
                USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
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
                User user = new User(id, account, password, name, gender,
                        role, note, imageId);

                result.add(user);
            }
            
            // 如果使用者資料讀取成功
            if (result.size() > 0) {
                // 建立使用者的包裝物件
                DataCollection<User> data = new DataCollection<User>();
                data.setList(result);
                
                // 建立物件轉為XML的物件
                Serializer serializer = new Persister();
                // 建立輸出檔案物件
                File out = new File("users.xml");
                
                try {
                    // 輸出所有使用者的XML資訊
                    serializer.write(data, out);
                    
                    // 讀取指定的XML檔案轉換為User包裝物件
                    DataCollection<User> read = 
                            serializer.read(DataCollection.class, out);
                    // 取得包裝的所有User物件
                    List<User> users = read.getList();
                    
                    // 顯示所有使用者資料
                    for (User u : users) {
                        System.out.println(u);
                    }
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

package net.macdidi.mantadia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import net.macdidi.mantadia.domain.User;

/**
 * 使用者DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class UserDaoImpl implements UserDao {

    @Resource(name = "jdbc/mantadiaDB")
    private DataSource dataSource;

    /**
     * 新增使用者
     * 
     * @param tables 新增使用者物件
     * @return 新增的使用者物件編號
     */
    @Override
    public int add(User user) {
        // 新增使用者資料的SQL敘述
        String sql = "INSERT INTO user VALUES(0, ?, ?, ?, ?, ?, ?, ?)";
        int result = -1;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定使用者帳號、密碼、姓名、性別、職務、備註與圖片編號
            pstmt.setString(1, user.getAccount());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setInt(4, user.getGender());
            pstmt.setInt(5, user.getRole());
            pstmt.setString(6, user.getNote());
            pstmt.setInt(7, user.getImageId());

            // 執行新增
            int rc = pstmt.executeUpdate();

            if (rc > 0) {
                // 讀取資料庫產生的編號
                ResultSet rs = pstmt.getGeneratedKeys();

                if (rs.next()) {
                    result = rs.getInt(1);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 刪除使用者
     * 
     * @param user 刪除使用者物件
     * @return 是否刪除成功 
     */
    @Override
    public boolean delete(User user) {
        // 刪除使用者資料的SQL敘述
        String sql = "DELETE FROM user WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定使用者編號
            pstmt.setInt(1, user.getId());
            
            // 執行刪除
            int rc = pstmt.executeUpdate();

            if (rc > 0) {
                result = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 修改使用者
     * 
     * @param user 修改使用者物件
     * @return 是否修改成功 
     */
    @Override
    public boolean update(User user) {
        // 修改使用者資料的SQL敘述
        String sql = "UPDATE user SET account=?, password=?, name=?, "
                + "gender=?, role=?, note=?, imageid=? WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定使用者帳號、密碼、姓名、性別、職務、備註、圖片編號與編號
            pstmt.setString(1, user.getAccount());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setInt(4, user.getGender());
            pstmt.setInt(5, user.getRole());
            pstmt.setString(6, user.getNote());
            pstmt.setInt(7, user.getImageId());
            pstmt.setInt(8, user.getId());

            // 執行修改
            int rc = pstmt.executeUpdate();

            if (rc > 0) {
                result = true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得所有使用者
     * 
     * @return 包含所有使用者的List物件
     */
    @Override
    public List<User> getAll() {
        // 查詢所有使用者資料的SQL敘述
        String sql = "SELECT * FROM user ORDER BY id";
        ArrayList<User> result = new ArrayList<User>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

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
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定編號的使用者
     * 
     * @param id 使用者物件編號
     * @return 使用者物件，如果指定的編號不存在傳回null
     */
    @Override
    public User get(int id) {
        // 查詢指定編號使用者資料的SQL敘述
        String sql = "SELECT * FROM user WHERE id=?";
        User result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定使用者編號
            pstmt.setInt(1, id);
            
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 讀取使用者帳號、密碼、姓名、性別、職務、備註與圖片編號
                String account = rs.getString(2);
                String password = rs.getString(3);
                String name = rs.getString(4);
                int gender = rs.getInt(5);
                int role = rs.getInt(6);
                String note = rs.getString(7);
                int imageId = rs.getInt(8);

                // 建立使用者物件
                result = new User(id, account, password, name, gender,
                        role, note, imageId);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得使用者,使用帳號與密碼
     * 
     * @param account 帳號
     * @param password 密碼
     * @return 使用者物件，如果帳號不存在或密碼不對傳回null
     */
    @Override
    public User get(String account, String password) {
        // 查詢指定帳號、密碼使用者資料的SQL敘述
        String sql = "SELECT * FROM user WHERE account=? AND password=?";
        User result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定使用者帳號與密碼
            pstmt.setString(1, account);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 讀取使用者編號、姓名、性別、職務、備註與圖片編號
                    int id = rs.getInt(1);
                    String name = rs.getString(4);
                    int gender = rs.getInt(5);
                    int role = rs.getInt(6);
                    String note = rs.getString(7);
                    int imageId = rs.getInt(8);

                    // 建立使用者物件
                    result = new User(id, account, password, name, gender,
                            role, note, imageId);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}

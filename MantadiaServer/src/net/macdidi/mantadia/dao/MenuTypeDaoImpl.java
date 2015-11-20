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

import net.macdidi.mantadia.domain.MenuType;

/**
 * 菜單種類DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class MenuTypeDaoImpl implements MenuTypeDao {

    @Resource(name = "jdbc/mantadiaDB")
    private DataSource dataSource;

    /**
     * 新增菜單種類
     * 
     * @param menuType 新增菜單種類物件
     * @return 新增的菜單種類物件編號
     */
    @Override
    public int add(MenuType menuType) {
        // 新增菜單種類資料的SQL敘述
        String sql = "INSERT INTO menutype VALUES(0, ?, ?)";
        int result = -1;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定菜單種類名稱與備註
            pstmt.setString(1, menuType.getName());
            pstmt.setString(2, menuType.getNote());
            
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
     * 刪除菜單種類
     * 
     * @param menuType 刪除菜單種類物件
     * @return 是否刪除成功 
     */
    @Override
    public boolean delete(MenuType menuType) {
        // 刪除菜單種類資料的SQL敘述
        String sql = "DELETE FROM menutype WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單種類編號
            pstmt.setInt(1, menuType.getId());
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
     * 修改菜單種類
     * 
     * @param menuType 修改菜單種類物件
     * @return 是否修改成功 
     */
    @Override
    public boolean update(MenuType menuType) {
        // 修改菜單種類資料的SQL敘述
        String sql = "UPDATE menutype SET name=?, note=? WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單種類名稱、備註與編號
            pstmt.setString(1, menuType.getName());
            pstmt.setString(2, menuType.getNote());
            pstmt.setInt(3, menuType.getId());
            
            // 執行更新
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
     * 取得所有菜單種類
     * 
     * @return 包含所有菜單種類的List物件
     */
    @Override
    public List<MenuType> getAll() {
        // 查詢所有菜單種類資料的SQL敘述
        String sql = "SELECT * FROM menutype ORDER BY id";
        ArrayList<MenuType> result = new ArrayList<MenuType>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取菜單種類編號、名稱與備註
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String note = rs.getString(3);

                // 建立菜單種類物件
                MenuType menuType = new MenuType(id, name, note);

                result.add(menuType);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定編號的菜單種類
     * 
     * @param id 菜單種類物件編號
     * @return 菜單種類物件，如果指定的編號不存在傳回null
     */
    @Override
    public MenuType get(int id) {
        // 查詢指定編號菜單種類資料的SQL敘述
        String sql = "SELECT * FROM menutype WHERE id=?";
        MenuType result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單種類編號
            pstmt.setInt(1, id);
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 讀取菜單種類名稱與備註
                String name = rs.getString(2);
                String note = rs.getString(3);

                // 建立菜單種類物件
                result = new MenuType(id, name, note);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}

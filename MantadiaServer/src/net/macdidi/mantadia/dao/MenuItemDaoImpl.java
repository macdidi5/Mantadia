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

import net.macdidi.mantadia.domain.MenuItem;

/**
 * 菜單DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class MenuItemDaoImpl implements MenuItemDao {
    
    @Resource(name = "jdbc/mantadiaDB")
    private DataSource dataSource;    

    /**
     * 新增菜單
     * 
     * @param menuItem 新增菜單物件
     * @return 新增的菜單物件編號
     */
    @Override
    public int add(MenuItem menuItem) {
        // 新增菜單資料的SQL敘述
        String sql = "INSERT INTO menuitem VALUES(0, ?, ?, ?, ?, ?)";
        int result = -1;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定菜單種類編號、名稱、價格、備註與圖片編號
            pstmt.setInt(1, menuItem.getMenuTypeId());
            pstmt.setString(2, menuItem.getName());
            pstmt.setInt(3, menuItem.getPrice());
            pstmt.setString(4, menuItem.getNote());
            pstmt.setInt(5, menuItem.getImageId());

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

        // 回傳編號
        return result;
    }

    /**
     * 刪除菜單
     * 
     * @param menuItem 刪除菜單物件
     * @return 是否刪除成功 
     */
    @Override
    public boolean delete(MenuItem menuItem) {
        // 刪除菜單資料的SQL敘述
        String sql = "DELETE FROM menuitem WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單編號
            pstmt.setInt(1, menuItem.getId());
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
     * 修改菜單
     *
     * @param menuItem 修改菜單物件
     * @return 是否修改成功 
     */
    @Override
    public boolean update(MenuItem menuItem) {
        // 修改菜單資料的SQL敘述
        String sql = "UPDATE menuitem SET menutypeid=?, name=?, "
                + "price=?, note=?, imageid=? WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單種類編號、名稱、價格、備註、圖片編號與菜單編號
            pstmt.setInt(1, menuItem.getMenuTypeId());
            pstmt.setString(2, menuItem.getName());
            pstmt.setInt(3, menuItem.getPrice());
            pstmt.setString(4, menuItem.getNote());
            pstmt.setInt(5, menuItem.getImageId());
            pstmt.setInt(6, menuItem.getId());

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
     * 取得所有菜單
     * 
     * @return 包含所有菜單的List物件
     */
    @Override
    public List<MenuItem> getAll() {
        // 查詢所有菜單資料的SQL敘述
        String sql = "SELECT mi.*, mt.name menutypename, im.filename "
                + "FROM menuitem mi LEFT JOIN menutype mt ON mi.menutypeid = mt.id "
                + "LEFT JOIN image im ON mi.imageid = im.id ORDER BY mi.id";
        ArrayList<MenuItem> result = new ArrayList<MenuItem>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取菜單編號、菜單種類編號、名稱、價格、備註、圖片編號、
                //   菜單種類名稱與圖片檔案名稱
                int id = rs.getInt(1);
                int menuTypeId = rs.getInt(2);
                String name = rs.getString(3);
                int price = rs.getInt(4);
                String note = rs.getString(5);
                int imageId = rs.getInt(6);
                String menuTypeName = rs.getString(7);
                String imageFileName = rs.getString(8);

                // 建立菜單物件
                MenuItem menuItem = new MenuItem(id, menuTypeId, name,
                        price, note, imageId, menuTypeName, imageFileName);

                result.add(menuItem);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定編號的菜單
     * 
     * @param id 菜單物件編號
     * @return 菜單物件，如果指定的編號不存在傳回null
     */
    @Override
    public MenuItem get(int id) {
        // 查詢指定編號菜單資料的SQL敘述
        String sql = "SELECT mi.*, mt.name menutypename, im.filename "
                + "FROM menuitem mi LEFT JOIN menutype mt ON mi.menutypeid = mt.id "
                + "LEFT JOIN image im ON mi.imageid = im.id "
                + "WHERE mi.id=?";
        MenuItem result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定菜單編號
            pstmt.setInt(1, id);
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 讀取菜單編號、菜單種類編號、名稱、價格、備註、圖片編號、
                //   菜單種類名稱與圖片檔案名稱                
                int menuTypeId = rs.getInt(2);
                String name = rs.getString(3);
                int price = rs.getInt(4);
                String note = rs.getString(5);
                int imageId = rs.getInt(6);
                String menuTypeName = rs.getString(7);
                String imageFileName = rs.getString(8);

                // 建立菜單物件
                result = new MenuItem(id, menuTypeId, name, price, note,
                        imageId, menuTypeName, imageFileName);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}

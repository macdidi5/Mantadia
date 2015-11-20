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

import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.domain.Tables;

/**
 * 餐桌DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class TablesDaoImpl implements TablesDao {

    @Resource(name = "jdbc/mantadiaDB")
    private DataSource dataSource;
    
    /**
     * 新增餐桌
     * 
     * @param tables 新增餐桌物件
     * @return 新增的餐桌物件編號
     */
    @Override
    public int add(Tables tables) {
        // 新增餐桌資料的SQL敘述
        String sql = "INSERT INTO tables VALUES(0, ?, ?)";
        int result = -1;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定狀態與備註
            pstmt.setInt(1, tables.getStatus());
            pstmt.setString(2, tables.getNote());

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
     * 刪除餐桌
     * 
     * @param tables 刪除餐桌種類物件
     * @return 是否刪除成功 
     */
    @Override
    public boolean delete(Tables tables) {
        // 刪除餐桌資料的SQL敘述
        String sql = "DELETE FROM tables WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定餐桌編號
            pstmt.setInt(1, tables.getId());

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
     * 修改餐桌
     * 
     * @param tables 修改餐桌種類物件
     * @return 是否修改成功 
     */
    @Override
    public boolean update(Tables tables) {
        // 修改餐桌資料的SQL敘述
        String sql = "UPDATE tables SET status=?, note=? WHERE id=?";
        boolean result = false;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定狀態、備註與餐桌編號
            pstmt.setInt(1, tables.getStatus());
            pstmt.setString(2, tables.getNote());
            pstmt.setInt(3, tables.getId());

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
     * 取得所有餐桌
     * 
     * @return 包含所有餐桌的List物件
     */
    @Override
    public List<Tables> getAll() {
        // 查詢所有餐桌資料的SQL敘述
        String sql = "SELECT * FROM tables ORDER BY id";
        ArrayList<Tables> result = new ArrayList<Tables>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取餐桌編號、狀態與備註
                int id = rs.getInt(1);
                int status = rs.getInt(2);
                String note = rs.getString(3);

                // 建立餐桌物件
                Tables tables = new Tables(id, status, note);

                result.add(tables);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定編號的餐桌
     * 
     * @param id 餐桌物件編號
     * @return 餐桌物件，如果指定的編號不存在傳回null
     */
    @Override
    public Tables get(int id) {
        // 查詢指定編號餐桌資料的SQL敘述
        String sql = "SELECT * FROM tables WHERE id=?";
        Tables result = null;

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定餐桌編號
            pstmt.setInt(1, id);
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 讀取狀態與備註
                int status = rs.getInt(2);
                String note = rs.getString(3);

                // 建立餐桌物件
                result = new Tables(id, status, note);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得所有餐桌資訊
     * 
     * @return 包含所有餐桌資訊的List物件
     */
    @Override
    public List<Tables> getAllStatus() {
        // 查詢所有餐桌資訊的SQL敘述
        String sql = "SELECT t.id, t.status, o.id orderid, o.time, u.name username, o.number orderNumber, IFNULL(o.status, -1) orderstatus "
                + "FROM tables t LEFT OUTER JOIN (SELECT * FROM orders WHERE status<>3) o ON t.id = o.tablesid  "
                + "LEFT OUTER JOIN user u ON o.userid = u.id ORDER BY t.id ";
        return processTableQuery(sql);
    }

    /**
     * 取得可以換桌的餐桌資訊
     * 
     * @return 包含所有可以換桌的餐桌資訊的List物件
     */
    @Override
    public List<Tables> getTablesChange() {
        // 查詢可以換桌餐桌資訊的SQL敘述
        String sql = "SELECT t.id, t.status, o.id orderid, o.time, " 
                + "u.name username, o.number orderNumber, " 
                + "IFNULL(o.status, -1) orderstatus "
                + "FROM tables t LEFT OUTER JOIN "
                + "(SELECT * FROM orders WHERE status IN (0, 1, 2)) o "
                + "ON t.id = o.tablesid  "
                + "LEFT OUTER JOIN user u ON o.userid = u.id "
                + "WHERE o.id IS NOT NULL ORDER BY t.id";
        return processTableQuery(sql);
    }

    /**
     * 取得有客人的餐桌資訊
     * 
     * @return 包含所有有客人的餐桌資訊的List物件
     */
    @Override
    public List<Tables> getTablesNotEmpty() {
        // 查詢有客人餐桌資訊的SQL敘述
        String sql = "SELECT t.id, t.status, o.id orderid, o.time, "
                + "u.name username, o.number orderNumber, "
                + "IFNULL(o.status, -1) orderstatus "
                + "FROM tables t LEFT OUTER JOIN "
                + "(SELECT * FROM orders WHERE status<>3) o "
                + "ON t.id = o.tablesid LEFT OUTER JOIN user u "
                + "ON o.userid = u.id WHERE o.id IS NOT NULL "
                + "ORDER BY t.id";
        return processTableQuery(sql);
    }

    private List<Tables> processTableQuery(String sql) {
        ArrayList<Tables> result = new ArrayList<Tables>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取餐桌編號與狀態
                int tablesId = rs.getInt(1);
                int status = rs.getInt(2);

                // 建立餐桌物件
                Tables tables = new Tables(tablesId, status, "");

                // 讀取餐桌編號、訂單編號、日期時間、使用者名稱、人數與狀態
                int ordersId = rs.getInt(3);
                String time = rs.getString(4);
                String userName = rs.getString(5);
                int orderNumber = rs.getInt(6);
                int orderStatus = rs.getInt(7);

                // 建立訂單物件
                Orders orders = new Orders(ordersId, time, 0, tablesId,
                        orderNumber, orderStatus, "", userName);
                tables.setOrders(orders);

                result.add(tables);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得沒有客人的餐桌
     * 
     * @return 包含所有有客人的餐桌資訊的List物件
     */
    @Override
    public List<Tables> getTablesEmpty() {
        // 查詢沒有客人餐桌的SQL敘述
        String sql = "SELECT * FROM tables WHERE status=0";
        ArrayList<Tables> result = new ArrayList<Tables>();

        try (Connection con = dataSource.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取餐桌編號、狀態與備註
                int id = rs.getInt(1);
                int status = rs.getInt(2);
                String note = rs.getString(3);

                // 建立餐桌物件
                Tables tables = new Tables(id, status, note);
                // 設定餐桌的訂單
                tables.setOrders(new Orders());

                result.add(tables);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 設定餐桌狀態
     * 
     * @param id 餐桌物件編號
     * @param status 餐桌狀態，0：空桌，1:有客人
     */
    @Override
    public void setTablesStatus(int id, int status) {
        // 修改餐桌狀態的SQL敘述
        String sql = "UPDATE tables SET status=? WHERE id=?";

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定餐桌狀態與編號
            pstmt.setInt(1, status);
            pstmt.setInt(2, id);

            // 執行修改
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

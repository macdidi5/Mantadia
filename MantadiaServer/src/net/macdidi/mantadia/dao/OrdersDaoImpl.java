package net.macdidi.mantadia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.util.DBUtil;

/**
 * 訂單DAO實作
 * 
 * @author macdidi
 */
@Named
@ApplicationScoped
public class OrdersDaoImpl implements OrdersDao {

    private TablesDao tablesDao;
    
    public OrdersDaoImpl() {
        tablesDao = new TablesDaoImpl();
    }
    
    /**
     * 新增訂單
     * 
     * @param orders 新增訂單物件
     * @return 新增的訂單物件編號
     */
    @Override
    public int addOrder(Orders orders) {
        // 新增訂單資料的SQL敘述
        String sql = "INSERT INTO orders VALUES(0, ?, ?, ?, ?, ?, ?)";
        int result = -1;

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定訂單日期時間、使用者編號、餐桌編號、人數、狀態與備註
            pstmt.setString(1, orders.getTime());
            pstmt.setInt(2, orders.getUserId());
            pstmt.setInt(3, orders.getTablesId());
            pstmt.setInt(4, orders.getNumber());
            pstmt.setInt(5, orders.getStatus());
            pstmt.setString(6, orders.getNote());

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
     * 修改訂單
     * 
     * @param id 訂單編號
     * @param number 人數
     */
    @Override
    public void updateOrder(int id, int number) {
        // 刪除訂單資料的SQL敘述
        String sqlOrders = "UPDATE orders SET number=? WHERE id=?";
        // 刪除訂明細單資料的SQL敘述
        String sqlOrderItem = "DELETE FROM orderitem WHERE ordersid=?";

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sqlOrders);
                PreparedStatement pstmt2 = con.prepareStatement(sqlOrderItem)) {
            // 設定訂單人數與編號
            pstmt.setInt(1, number);
            pstmt.setInt(2, id);
            // 執行更新訂單
            pstmt.executeUpdate();

            // 設定訂單編號
            pstmt2.setInt(1, id);
            // 執行刪除訂單明細
            pstmt2.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增訂單明細
     * 
     * @param orderItem 新增的訂單明細物件
     * @return 新增的訂單明細物件編號
     */
    @Override
    public int addOrderItem(OrderItem orderItem) {
        // 新增訂單明細資料的SQL敘述
        String sql = "INSERT INTO orderitem VALUES(0, ?, ?, ?, ?, ?)";
        int result = -1;

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
            // 設定訂單編號、菜單編號、數量、狀態與備註
            pstmt.setInt(1, orderItem.getOrdersId());
            pstmt.setInt(2, orderItem.getMenuItemId());
            pstmt.setInt(3, orderItem.getNumber());
            pstmt.setInt(4, orderItem.getStauts());
            pstmt.setString(5, orderItem.getNote());

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
     * 取得指定編號的訂單
     * 
     * @param id 訂單物件編號
     * @return 訂單物件，如果指定的編號不存在傳回null
     */
    @Override
    public Orders getOrder(int id) {
        // 查詢指定編號訂單資料的SQL敘述
        String sql = "SELECT * FROM orders WHERE id=?";
        Orders result = null;

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定訂單編號
            pstmt.setInt(1, id);
            // 執行查詢
            ResultSet rs = pstmt.executeQuery();            

            if (rs.next()) {
                // 讀取訂單日期時間、使用者編號、餐桌編號、人數、狀態與備註
                String time = rs.getString(2);
                int userId = rs.getInt(3);
                int tablesId = rs.getInt(4);
                int number = rs.getInt(5);
                int status = rs.getInt(6);
                String note = rs.getString(7);

                // 建立訂單物件
                result = new Orders(id, time, userId, tablesId, number,
                        status, note);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定日期的訂單
     * 
     * @param date 指定的日期
     * @return 包含訂單的List物件
     */
    @Override
    public List<Orders> getOrderByDate(String date) {
        // 查詢指定日期訂單資料的SQL敘述
        String sql = "SELECT o.*, u.name, od.amount "
                + "FROM orders o JOIN user u ON o.userid = u.id  "
                + "JOIN (SELECT ordersid, SUM(price * number) amount "
                + "FROM orderitem od JOIN menuitem m ON od.menuitemid = m.id "
                + "GROUP BY ordersid) od ON o.id = od.ordersid "
                + "WHERE time LIKE '" + date + "%'";

        List<Orders> result = new ArrayList<Orders>();

        try (Connection con = DBUtil.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取訂單編號、日期時間、使用者編號、餐桌編號、人數、狀態、備註、
                //   使用者名稱與訂單金額
                int id = rs.getInt(1);
                String time = rs.getString(2);
                int userId = rs.getInt(3);
                int tablesId = rs.getInt(4);
                int number = rs.getInt(5);
                int status = rs.getInt(6);
                String note = rs.getString(7);
                String userName = rs.getString(8);
                int amount = rs.getInt(9);

                // 建立訂單物件
                Orders orders = new Orders(id, time, userId, tablesId,
                        number, status, note, userName, amount);
                result.add(orders);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得指定訂單編號的訂單明細
     * 
     * @param id 訂單物件編號
     * @return 包含訂單明細的List物件
     */
    @Override
    public List<OrderItem> getOrderItems(int ordersId) {
        // 查詢指定訂單編號明細資料的SQL敘述
        String sql = "SELECT @rownum:=@rownum+1 serial, m.name, m.price, " 
                + "oi.number, oi.number * m.price amount, menuitemid, oi.note "
                + "FROM orderitem oi LEFT JOIN menuitem m ON " 
                + "(oi.menuitemid = m.id), (SELECT @rownum:=0) r  "
                + "WHERE ordersid=" + ordersId;
        List<OrderItem> result = new ArrayList<OrderItem>();

        try (Connection con = DBUtil.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取序號、菜單名稱、價格、數量、金額、菜單編號與備註
                int serial = rs.getInt(1);
                String name = rs.getString(2);
                int price = rs.getInt(3);
                int number = rs.getInt(4);
                int amount = rs.getInt(5);
                int menuItemId = rs.getInt(6);
                String note = rs.getString(7);

                // 建立訂單明細物件
                OrderItem orderItem = new OrderItem(0, ordersId,
                        menuItemId, number, 0, note, name, price, amount,
                        serial);
                result.add(orderItem);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 取得未完成的訂單
     * 
     * @return 包含所有未完成訂單的List物件
     */
    @Override
    public List<Orders> getOrderNotComplete() {
        // 查詢未完成訂單資料的SQL敘述
        String sql = "SELECT * FROM orders WHERE status IN (0, 1)";
        List<Orders> result = new ArrayList<Orders>();

        try (Connection con = DBUtil.getConnection();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // 讀取訂單編號、日期時間、使用者編號、餐桌編號、人數、狀態與備註
                int id = rs.getInt(1);
                String time = rs.getString(2);
                int userId = rs.getInt(3);
                int tablesId = rs.getInt(4);
                int number = rs.getInt(5);
                int status = rs.getInt(6);
                String note = rs.getString(7);

                // 建立訂單物件
                Orders orders = new Orders(id, time, userId, tablesId,
                        number, status, note);

                result.add(orders);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 設定指定編號的訂單狀態為準備中
     * 
     * @param id 訂單物件編號
     */
    @Override
    public void setOrderPrepare(int id) {
        setOrderStatus(id, 1);
    }

    /**
     * 設定指定編號的訂單狀態為已完成
     * 
     * @param id 訂單物件編號
     */
    @Override
    public void setOrderDone(int id) {
        setOrderStatus(id, 2);
    }

    /**
     * 更改訂單餐桌編號
     * 
     * @param id 訂單物件編號
     * @param oldTablesId 原來的餐桌編號
     * @param newTablesId 新的餐桌編號
     */
    @Override
    public void changeOrderTable(int id, int oldTablesId, int newTablesId) {
        // 更改訂單餐桌編號的SQL敘述
        String sql = "UPDATE orders SET tablesid=? WHERE id=?";

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定餐桌編號與訂單編號
            pstmt.setInt(1, newTablesId);
            pstmt.setInt(2, id);

            // 執行修改
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 處理訂單結帳
     * 
     * @param ordersId 訂單物件編號
     * @param tablesId 餐桌編號
     */
    @Override
    public void checkOut(int ordersId, int tablesId) {
        // 設定訂單狀態為完成
        setOrderStatus(ordersId, 3);
    }

    private void setOrderStatus(int orderId, int status) {
        // 更改訂單狀態的SQL敘述
        String sql = "UPDATE orders SET status=? WHERE id=?";

        try (Connection con = DBUtil.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            // 設定訂單狀態與訂單編號
            pstmt.setInt(1, status);
            pstmt.setInt(2, orderId);
            // 執行修改
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

package net.macdidi.mantadia.dao;

import java.util.List;

import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;

/**
 * 訂單DAO介面
 * 
 * @author macdidi
 */
public interface OrdersDao {
    /**
     * 新增訂單
     * 
     * @param orders 新增訂單物件
     * @return 新增的訂單物件編號
     */
    public int addOrder(Orders orders);
    
    /**
     * 修改訂單
     * 
     * @param id 訂單編號
     * @param number 人數
     */
    public void updateOrder(int id, int number);

    /**
     * 新增訂單明細
     * 
     * @param orderItem 新增的訂單明細物件
     * @return 新增的訂單明細物件編號
     */
    public int addOrderItem(OrderItem orderItem);

    /**
     * 取得指定編號的訂單
     * 
     * @param id 訂單物件編號
     * @return 訂單物件，如果指定的編號不存在傳回null
     */
    public Orders getOrder(int id);

    /**
     * 取得指定日期的訂單
     * 
     * @param date 指定的日期
     * @return 包含訂單的List物件
     */
    public List<Orders> getOrderByDate(String date);

    /**
     * 取得指定訂單編號的訂單明細
     * 
     * @param id 訂單物件編號
     * @return 包含訂單明細的List物件
     */
    public List<OrderItem> getOrderItems(int id);

    /**
     * 取得未完成的訂單
     * 
     * @return 包含所有未完成訂單的List物件
     */
    public List<Orders> getOrderNotComplete();

    /**
     * 設定指定編號的訂單狀態為準備中
     * 
     * @param id 訂單物件編號
     */
    public void setOrderPrepare(int id);

    /**
     * 設定指定編號的訂單狀態為已完成
     * 
     * @param id 訂單物件編號
     */
    public void setOrderDone(int id);

    /**
     * 更改訂單餐桌編號
     * 
     * @param id 訂單物件編號
     * @param oldTablesId 原來的餐桌編號
     * @param newTablesId 新的餐桌編號
     */
    public void changeOrderTable(int id, int oldTablesId, int newTablesId);

    /**
     * 處理訂單結帳
     * 
     * @param ordersId 訂單物件編號
     * @param tablesId 餐桌編號
     */
    public void checkOut(int ordersId, int tablesId);
}

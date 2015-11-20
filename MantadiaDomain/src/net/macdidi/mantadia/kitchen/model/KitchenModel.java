package net.macdidi.mantadia.kitchen.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.kitchen.view.KitchenView;

/**
 * 廚房Model介面
 * 
 * @author macdidi
 */
public interface KitchenModel extends Remote {
    
    // RMI伺服器服務埠號
    public static final int RMI_SERVICE_PORT = 6508;
    
    // RMI服務名稱
    public static final String RMI_SERVICE_NAME = "kitchen";
    
    /**
     * 新增通知的View物件
     * 
     * @param view 用戶端View物件
     * @throws RemoteException
     */
    public void addKitchenView(KitchenView view) throws RemoteException;

    /**
     * 移除通知的View物件
     * 
     * @param view 用戶端View物件
     * @throws RemoteException
     */
    public void removeKitchenView(KitchenView view) throws RemoteException;

    /**
     * 處理新增或修改訂單
     * 
     * @param orders 訂單物件
     * @param orderItem 訂單明細物件
     * @throws RemoteException
     */
    public void processOrders(Orders orders, List<OrderItem> orderItem)
            throws RemoteException;

    /**
     * 處理訂單結帳
     * 
     * @param orders 訂單物件
     * @throws RemoteException
     */
    public void processCheckOut(Orders orders) throws RemoteException;

    /**
     * 設定訂單狀態為準備中
     * 
     * @param orderId 訂單編號
     */
    public void setOrderPrepare(int orderId) throws RemoteException;

    /**
     * 設定訂單狀態為已完成
     * 
     * @param orderId 訂單編號
     */
    public void setOrderDone(int orderId) throws RemoteException;
}

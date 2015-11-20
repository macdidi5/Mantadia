package net.macdidi.mantadia.kitchen.view;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;

/**
 * 廚房View介面
 * 
 * @author macdidi
 */
public interface KitchenView extends Remote {
    /**
     * 處理新增或修改訂單
     * 
     * @param orders 訂單物件
     * @param orderItem 訂單明細物件
     * @throws RemoteException
     */
    public void handleOrders(Orders orders, List<OrderItem> orderItem)
            throws RemoteException;

    /**
     * 處理訂單結帳
     * 
     * @param orders 訂單物件
     * @throws RemoteException
     */
    public void handleCheckOut(Orders orders) throws RemoteException;
}

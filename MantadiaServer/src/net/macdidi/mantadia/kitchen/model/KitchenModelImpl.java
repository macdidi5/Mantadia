package net.macdidi.mantadia.kitchen.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.dao.OrdersDaoImpl;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.kitchen.view.KitchenView;

/**
 * 廚房Model實作
 * 
 * @author macdidi
 */
public class KitchenModelImpl extends UnicastRemoteObject 
        implements KitchenModel {

    private List<KitchenView> views;

    public KitchenModelImpl() throws RemoteException {
        // 建立保存廚房用戶端View用的List物件
        views = new ArrayList<KitchenView>();
    }

    /**
     * 新增通知的View物件，提供給廚房用戶端呼叫
     */
    @Override
    public void addKitchenView(KitchenView view) throws RemoteException {
        // 加入一個廚房用戶端View物件
        views.add(view);
        // 讀取訂單資訊與通知用戶端
        readOrdersOnTable(view);
    }

    private void readOrdersOnTable(KitchenView view) throws RemoteException {
        // 建立訂單DAO物件
        OrdersDao ordersDao = new OrdersDaoImpl();
        // 取得未結帳的訂單資訊
        List<Orders> orders = ordersDao.getOrderNotComplete();

        for (Orders order : orders) {
            // 取得訂單明細
            List<OrderItem> orderItem = 
                    ordersDao.getOrderItems(order.getId());

            // 通知用戶端接收與處理訂單資訊
            view.handleOrders(order, orderItem);
        }
    }

    /**
     * 移除通知的View物件，提供給廚房用戶端呼叫
     */
    @Override
    public void removeKitchenView(KitchenView view) throws RemoteException {
        // 移除一個廚房用戶端View物件
        views.remove(view);
    }

    /**
     * 處理新增或修改訂單
     */
    @Override
    public void processOrders(Orders orders, List<OrderItem> orderItem)
            throws RemoteException {
        // 通知所有廚房用戶端接收與處理訂單資訊
        for (KitchenView view : views) {
            view.handleOrders(orders, orderItem);
        }
    }

    /**
     * 處理訂單結帳
     */
    @Override
    public void processCheckOut(Orders orders) throws RemoteException {
        // 通知所有廚房用戶端處理訂單結帳
        for (KitchenView view : views) {
            view.handleCheckOut(orders);
        }
    }

    /**
     * 設定訂單狀態為準備中，提供給廚房用戶端呼叫
     */
    @Override
    public void setOrderPrepare(int orderId) throws RemoteException {
        // 建立訂單DAO物件
        OrdersDao dao = new OrdersDaoImpl();
        // 設定訂單的狀態為準備中
        dao.setOrderPrepare(orderId);
    }

    /**
     * 設定訂單狀態為已完成，提供給廚房用戶端呼叫
     */
    @Override
    public void setOrderDone(int orderId) throws RemoteException {
        // 建立訂單DAO物件
        OrdersDao dao = new OrdersDaoImpl();
        // 設定訂單的狀態為已完成
        dao.setOrderDone(orderId);
    }

}
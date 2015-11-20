package net.macdidi.mantadia.kitchen.view;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javafx.application.Platform;
import javafx.stage.Stage;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.kitchen.gui.KitchenGUI;
import net.macdidi.mantadia.kitchen.model.KitchenModel;

/**
 * 廚房View介面實作
 * 
 * @author macdidi
 */
public class KitchenViewImpl extends UnicastRemoteObject 
        implements KitchenView {

    private KitchenGUI gui;

    public KitchenViewImpl(KitchenModel model, Stage primaryStage)
            throws RemoteException {
        // 建立畫面
        gui = new KitchenGUI(model, this, primaryStage);
        // 加入用戶端
        model.addKitchenView(this);
    }

    /**
     * 處理新增或修改訂單
     */
    @Override
    public void handleOrders(final Orders orders,
            final List<OrderItem> orderItem) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gui.addOrders(orders, orderItem);
            }
        });
    }

    /**
     * 處理訂單結帳
     */
    @Override
    public void handleCheckOut(final Orders orders) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gui.removeOrders(orders);
            }
        });
    }

}

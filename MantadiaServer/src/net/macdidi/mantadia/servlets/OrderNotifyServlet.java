package net.macdidi.mantadia.servlets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.domain.OrderItem;
import net.macdidi.mantadia.domain.Orders;
import net.macdidi.mantadia.kitchen.model.KitchenModel;

/**
 * 通知廚房（新增訂單，修改訂單，結帳，換桌）
 * 
 * @author macdidi
 */
@WebServlet(name = "OrderNotifyServlet", 
            urlPatterns = { "/mobile/OrderNotifyServlet.do" })
public class OrderNotifyServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取訂單編號與種類
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        String type = request.getParameter("type");

        // 取得訂單物件
        Orders orders = ordersDao.getOrder(ordersId);
        // 取得通知廚房用的Model物件
        KitchenModel model = (KitchenModel) getServletContext()
                .getAttribute("KitchenModel");

        if (model != null) {
            // 通知更新訂單
            if (type.equals("ORDERS")) {
                List<OrderItem> orderItem = 
                        ordersDao.getOrderItems(ordersId);
                model.processOrders(orders, orderItem);
            }
            // 通知結帳
            else if (type.equals("CHECKOUT")) {
                model.processCheckOut(orders);
            }
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

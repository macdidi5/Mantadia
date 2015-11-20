package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;

/**
 * 修改訂單
 * 
 * @author macdidi
 */
@WebServlet(name = "UpdateOrdersServlet", 
            urlPatterns = { "/mobile/UpdateOrdersServlet.do" })
public class UpdateOrdersServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取訂單編號與人數
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        int ordersNumber = Integer.parseInt(
                request.getParameter("ordersNumber"));
        // 修改訂單
        ordersDao.updateOrder(ordersId, ordersNumber);
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

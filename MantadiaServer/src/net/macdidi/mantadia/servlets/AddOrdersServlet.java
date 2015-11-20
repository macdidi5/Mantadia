package net.macdidi.mantadia.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.dao.TablesDao;
import net.macdidi.mantadia.domain.Orders;

/**
 * 新增訂單
 * 
 * @author macdidi
 */
@WebServlet(name = "AddOrdersServlet", 
            urlPatterns = { "/mobile/AddOrdersServlet.do" })
public class AddOrdersServlet extends HttpServlet {

    @Inject
    private TablesDao tablesDao;
    @Inject
    private OrdersDao ordersDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為文字
        response.setContentType("text/plain");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 讀取訂單日期時間、使用者編號、餐桌編號與訂單編號
        String ordersTime = request.getParameter("ordersTime");
        int userId = Integer.parseInt(request.getParameter("userId"));
        int tablesId = Integer.parseInt(request.getParameter("tablesId"));
        int ordersNumber = Integer.parseInt(
                request.getParameter("ordersNumber"));

        // 建立要新增的訂單物件
        Orders orders = new Orders(0, ordersTime, userId, tablesId, 
                ordersNumber, 0, "");
        // 新增訂單並取得訂單編號
        int ordersId = ordersDao.addOrder(orders);

        // 設定訂單狀態為「有客人」
        tablesDao.setTablesStatus(tablesId, 1);

        // 回應訂單編號
        out.print(ordersId);
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

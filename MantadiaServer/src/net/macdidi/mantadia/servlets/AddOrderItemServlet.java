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
import net.macdidi.mantadia.domain.OrderItem;

/**
 * 新增訂單明細
 * 
 * @author macdidi
 */
@WebServlet(name = "AddOrderItemServlet", 
            urlPatterns = { "/mobile/AddOrderItemServlet.do" })
public class AddOrderItemServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為文字
        response.setContentType("text/plain");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 讀取菜單編號、訂單編號、數量與備註
        int menuItemId = Integer.parseInt(
                request.getParameter("menuItemId"));
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        int number = Integer.parseInt(request.getParameter("number"));
        String note = request.getParameter("note");

        // 建立要新增的菜單物件
        OrderItem orderItem = new OrderItem(0, ordersId, menuItemId,
                number, 0, note);
        // 新增菜單物件並取得訂單明細編號
        int orderItemId = ordersDao.addOrderItem(orderItem);

        // 回應訂單明細編號
        out.print(orderItemId);
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

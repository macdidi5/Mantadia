package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.dao.TablesDao;

/**
 * 結帳
 * 
 * @author macdidi
 */
@WebServlet(name = "CheckOutServlet", 
            urlPatterns = { "/mobile/CheckOutServlet.do" })
public class CheckOutServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;
    @Inject
    private TablesDao tablesDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取訂單編號與餐桌編號
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        int tablesId = Integer.parseInt(request.getParameter("tablesId"));
        // 結帳
        ordersDao.checkOut(ordersId, tablesId);
        // 設定餐桌狀態為沒有客人
        tablesDao.setTablesStatus(tablesId, 0);
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

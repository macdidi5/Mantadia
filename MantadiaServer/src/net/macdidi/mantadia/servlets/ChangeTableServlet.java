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
 * 換桌
 * 
 * @author macdidi
 */
@WebServlet(name = "ChangeTableServlet", 
            urlPatterns = { "/mobile/ChangeTableServlet.do" })
public class ChangeTableServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;
    @Inject
    private TablesDao tablesDao;    

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取訂單編號、原來與新的餐桌編號
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        int oldTablesId = Integer.parseInt(
                request.getParameter("oldTablesId"));
        int newTablesId = Integer.parseInt(
                request.getParameter("newTablesId"));

        // 修改訂單的餐桌編號
        ordersDao.changeOrderTable(ordersId, oldTablesId, newTablesId);
        
        // 原來的餐桌設定為空桌
        tablesDao.setTablesStatus(oldTablesId, 0);
        // 設定新餐桌為有客人
        tablesDao.setTablesStatus(newTablesId, 1);        
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

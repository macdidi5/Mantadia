package net.macdidi.mantadia.servlets;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.domain.Orders;

/**
 * 取得指定日期的訂單資訊
 * 
 * @author macdidi
 */
@WebServlet(name = "DailyReportServlet", 
            urlPatterns = { "/daily_report.do" })
public class DailyReportServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取年、月、日
        String year = request.getParameter("year");
        int month = Integer.parseInt(request.getParameter("month"));
        int day = Integer.parseInt(request.getParameter("day"));
        
        // 轉換日期格式
        String formatDate = String.format("%s-%02d-%02d", year, month, day);
        // 取得指定日期的訂單資訊
        List<Orders> orders = ordersDao.getOrderByDate(formatDate);
        
        // 計算訂單合計
        int total = 0;
        
        for (Orders order : orders) {
            total += order.getAmount();
        }
        
        // 設定訂單資訊、訂單合計與數量為Request範圍的Attribute
        request.setAttribute("orders", orders);
        request.setAttribute("total", total);
        request.setAttribute("size", orders.size());
        
        // 轉向日報表元件
        RequestDispatcher view = request.getRequestDispatcher("daily_report.jsp");
        view.forward(request, response);        
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

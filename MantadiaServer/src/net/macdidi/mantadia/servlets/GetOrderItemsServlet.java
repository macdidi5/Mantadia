package net.macdidi.mantadia.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.OrdersDao;
import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.OrderItem;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 取得訂單明細
 * 
 * @author macdidi
 */
@WebServlet(name = "GetOrderItemsServlet", 
            urlPatterns = { "/mobile/GetOrderItemsServlet.do" })
public class GetOrderItemsServlet extends HttpServlet {

    @Inject
    private OrdersDao ordersDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 讀取訂單編號
        int ordersId = Integer.parseInt(request.getParameter("ordersId"));
        // 取得所有訂單明細
        List<OrderItem> orderItems = ordersDao.getOrderItems(ordersId);

        // 建立訂單明細的包裝物件
        DataCollection<OrderItem> data = new DataCollection<OrderItem>();
        data.setList(orderItems);
        
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();
        
        try {
            // 輸出所有訂單明細的XML資訊
            serializer.write(data, out);
        }
        catch (Exception e) {
            throw new IOException(e);
        }        

        out.flush();
        out.close();
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

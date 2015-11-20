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

import net.macdidi.mantadia.dao.TablesDao;
import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Tables;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 餐桌狀態
 * 
 * @author macdidi
 */
@WebServlet(name = "TableStatusServlet", 
            urlPatterns = { "/mobile/TableStatusServlet.do" })
public class TableStatusServlet extends HttpServlet {

    @Inject
    private TablesDao tablesDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取種類
        String type = request.getParameter("type");
        // 如果沒有種類資料，就設定為全部「ALL」
        type = (type == null) ? "ALL" : type;

        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        List<Tables> tableList = null;

        // 全部餐桌資訊
        if (type.equals("ALL")) {
            tableList = tablesDao.getAllStatus();
        }
        // 可以換桌的餐桌資訊（沒有客人）
        else if (type.equals("CHANGE")) {
            tableList = tablesDao.getTablesChange();
        }
        // 有客人的餐桌資訊
        else if (type.equals("NOT_EMPTY")) {
            tableList = tablesDao.getTablesNotEmpty();
        }
        // 沒有客人的餐桌資訊
        else if (type.equals("EMPTY")) {
            tableList = tablesDao.getTablesEmpty();
        }

        // 建立餐桌資訊的包裝物件
        DataCollection<Tables> data = new DataCollection<Tables>();
        data.setList(tableList);
        
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();
        
        try {
            // 輸出所有餐桌資訊的XML資訊
            serializer.write(data, out);
        }
        catch (Exception e) {
            throw new IOException(e);
        }                   
        
        out.flush();
        out.close();
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}

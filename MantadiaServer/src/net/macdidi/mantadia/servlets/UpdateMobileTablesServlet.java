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
 * 更新餐桌資料
 * 
 * @author macdidi
 */
@WebServlet(name = "UpdateMobileTablesServlet", 
            urlPatterns = { "/mobile/UpdateMobileTablesServlet.do" })
public class UpdateMobileTablesServlet extends HttpServlet {

    @Inject
    private TablesDao tablesDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 取得所所有餐桌物件
        List<Tables> tables = tablesDao.getAll();
        
        // 建立餐桌的包裝物件
        DataCollection<Tables> data = new DataCollection<Tables>();
        data.setList(tables);
        
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();
        
        try {
            // 輸出所有餐桌的XML資訊
            serializer.write(data, out);
        }
        catch (Exception e) {
            throw new IOException(e);
        }                

        out.flush();
        out.close();
    }

}

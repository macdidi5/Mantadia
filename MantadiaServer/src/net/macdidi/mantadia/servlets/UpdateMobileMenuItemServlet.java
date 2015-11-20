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

import net.macdidi.mantadia.dao.MenuItemDao;
import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.MenuItem;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 更新菜單資料
 * 
 * @author macdidi
 */
@WebServlet(name = "UpdateMobileMenuItemServlet", 
            urlPatterns = { "/mobile/UpdateMobileMenuItemServlet.do" })
public class UpdateMobileMenuItemServlet extends HttpServlet {

    @Inject
    private MenuItemDao menuItemDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 取得所所有菜單物件
        List<MenuItem> menuItems = menuItemDao.getAll();
        
        // 建立菜單的包裝物件
        DataCollection<MenuItem> data = new DataCollection<MenuItem>();
        data.setList(menuItems);
        
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();
        
        try {
            // 輸出所有菜單的XML資訊
            serializer.write(data, out);
        }
        catch (Exception e) {
            throw new IOException(e);
        }                

        out.flush();
        out.close();
    }

}

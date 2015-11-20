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

import net.macdidi.mantadia.dao.ImageDao;
import net.macdidi.mantadia.domain.DataCollection;
import net.macdidi.mantadia.domain.Image;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 取得所有圖片檔案名稱
 * 
 * @author macdidi
 */
@WebServlet(name = "ImageFileNameServlet", 
            urlPatterns = { "/mobile/ImageFileNameServlet.get" })
public class ImageFileNameServlet extends HttpServlet {

    @Inject
    private ImageDao imageDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();
        // 取得所有圖片物件
        List<Image> imageList = imageDao.getAll();
        
        // 建立圖片的包裝物件
        DataCollection<Image> data = new DataCollection<Image>();
        data.setList(imageList);
        
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();
        
        try {
            // 輸出所有圖片的XML資訊
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

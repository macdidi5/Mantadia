package net.macdidi.mantadia.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.util.ImageUtil;

/**
 * 更新圖片
 * 
 * @author macdidi
 */
@WebServlet(name = "UpdateImageServlet", 
            urlPatterns = { "/mobile/UpdateImageServlet.do" })
public class UpdateImageServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取圖片檔案名稱
        String fileName = request.getParameter("fileName");
        
        if (fileName != null) {
            // 建立圖片檔案物件
            File imageFile = new File(ImageUtil.UPLOAD_FOLDER, fileName);

            if (imageFile.exists()) {
                // 讀取圖片檔案
                BufferedImage original = ImageIO.read(imageFile);
                // 設定回應型態為圖片
                response.setContentType("image/jpeg");
                // 取得回應輸出串流物件
                ServletOutputStream out = response.getOutputStream();
                // 回應圖片
                ImageIO.write(original, "jpeg", out);
                out.close();
            }            
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }

}
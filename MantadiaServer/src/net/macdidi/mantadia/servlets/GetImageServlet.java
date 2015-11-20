package net.macdidi.mantadia.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.ImageDao;
import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.util.ImageUtil;

/**
 * 取得圖檔
 * 
 * @author macdidi
 */
@WebServlet(name = "GetImageServlet", 
            urlPatterns = { "/GetImageServlet.view" })
public class GetImageServlet extends HttpServlet {

    @Inject
    private ImageDao imageDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取圖片編號與種類
        int imageId = Integer.parseInt(request.getParameter("imageId"));
        String type = request.getParameter("type");
        // 取得指定編號的圖片物件
        Image image = imageDao.get(imageId);

        if (image != null) {
            // 取得圖片的檔案名稱
            String fileName = image.getFileName();

            if (type == null) {
                // 縮圖檔案名稱
                fileName = "s" + fileName;
            }

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

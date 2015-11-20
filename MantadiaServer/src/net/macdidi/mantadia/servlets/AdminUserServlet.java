package net.macdidi.mantadia.servlets;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import net.macdidi.mantadia.dao.ImageDao;
import net.macdidi.mantadia.dao.UserDao;
import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.domain.User;
import net.macdidi.mantadia.util.ImageUtil;

/**
 * 使用者資料管理（新增，修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "AdminUserServlet", urlPatterns = { "/admin_user.do" })
@MultipartConfig(location=ImageUtil.UPLOAD_FOLDER)
public class AdminUserServlet extends HttpServlet {

    @Inject
    private UserDao userDao;
    @Inject
    private ImageDao imageDao;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取處理種類。"add"：新增；"update"：修改；"delete"：刪除
        String action = request.getParameter("action");
                
        // 新增使用者
        if (action.equals("add")) {
            // 讀取使用者圖片物件
            Part imagePart = request.getPart("image");
            
            // 讀取使用者帳號、密碼、姓名、性別、職務與備註
            String account = request.getParameter("account");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int role = Integer.parseInt(request.getParameter("role"));
            String note = request.getParameter("note");
            
            // 取得圖片名稱
            String fileName = ImageUtil.extractFileName(imagePart);
            int imageId = 0;
            
            // 如果有上傳圖片
            if (fileName != null && fileName.length() > 0) {
                // 取得儲存圖片用的檔案名稱
                String imageFileName = ImageUtil.getUniqueName(fileName);
                // 儲存圖片
                imagePart.write(imageFileName);
                // 原始圖片檔案與縮圖檔案
                File original = new File(ImageUtil.UPLOAD_FOLDER, imageFileName);
                File small = new File(ImageUtil.UPLOAD_FOLDER, "s" + imageFileName);
                // 儲存縮圖
                ImageUtil.saveSmallImage(original, small);
                // 建立圖片物件
                Image image = new Image(0, imageFileName, "");
                // 新增圖片並取得圖片編號
                imageId = imageDao.add(image);
            }
            
            // 建立要新增的使用者物件
            User user = new User(0, account, password, name, gender, role,
                    note, imageId);
            // 新增使用者
            userDao.add(user);
        }
        // 修改使用者
        else if (action.equals("update")) {
            // 讀取使用者圖片物件
            Part imagePart = request.getPart("image");
            
            // 讀取使用者編號、帳號、密碼、姓名、性別、職務與備註
            int id = Integer.parseInt(request.getParameter("id"));
            String account = request.getParameter("account");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            int gender = Integer.parseInt(request.getParameter("gender"));
            int role = Integer.parseInt(request.getParameter("role"));
            String note = request.getParameter("note");
            
            // 取得圖片名稱
            String fileName = ImageUtil.extractFileName(imagePart);
            // 取得圖片編號
            int imageId = userDao.get(id).getImageId();
            
            // 如果有上傳圖片
            if (fileName != null && fileName.length() > 0) {
                // 取得儲存圖片用的檔案名稱
                String imageFileName = ImageUtil.getUniqueName(fileName);
                // 儲存圖片
                imagePart.write(imageFileName);
                // 原始圖片檔案與縮圖檔案
                File original = new File(ImageUtil.UPLOAD_FOLDER, imageFileName);
                File small = new File(ImageUtil.UPLOAD_FOLDER, "s" + imageFileName);
                // 儲存縮圖
                ImageUtil.saveSmallImage(original, small);
                
                if (imageId != 0) {
                    // 修改圖片
                    imageDao.update(new Image(imageId, imageFileName, ""));
                }
                else {
                    // 新增圖片並取得圖片編號
                    imageId = imageDao.add(new Image(0, imageFileName, ""));
                }
            }
            
            // 建立要修改的使用者物件
            User user = new User(id, account, password, name, gender, role,
                    note, imageId);
            // 修改使用者
            userDao.update(user);

        }
        // 刪除使用者
        else if (action.equals("delete")) {
            // 讀取使用者編號
            int id = Integer.parseInt(request.getParameter("id"));

            // 取得要刪除的使用者物件
            User user = userDao.get(id);
            // 刪除使用者
            userDao.delete(user);
            // 建立要刪除的圖片物件
            Image imageDelete = imageDao.get(user.getImageId());

            if (imageDelete != null) {
                // 刪除圖片
                imageDao.delete(imageDelete);
            }
        }

        // 轉向使用者列表元件
        RequestDispatcher view = request.getRequestDispatcher("users.jsp");
        view.forward(request, response);
    }

}

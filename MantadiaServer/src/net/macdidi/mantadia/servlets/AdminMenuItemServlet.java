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
import net.macdidi.mantadia.dao.MenuItemDao;
import net.macdidi.mantadia.domain.Image;
import net.macdidi.mantadia.domain.MenuItem;
import net.macdidi.mantadia.util.ImageUtil;

/**
 * 菜單資料管理（新增，修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "AdminMenuItemServlet", 
            urlPatterns = { "/admin_menu_item.do" })
@MultipartConfig(location=ImageUtil.UPLOAD_FOLDER)
public class AdminMenuItemServlet extends HttpServlet {

    @Inject
    private MenuItemDao menuItemDao;
    @Inject
    private ImageDao imageDao;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {

        // 讀取處理種類。"add"：新增；"update"：修改；"delete"：刪除
        String action = request.getParameter("action");
        
        // 新增菜單資料
        if (action.equals("add")) {
            // 讀取菜單圖片物件
            Part imagePart = request.getPart("image");

            // 讀取菜單種類編號、菜單名稱、菜單價格與菜單備註
            int menuTypeId = Integer.parseInt(request.getParameter("menuTypeId"));
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
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

            // 建立要新增的菜單物件
            MenuItem menuItem = new MenuItem(0, menuTypeId, name, price,
                    note, imageId);
            // 新增菜單
            menuItemDao.add(menuItem);
        }
        // 修改菜單資料
        else if (action.equals("update")) {
            // 讀取菜單圖片物件
            Part imagePart = request.getPart("image");

            // 讀取菜單編號、菜單種類編號、菜單名稱、菜單價格與菜單備註
            int id = Integer.parseInt(request.getParameter("id"));
            int menuTypeId = Integer.parseInt(request.getParameter("menuTypeId"));
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            String note = request.getParameter("note");

            // 取得圖片名稱
            String fileName = ImageUtil.extractFileName(imagePart);
            // 取得圖片編號
            int imageId = menuItemDao.get(id).getImageId();

            // 如果有上傳圖片
            if (fileName != null && fileName.length() > 0) {
                // 取得儲存圖片用的檔案名稱
                String imageFileName = ImageUtil.getUniqueName(fileName);
                // 儲存圖片
                imagePart.write(imageFileName);
                // 原始圖片檔案
                File original = new File(ImageUtil.UPLOAD_FOLDER, imageFileName);
                // 縮圖檔案
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
            
            // 建立要修改的菜單物件
            MenuItem menuItem = new MenuItem(id, menuTypeId, name, price,
                    note, imageId);
            // 修改菜單
            menuItemDao.update(menuItem);
        }
        // 刪除菜單資料
        else if (action.equals("delete")) {
            // 讀取菜單編號
            int id = Integer.parseInt(request.getParameter("id"));

            // 取得要刪除的菜單物件
            MenuItem menuItem = menuItemDao.get(id);
            // 刪除菜單
            menuItemDao.delete(menuItem);
            // 建立要刪除的圖片物件
            Image imageDelete = imageDao.get(menuItem.getImageId());

            if (imageDelete != null) {
                // 刪除圖片
                imageDao.delete(imageDelete);
            }
        }

        // 轉向菜單列表元件
        RequestDispatcher view = 
                request.getRequestDispatcher("menu_items.jsp");
        view.forward(request, response);
    }

}

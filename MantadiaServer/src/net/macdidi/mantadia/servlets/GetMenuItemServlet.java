package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.MenuItemDao;
import net.macdidi.mantadia.domain.MenuItem;

/**
 * 取得菜單資料（修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "GetMenuItemServlet", 
            urlPatterns = { "/get_menu_item.do" })
public class GetMenuItemServlet extends HttpServlet {

    @Inject
    private MenuItemDao menuItemDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {

        // 讀取處理種類。"update"：修改；"delete"：刪除
        String action = request.getParameter("action");
        // 讀取菜單編號
        int id = Integer.parseInt(request.getParameter("id"));
        // 取得指定編號的菜單物件
        MenuItem menuItem = menuItemDao.get(id);
        // 設定菜單物件為Request範圍的Attribute
        request.setAttribute("menuItem", menuItem);
        
        String forward = "";
        
        switch (action) {
        // 修改菜單資料
        case "update":
            forward = "update_menu_item.jsp";
            break;
        // 刪除菜單資料
        case "delete":
            forward = "delete_menu_item.jsp";
            break;
        }

        // 轉向修改或刪除菜單元件
        RequestDispatcher view = 
                request.getRequestDispatcher(forward);
        view.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }    

}

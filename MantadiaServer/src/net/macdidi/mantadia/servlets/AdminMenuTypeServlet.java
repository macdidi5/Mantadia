package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.MenuTypeDao;
import net.macdidi.mantadia.domain.MenuType;

/**
 * 菜單種類資料管理（新增，修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "AdminMenuTypeServlet", 
            urlPatterns = { "/admin_menu_type.do" })
public class AdminMenuTypeServlet extends HttpServlet {

    @Inject
    private MenuTypeDao menuTypeDao;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取處理種類。"add"：新增；"update"：修改；"delete"：刪除
        String action = request.getParameter("action");

        // 新增菜單種類
        if (action.equals("add")) {
            // 讀取菜單種類名稱與菜單種類備註
            String name = request.getParameter("name");
            String note = request.getParameter("note");

            // 建立要新增的菜單種類物件
            MenuType menuType = new MenuType(0, name, note);
            // 新增菜單種類
            menuTypeDao.add(menuType);
        }
        // 修改菜單種類
        else if (action.equals("update")) {
            // 讀取菜單種類編號、菜單種類名稱與菜單種類備註
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String note = request.getParameter("note");

            // 建立要修改的菜單種類物件
            MenuType menuType = new MenuType(id, name, note);
            // 修改菜單種類
            menuTypeDao.update(menuType);
        }
        // 刪除菜單種類
        else if (action.equals("delete")) {
            // 讀取菜單種類編號
            int id = Integer.parseInt(request.getParameter("id"));
            // 刪除菜單種類
            menuTypeDao.delete(menuTypeDao.get(id));
        }

        // 轉向菜單種類列表元件
        RequestDispatcher view = request
                .getRequestDispatcher("menu_types.jsp");
        view.forward(request, response);
    }

}

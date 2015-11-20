package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.TablesDao;
import net.macdidi.mantadia.domain.Tables;

/**
 * 餐桌資料管理（新增，修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "AdminTablesServlet", 
            urlPatterns = { "/admin_tables.do" })
public class AdminTablesServlet extends HttpServlet {

    @Inject
    private TablesDao tablesDao;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 讀取處理種類。"add"：新增；"update"：修改；"delete"：刪除
        String action = request.getParameter("action");

        // 新增餐桌
        if (action.equals("add")) {
            // 讀取餐桌備註
            String note = request.getParameter("note");

            // 建立要新增的餐桌物件
            Tables tables = new Tables(0, 0, note);
            // 新增餐桌
            tablesDao.add(tables);
        }
        // 修改餐桌
        else if (action.equals("update")) {
            // 讀取餐桌編號與餐桌備註
            int id = Integer.parseInt(request.getParameter("id"));
            String note = request.getParameter("note");

            // 建立要修改的餐桌物件
            Tables tables = new Tables(id, 0, note);
            // 修改餐桌
            tablesDao.update(tables);
        }
        // 刪除餐桌
        else if (action.equals("delete")) {
            // 讀取餐桌編號
            int id = Integer.parseInt(request.getParameter("id"));
            // 刪除餐桌
            tablesDao.delete(tablesDao.get(id));
        }

        // 轉向餐桌列表元件
        RequestDispatcher view = request.getRequestDispatcher("tables.jsp");
        view.forward(request, response);
    }

}

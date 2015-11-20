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
 * 取得餐桌資料（修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "GetTableServlet", 
            urlPatterns = { "/get_table.do" })
public class GetTableServlet extends HttpServlet {

    @Inject
    private TablesDao tablesDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {

        // 讀取處理種類。"update"：修改；"delete"：刪除
        String action = request.getParameter("action");
        // 讀取餐桌編號
        int id = Integer.parseInt(request.getParameter("id"));
        // 取得指定編號的餐桌物件
        Tables table = tablesDao.get(id);
        // 設定餐桌物件為Request範圍的Attribute
        request.setAttribute("table", table);
        
        String forward = "";
        
        switch (action) {
        // 修改餐桌資料
        case "update":
            forward = "update_table.jsp";
            break;
        // 刪除餐桌資料
        case "delete":
            forward = "delete_table.jsp";
            break;
        }

        // 轉向修改或刪除餐桌元件
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

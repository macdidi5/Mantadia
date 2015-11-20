package net.macdidi.mantadia.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.UserDao;
import net.macdidi.mantadia.domain.User;

/**
 * 取得使用者資料（修改，刪除）
 * 
 * @author macdidi
 */
@WebServlet(name = "GetUserServlet", 
            urlPatterns = { "/get_user.do" })
public class GetUserServlet extends HttpServlet {

    @Inject
    private UserDao userDao;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {

        // 讀取處理種類。"update"：修改；"delete"：刪除
        String action = request.getParameter("action");
        // 讀取使用者編號
        int id = Integer.parseInt(request.getParameter("id"));
        // 取得指定編號的使用者物件
        User user = userDao.get(id);
        // 設定使用者物件為Request範圍的Attribute
        request.setAttribute("user", user);
        
        String forward = "";
        
        switch (action) {
        // 修改使用者資料
        case "update":
            forward = "update_user.jsp";
            break;
        // 刪除使用者資料
        case "delete":
            forward = "delete_user.jsp";
            break;
        }

        // 轉向修改或刪除使用者元件
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

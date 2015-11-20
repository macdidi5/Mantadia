package net.macdidi.mantadia.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.macdidi.mantadia.dao.UserDao;
import net.macdidi.mantadia.domain.User;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * 登入
 * 
 * @author macdidi
 */
@WebServlet(name = "LoginServlet", 
            urlPatterns = { "/mobile/LoginServlet.do" })
public class LoginServlet extends HttpServlet {

    @Inject
    private UserDao userDao;

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException,
            IOException {
        // 設定回應型態為XML
        response.setContentType("application/xml");
        // 取得回應物件
        PrintWriter out = response.getWriter();

        // 讀取帳號與密碼
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        // 使用帳號與密碼取得使用者物件
        User user = userDao.get(account, password);
        // 建立物件轉為XML的物件
        Serializer serializer = new Persister();

        try {
            // 如果帳號或密碼錯誤，建立一個空白的使用者物件
            if (user == null) {
                user = new User();
            }

            // 輸出使用者XML資訊
            serializer.write(user, out);
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


package frontend;

import pageGenerator.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Frontend extends HttpServlet {

    private Map<String,String> auth=new HashMap<String,String>() {{
        put("max","12345");
        put("serj", "54321");
    }}; //данные для авторизации
    private AtomicLong userIdGenerator = new AtomicLong();

    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getPathInfo().equals("/authform")) {
            pageVariables.put("error", "");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            return;
        }

        if (request.getPathInfo().equals("/userid")) {
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.sendRedirect("/index.html"); //перенаправляем на индекс если не было логина
                return;
            }
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());
            pageVariables.put("userId", userId);
            response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
            return;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if(auth.containsKey(login)&& password.equals(auth.get(login)))
        {
            HttpSession session = request.getSession();
            if( !session.isNew() ) //если сессия была в браузере, заканчиваем и начинаем новую
            {
                session.invalidate();
                session = request.getSession();
            }
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                userId = userIdGenerator.getAndIncrement();
                session.setAttribute("userId", userId);
            }
            response.sendRedirect("userid");
            return;
        }
        else
        {
            if(login != null && password != null) // выдаём ошибку если обе формы не пусты
                pageVariables.put("error" , "Неправильные login/password");
            else
                pageVariables.put("error" , "");
            response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
            return;
        }
    }
}

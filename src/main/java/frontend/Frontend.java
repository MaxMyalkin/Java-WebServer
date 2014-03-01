
package frontend;

import database.AccountService;
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

@CreatedBy( name = "max" , date = "15.02.14" )
public class Frontend extends HttpServlet {

    static final DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");
    private AtomicLong userIdGenerator = new AtomicLong();
    private AccountService accountService;

    public Frontend() {
        this.accountService = new AccountService();
    }

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        switch(request.getPathInfo()) {
            case "/authform":
                if(request.getParameter("info") != null)
                    pageVariables.put("info", "Wrong login/password" );
                else
                    pageVariables.put("info", "");
                response.getWriter().println(PageGenerator.getPage("authform.tml", pageVariables));
                break;

            case "/registerform":
                if(request.getParameter("info") != null) {
                    switch(request.getParameter("info")) {
                        case "error":
                            pageVariables.put("info", "Input all fields" );
                            break;
                        case "exist":
                            pageVariables.put("info", "User already exists" );
                            break;
                        case "ok":
                            pageVariables.put("info", "User was added" );
                            break;
                        default:
                            pageVariables.put("info", "");
                    }
                }
                else
                    pageVariables.put("info", "");
                response.getWriter().println(PageGenerator.getPage("registerform.tml", pageVariables));
                break;

            case "/userid":

                HttpSession session = request.getSession();
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null) {
                    response.sendRedirect("/index.html"); //перенаправляем на индекс если не было логина
                }
                else {
                    pageVariables.put("refreshPeriod", "1000");
                    pageVariables.put("serverTime", getTime());
                    pageVariables.put("userId", userId);
                    response.getWriter().println(PageGenerator.getPage("userId.tml", pageVariables));
                }
                break;

            default:
                response.sendRedirect("/index.html"); // для любых других адресов идём на главную

        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        final String LOGIN = request.getParameter("login");
        final String PASSWORD = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        switch (request.getPathInfo()) {
            case "/authform" :

                if(accountService.checkUser(LOGIN,PASSWORD))
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
                }
                else
                {
                    response.sendRedirect("/authform?info=error");
                }
                break;

            case "/registerform" :

                if(LOGIN.equals("") || PASSWORD.equals("")) {
                    response.sendRedirect("/registerform?info=error");
                }
                else {
                    if(accountService.checkUser(LOGIN,PASSWORD)) {
                        response.sendRedirect("/registerform?info=exist");
                    }
                    else {
                        accountService.addUser( LOGIN , PASSWORD );
                        response.sendRedirect("registerform?info=ok");
                    }
                }
                break;

            default:
        }
    }
}

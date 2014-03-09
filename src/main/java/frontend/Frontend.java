
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

    static final private DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");
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
        HttpSession session= request.getSession();
        String info;
        try
        {
            info = session.getAttribute("info").toString();
        }
        catch (NullPointerException e)
        {
            info = "";
        }
        switch(request.getPathInfo()) {
            case Constants.Url.AUTHFORM:
                if(info.equals("wrong"))
                    pageVariables.put("info", Constants.Message.AUTH_FAILED );
                response.getWriter().println(PageGenerator.getPage(Constants.Page.AUTHORIZATION, pageVariables));
                break;

            case Constants.Url.REGISTERFORM:
                switch(info) {
                    case "error":
                        pageVariables.put("info", Constants.Message.EMPTY_FIELDS );
                        break;
                    case "exist":
                        pageVariables.put("info", Constants.Message.USER_EXISTS );
                        break;
                    case "ok":
                        pageVariables.put("info", Constants.Message.SUCCESSFUL_REGISTRATION );
                        break;
                    default:
                        break;
                }
                response.getWriter().println(PageGenerator.getPage(Constants.Page.REGISTRATION, pageVariables));
                break;

            case Constants.Url.SESSION:
                Long userId;
                try
                {
                    userId = (Long)session.getAttribute("userId");
                }
                catch (NullPointerException e)
                {
                    userId = null;
                }
                if(userId == null)
                {
                    response.sendRedirect(Constants.Url.INDEX);
                }
                pageVariables.put("refreshPeriod", Constants.REFRESH_TIME);
                pageVariables.put("serverTime", getTime());
                pageVariables.put("userId", userId);
                response.getWriter().println(PageGenerator.getPage(Constants.Page.SESSION, pageVariables));
                break;

            case Constants.Url.INDEX:
                response.getWriter().println(PageGenerator.getPage(Constants.Page.INDEX , pageVariables));
                break;
            default:
                response.sendRedirect(Constants.Url.INDEX);
        }
        try {
            session.removeAttribute("info");
        }
        catch (NullPointerException e){}
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        switch (request.getPathInfo()) {
            case Constants.Url.AUTHFORM :
                if(accountService.checkUser(login,password))
                {
                    try
                    {
                        if( session.getAttribute("userId") != null )
                        {
                            session.removeAttribute("userId");
                        }
                        Long userId = userIdGenerator.getAndIncrement();
                        session.setAttribute("userId", userId);
                        session.removeAttribute("info");
                        response.sendRedirect(Constants.Url.SESSION);
                    }
                    catch (NullPointerException e)
                    {
                        response.sendRedirect(Constants.Url.INDEX);
                    }
                }
                else
                {
                    session.setAttribute("info" , "wrong");
                    response.sendRedirect(Constants.Url.AUTHFORM);
                }
                break;

            case Constants.Url.REGISTERFORM :
                if(login.equals("") || password.equals("")) {
                    session.setAttribute("info", "error");
                    response.sendRedirect(Constants.Url.REGISTERFORM );
                }
                else {
                    if(!accountService.addUser(login , password)) {
                        session.setAttribute("info", "exist");
                        response.sendRedirect(Constants.Url.REGISTERFORM);
                    }
                    else {
                        session.setAttribute("info" , "ok");
                        response.sendRedirect(Constants.Url.REGISTERFORM);
                    }
                }
                break;

        }
    }
}

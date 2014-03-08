
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
        String info = null;
        if(session != null)
        {
            try{
                info = session.getAttribute("info").toString();
            }
            catch (NullPointerException e){
                info = "";
            }
        }
        switch(request.getPathInfo()) {
            case Url.AUTHFORM:
                if(info.equals("wrong") )
                    pageVariables.put("info", "Wrong login/password" );
                response.getWriter().println(PageGenerator.getPage(Page.AUTHORIZATION, pageVariables));
                break;

            case Url.REGISTERFORM:

                if(info != null) {
                    switch(info) {
                        case "error":
                            pageVariables.put("info", "Input all fields" );
                            break;
                        case "exist":
                            pageVariables.put("info", "User already exists" );
                            break;
                        case "ok":
                            pageVariables.put("info", "User was added" );
                            break;
                    }
                }
                response.getWriter().println(PageGenerator.getPage(Page.REGISTRATION, pageVariables));
                break;

            case Url.SESSION:
                Long userId = (Long) session.getAttribute("userId");
                if (userId == null) {
                    response.sendRedirect(Url.INDEX);
                }
                else {
                    pageVariables.put("refreshPeriod", "5000");
                    pageVariables.put("serverTime", getTime());
                    pageVariables.put("userId", userId);
                    response.getWriter().println(PageGenerator.getPage(Page.SESSION, pageVariables));
                }
                break;

            case Url.INDEX:
                session.removeAttribute("info");
                session.removeAttribute("userId");
                response.getWriter().println(PageGenerator.getPage(Page.INDEX , pageVariables));

        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        switch (request.getPathInfo()) {
            case Url.AUTHFORM :
                if(accountService.checkUser(login,password))
                {
                    if( !session.isNew() )
                    {
                        session.invalidate();
                        session = request.getSession();
                    }
                    Long userId = (Long) session.getAttribute("userId");
                    if (userId == null) {
                        userId = userIdGenerator.getAndIncrement();
                        session.setAttribute("userId", userId);
                    }
                    session.removeAttribute("info");
                    response.sendRedirect(Url.SESSION);
                }
                else
                {
                    session.setAttribute("info" , "wrong");
                    response.sendRedirect(Url.AUTHFORM);
                }
                break;

            case Url.REGISTERFORM :
                if(login.equals("") || password.equals("")) {
                    session.setAttribute("info", "error");
                    response.sendRedirect(Url.REGISTERFORM );
                }
                else {
                    if(!accountService.addUser(login , password)) {
                        session.setAttribute("info", "exist");
                        response.sendRedirect(Url.REGISTERFORM);
                    }
                    else {
                        session.setAttribute("info" , "ok");
                        response.sendRedirect(Url.REGISTERFORM);
                    }
                }
                break;

        }
    }
}

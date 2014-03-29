package frontend;

import database.UsersDataSet;
import message.MsgGetUser;
import message.MsgRegistrate;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import thread.Helper;

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

@CreatedBy( name = "max" , date = "15.02.14" )
public class Frontend extends HttpServlet implements Runnable, Abonent {

    static final private DateFormat FORMATTER = new SimpleDateFormat("HH.mm.ss");
    private MessageSystem messageSystem;
    private Address address;
    private Map<String, UserSession> sessions = new HashMap<>();

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        this.messageSystem.addAbonent(this);
    }

    public void setUser(String sessionID , UsersDataSet user){
        UserSession session = sessions.get(sessionID);
        if(session != null) {
            session.setUserID(user.getId());
            session.setUserName(user.getLogin());
        }
    }

    public void setMessage(String sessionID , String message){
        UserSession session = sessions.get(sessionID);
        if(session != null)
            session.setMessage(message);
    }

    private void logout(HttpServletRequest request) {
        sessions.remove(request.getSession().getId());
    }

    private void setInfo(HttpSession session , HttpServletResponse response) throws IOException{
        UserSession userSession=sessions.get(session.getId());
        if( userSession!= null) {
            response.getWriter().println(userSession.getMessage());
            userSession.setMessage("");
        }
    }

    private void getAuthForm(HttpServletResponse response) throws ServletException, IOException
    {
        response.getWriter().println(PageGenerator.getPage(Constants.Page.AUTHORIZATION, new HashMap<String, Object>()));
    }

    private void getRegisterForm(HttpServletResponse response) throws ServletException, IOException
    {
        response.getWriter().println(PageGenerator.getPage(Constants.Page.REGISTRATION, new HashMap<String, Object>()));
    }

    private void getSessionPage( HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        Map<String,Object> pageVariables = new HashMap<>();
        UserSession userSession = sessions.get(session.getId());
        if(userSession == null) {
            pageVariables.put("info", "Авторизуйтесь на /authform");
        }
        else {
            pageVariables.put("info", userSession.getMessage());
            if(userSession.getMessage().equals(Constants.Message.AUTH_SUCCESSFUL)) {
                pageVariables.put("userId", userSession.getUserID());
                pageVariables.put("userName", userSession.getName());
            }
        }
        pageVariables.put("refreshPeriod", Constants.REFRESH_TIME);
        pageVariables.put("serverTime", getTime());
        response.getWriter().println(PageGenerator.getPage(Constants.Page.SESSION, pageVariables));
    }

    private void getIndexPage(HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();
        response.getWriter().println(PageGenerator.getPage(Constants.Page.INDEX , pageVariables));
    }

    private void postAuthForm( HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        final String sessionID = session.getId();

        UserSession userSession = new UserSession(login,sessionID, messageSystem.getAddressService() , "");
        sessions.put(sessionID,userSession);
        if(!login.equals("") && !password.equals("")) {
            messageSystem.sendMessage(new MsgGetUser(address,messageSystem.getAddressService().getAccountService(),
                    login, password, sessionID));
            userSession.setMessage(Constants.Message.WAITING);
        }
        else
            userSession.setMessage(Constants.Message.EMPTY_FIELDS);
        response.sendRedirect(Constants.Url.SESSION);
    }

    private void postRegisterForm( HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        final String sessionID = session.getId();

        UserSession userSession = new UserSession(login,sessionID, messageSystem.getAddressService() , "");
        sessions.put(sessionID,userSession);
        if(!login.equals("") && !password.equals(""))
            messageSystem.sendMessage(new MsgRegistrate(address, messageSystem.getAddressService().getAccountService(),
                    login, password, sessionID));
        else
            userSession.setMessage(Constants.Message.EMPTY_FIELDS);

        response.sendRedirect(Constants.Url.REGISTERFORM);
    }

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session= request.getSession();
        switch(request.getPathInfo()) {
            case Constants.Url.AUTHFORM:
                getAuthForm(response);
                break;

            case Constants.Url.REGISTERFORM:
                getRegisterForm(response);
                break;

            case Constants.Url.SESSION:
                getSessionPage(request, response);
                break;

            case Constants.Url.LOGOUT:
                logout(request);
                response.sendRedirect(Constants.Url.INDEX);
                break;

            case Constants.Url.INDEX:
                getIndexPage(response);
                break;

            case Constants.Url.AJAX_CHECKING:
                setInfo(session,response);
                break;

            default:
                response.sendRedirect(Constants.Url.INDEX);
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        switch (request.getPathInfo()) {
            case Constants.Url.AUTHFORM :
                postAuthForm(request , response);
                break;

            case Constants.Url.REGISTERFORM :
                postRegisterForm(request , response);
                break;

        }
    }


    public void run(){
        while (true){
            messageSystem.execForAbonent(this);
            Helper.sleep(300);
        }
    }

    public Address getAddress(){
        return address;
    }

}

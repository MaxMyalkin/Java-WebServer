package frontend;

import database.UsersDataSet;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Frontend extends HttpServlet implements Runnable, Abonent {

    private MessageSystem messageSystem;
    private Address address;
    private Map<String, UserSession> sessions = new HashMap<>();
    private FactoryHelper factoryHelper;

    public Frontend(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        this.address = new Address();
        this.messageSystem.addAbonent(this);
        this.factoryHelper = new FactoryHelper();
    }

    public Frontend(MessageSystem messageSystem, FactoryHelper factoryHelper) {
        this(messageSystem);
        this.factoryHelper = factoryHelper;
    }

    public UserSession addUserSession(String login , String sessionID){
        UserSession userSession = new UserSession(login,sessionID, messageSystem.getAddressService() , "");
        sessions.put(sessionID,userSession);
        return userSession;
    }

    private void removeUserSession(String sessionID) {
        sessions.remove(sessionID);
    }

    public UserSession getUserSession(String sessionID) {
        return sessions.get(sessionID);
    }


    public void setUser(String sessionID , UsersDataSet user){
        UserSession session = getUserSession(sessionID);
        if(session != null) {
            session.setUserID(user.getId());
            session.setUserName(user.getLogin());
        }
    }

    public void setMessage(String sessionID , String message){
        UserSession session = getUserSession(sessionID);
        if(session != null)
            session.setMessage(message);
    }



    private void setInfo(HttpSession session , HttpServletResponse response) throws IOException{
        UserSession userSession=getUserSession(session.getId());
        if( userSession!= null) {
            response.getWriter().print(userSession.getMessage());
            userSession.setMessage("");
        }
    }

    private void getPage (String page , HttpServletResponse response, Map<String, Object> pageVariables) throws IOException{
        response.getWriter().println(PageGenerator.getPage(page, pageVariables));
    }

    private void getSessionPage( HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        Map<String,Object> pageVariables = new HashMap<>();
        UserSession userSession = getUserSession(session.getId());
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
        pageVariables.put("serverTime", Constants.getTime());
        response.getWriter().println(PageGenerator.getPage(Constants.Page.SESSION, pageVariables));
    }

    private void postAuthForm( HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        final String sessionID = session.getId();

        UserSession userSession = addUserSession(login, sessionID);
        if(!login.equals("") && !password.equals("")) {
            messageSystem.sendMessage(factoryHelper.makeMsgGetUser(address,messageSystem.getAddressService().getAccountService(),
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

        UserSession userSession = addUserSession(login, sessionID);
        if(!login.equals("") && !password.equals(""))
            messageSystem.sendMessage(factoryHelper.makeMsgRegistrate(address, messageSystem.getAddressService().getAccountService(),
                    login, password, sessionID));
        else
            userSession.setMessage(Constants.Message.EMPTY_FIELDS);

        response.sendRedirect(Constants.Url.REGISTERFORM);
    }



    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session= request.getSession();
        switch(request.getPathInfo()) {
            case Constants.Url.AUTHFORM:
                getPage(Constants.Page.AUTHORIZATION, response, null);
                break;

            case Constants.Url.REGISTERFORM:
                Map<String,Object> pageVariables = new HashMap<>();
                UserSession userSession = getUserSession(session.getId());
                if(userSession != null)
                    pageVariables.put("info", userSession.getMessage());
                getPage(Constants.Page.REGISTRATION, response, pageVariables);
                break;

            case Constants.Url.SESSION:
                getSessionPage(request, response);
                break;

            case Constants.Url.LOGOUT:
                removeUserSession(request.getSession().getId());
                response.sendRedirect(Constants.Url.INDEX);
                break;

            case Constants.Url.INDEX:
                getPage(Constants.Page.INDEX, response, null);
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
            Constants.sleep(300);
        }
    }

    public Address getAddress(){
        return address;
    }

}

package filters;

import dao.UserDao;
import model.RolesInspector;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class AuthFilter extends HttpFilter {
    private final static Logger logger = LogManager.getLogger(AuthFilter.class);
    private final static String LOGIN_PAGE = "index.jsp";
    private UserDao userDao;
    private RolesInspector rolesInspector;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
        rolesInspector=(RolesInspector) servletContext.getAttribute("rolesInspector");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        setUserDestinationUrl(request,session);
        checkAndSetLanguage(request);
//        if(isAuthorisationRequest(request)||isLogoutRequest(request)){
//            chain.doFilter(request, response);
//            return;
//        }

        if (userIsLoggedIn(session) && userRequestIsValid(session, request)) {
            logger.debug("User" + session.getAttribute("userName") + " accepted access to" + request.getRequestURI());
            chain.doFilter(request, response);
        }
        else {
            logger.debug("User not logged in");
            RequestDispatcher rd = request.getRequestDispatcher(LOGIN_PAGE);
            rd.forward(request, response);
        }
    }

//    private boolean isAuthorisationRequest(HttpServletRequest request){
//        return request.getRequestURI().endsWith("/logoit");
//    }
//
//    private boolean isLogoutRequest(HttpServletRequest request){
//        return request.getRequestURI().endsWith("/logout");
//    }




    private void setUserDestinationUrl(HttpServletRequest request, HttpSession session){
        String requestQuery=request.getQueryString();
        if(requestQuery!=null){
            request.setAttribute("userDestinationUrl",request.getRequestURI()+"?"+requestQuery);
        }
        else
            request.setAttribute("userDestinationUrl",request.getRequestURI());
    }

    private boolean userIsLoggedIn(HttpSession session) {
        return session.getAttribute("user")!=null;
    }

    private void checkAndSetLanguage(HttpServletRequest req){
        String requestLocale=req.getParameter("language");
        if(requestLocale!=null){
            req.getSession().setAttribute("language",requestLocale);
        }
    }

    private boolean userRequestIsValid(HttpSession session, HttpServletRequest request){
        Optional<User> sessionUser=Optional.ofNullable((User) session.getAttribute("user"));


        if(sessionUser.isPresent()){
            boolean accessGranted= rolesInspector.isUserRequestValid(sessionUser.get(),request.getRequestURI());
            logger.debug("Access for user "+sessionUser.get().getFullName()+" to "+request.getRequestURI()+":"+accessGranted);
            return accessGranted;
        }
        logger.debug("Access to "+request.getRequestURI()+": Failed: user not logged in");
        return false;
    }

}

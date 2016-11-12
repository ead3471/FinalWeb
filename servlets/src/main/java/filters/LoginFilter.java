package filters;


import dao.UserDao;
import dao.exceptions.DaoException;
import model.RolesInspector;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public class LoginFilter extends HttpFilter{
    private final static Logger logger = LogManager.getLogger(AuthFilter.class);
    private final static String LOGIN_PAGE = "/index.jsp";
    private UserDao userDao;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        userDao = (UserDao) servletContext.getAttribute("userDao");
    }



    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestUser=request.getParameter("user");
        String authString=request.getParameter("authString");
        HttpSession session=request.getSession();
        String userDest=request.getParameter("userDestinationUrl");
        Optional userDestination=Optional.ofNullable(request.getParameter("userDestinationUrl"));
        logger.debug("in login filter");
        try {
            Optional<User> loadedUser = userDao.getUserByFilter(userDao.filter().withLogin(requestUser).withPass(authString));
            if(loadedUser.isPresent())
            {
                logger.debug("User logged in!"+loadedUser.get().getLogin());
                session.setAttribute("user",loadedUser.get());
                session.setAttribute("userName",loadedUser.get().getFullName());
               String destString="/userpages/"+userDestination.filter(value->value.toString().length()>1).orElse(loadedUser.get().getRole()+".jsp");
                request.getRequestDispatcher(destString).forward(request,response);
               // chain.doFilter(request,response);
            }
            else{
                logger.debug("User NOT FOUND!"+requestUser);
                request.setAttribute("login_msg","Cant find user "+requestUser);
                request.getRequestDispatcher(LOGIN_PAGE).forward(request,response);
            }

        } catch (DaoException e) {
            logger.warn("Error at userDao access by user login and pass:"+requestUser+":"+authString,e);
            request.setAttribute("login_msg","Sever error" +e);
            request.getRequestDispatcher(LOGIN_PAGE).forward(request,response);
        }
    }
}

package controllers;


import dao.UserDao;
import dao.exceptions.DaoException;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class AuthController extends HttpServlet {
    private final Logger logger= LogManager.getLogger(AuthController.class);
    private final static String LOGIN_PAGE = "/index.jsp";
    private final static String DEFAULT_AFTER_LOGIN_PAGE="/";
    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = (UserDao) getServletContext().getAttribute("userDao");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestUser=req.getParameter("user");
        String authString=req.getParameter("authString");
        HttpSession session=req.getSession();
        Optional userDestination=Optional.ofNullable(req.getParameter("userDestinationUrl"));

        try {
            Optional<User> loadedUser = userDao.getUserByFilter(userDao.filter().withLogin(requestUser).withPass(authString));

            if(loadedUser.isPresent())
            {
                logger.debug("User logged in!"+loadedUser.get().getLogin());
                session.setAttribute("user",loadedUser.get());
                session.setAttribute("userName",loadedUser.get().getFullName());
                String destString="userpages/"+userDestination.filter(value->value.toString().length()>1).orElse(loadedUser.get().getRole()+".jsp");
                req.getRequestDispatcher(destString).forward(req,resp);
            }
            else{
                logger.debug("User NOT FOUND!"+requestUser);
                req.setAttribute("login_msg","Cant find user "+requestUser);
                req.getRequestDispatcher(LOGIN_PAGE).forward(req,resp);
            }

        } catch (DaoException e) {
            logger.warn("Error at userDao access by user login and pass:"+requestUser+":"+authString,e);
            req.setAttribute("login_msg","Sever error" +e);
            req.getRequestDispatcher(LOGIN_PAGE).forward(req,resp);
        }


    }


}

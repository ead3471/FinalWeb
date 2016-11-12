package controllers;

import filters.AuthFilter;
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


public class LogOutController extends HttpServlet{
    private static final String DEFAULT_AFTER_LOGOUT_PAGE="index.jsp";
    private final static Logger logger = LogManager.getLogger(AuthFilter.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session=req.getSession();


        if(session!=null){
            logger.debug("Logout user "+((User)(session.getAttribute("user"))).getLogin());
            session.removeAttribute("user");
            session.removeAttribute("userName");
            session.invalidate();
        }
        req.getRequestDispatcher(DEFAULT_AFTER_LOGOUT_PAGE).forward(req,resp);
    }
}

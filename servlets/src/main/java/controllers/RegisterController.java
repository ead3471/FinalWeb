package controllers;

import dao.UserDao;
import dao.exceptions.DaoException;
import filters.AuthFilter;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/register/")

public class RegisterController extends HttpServlet {
    private final static Logger logger = LogManager.getLogger(AuthFilter.class);
    private final static String AFTER_REGISTER_PAGE ="../index.jsp";
    private final static String ERROR_PAGE="../index.jsp";
    private final static String DEFAULT_PHOTO_URL="/avatars/default.jpg";

    private UserDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = (UserDao) getServletContext().getAttribute("userDao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userLogin=req.getParameter("login");
        String userName=req.getParameter("name");
        String password=req.getParameter("pass");
        String role=req.getParameter("userType");


        if(userLogin!=null & userName!=null & password!=null&role!=null){
            User newUser=new User(userName,userLogin,password,DEFAULT_PHOTO_URL,role);
            try {
                userDao.insertUser(newUser);
                logger.info("New user created:"+newUser);
                req.getRequestDispatcher(AFTER_REGISTER_PAGE).forward(req,resp);
                return;
            }
            catch(DaoException ex){
                logger.warn("Error at create new user:"+userLogin+"/"+userName+"/"+password+"/"+role,ex);
                req.setAttribute("errorMsg","Server Error. Try Again please");
            }

        }
        else{
            req.setAttribute("errorMsg","Wrong register parameters. Try Again please");

        }

        req.getRequestDispatcher(ERROR_PAGE).forward(req,resp);


    }


}

package controllers;

import common.HttpRequestHandler;
import dao.SpecialisationDao;
import dao.UserDao.UserFilter;
import dao.UserDao;
import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/profile/")
public class ShowUser extends HttpServlet implements HttpRequestHandler{

    private final Logger logger= LogManager.getLogger(ShowUser.class);
    private UserDao userDao;
    private SpecialisationDao specialisationDao;

    private final static String USER_PROFILE_PAGE="/userpages/workerpages/profile.jsp";

    private final static String ERROR_PAGE="/error.jsp";

    @Override
    public void init() throws ServletException {
        userDao=(UserDao) getServletContext().getAttribute("userDao");
        specialisationDao=(SpecialisationDao)getServletContext().getAttribute("specDao");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> sessionUser = Optional.ofNullable((User) req.getSession().getAttribute("user"));
        Optional<Integer> userIdFromRequest = getIntParameterAsOptional(req, "id");
        try{
            Optional<User> viewedUser ;
            if(userIdFromRequest.isPresent()){
                viewedUser=userDao.getUserByFilter((UserFilter) userDao.filter().withID(userIdFromRequest.get()));
                if(!viewedUser.isPresent()){
                    logger.warn("User with ID "+userIdFromRequest+" not found");
                    req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
                }
            }
            else if(sessionUser.isPresent()){
                viewedUser=sessionUser;
            }
            else{
                req.getRequestDispatcher(ERROR_PAGE).forward(req,resp);
                return;
            }

            req.setAttribute("viewedUser",viewedUser.get());
            List<Specialisation> userSpecs = specialisationDao.getByUserId(viewedUser.get().getId());
            req.setAttribute("userSpecs", userSpecs);
            req.getRequestDispatcher(USER_PROFILE_PAGE).forward(req, resp);
        }
        catch(DaoException ex){
            logger.warn("Error at load user info", ex);
            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
        }
    }





}

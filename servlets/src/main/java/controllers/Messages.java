package controllers;

import dao.MessageDao;
import dao.UserDao;
import dao.exceptions.DaoException;
import model.Message;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/messages/*")

public class Messages extends HttpServlet {
    private final Logger logger = LogManager.getLogger(Messages.class);
    MessageDao messageDao;
    private final static String NOT_REGISTERED_USER_PAGE = "index.jsp";
    private final static String USER_MESSAGES_PAGE = "/userpages/workerpages/messages.jsp";

    private final static String USER_MESSAGES_WITH_ANOTHER_USER_PAGE = "/userpages/workerpages/dialog.jsp";
    private final static String ERROR_PAGE = "/userpages/workpages/error.jsp";

    @Override
    public void init() throws ServletException {
        messageDao = (MessageDao) getServletContext().getAttribute("messagesDao");
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> action = Optional.ofNullable(req.getParameter("action")).filter(value -> value.matches("^(listAll|listWith|listBetween)$"));

        User mainUser = (User) req.getSession().getAttribute("user");

        if (mainUser != null && action.isPresent()) {
            if (action.get().equals("listAll")) {
                try {
                    List<Message> userMessages = messageDao.getLastMessages(mainUser.getId());
                    req.setAttribute("allMessages", userMessages);
                    req.getRequestDispatcher(USER_MESSAGES_PAGE).forward(req, resp);
                } catch (DaoException e) {
                    logger.warn("Error list all messages for user " + mainUser.getLogin(), e);
                    req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
                }
                return;
            }
            if (action.get().equals("listWith")) {
                    Optional<Integer> partnerOptional=getIntParameterAsOptional(req,"id");
                    if(partnerOptional.isPresent()){
                        try {
                            List<Message> betweenUserMessages = messageDao.getMessagesByFilter(messageDao.filter().allWithUserId(partnerOptional.get()));
                            req.setAttribute("dialog",betweenUserMessages);
                            req.getRequestDispatcher(USER_MESSAGES_WITH_ANOTHER_USER_PAGE).forward(req,resp);
                        }
                        catch(DaoException e){
                            logger.warn("Error list all messages for user " + mainUser.getLogin(), e);
                            req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
                        }
                    }
                }
            if (action.get().equals("listBetween")) {
                Optional<Integer> partner1Optional=getIntParameterAsOptional(req,"id1");
                Optional<Integer> partner2Optional=getIntParameterAsOptional(req,"id2");
                if(partner1Optional.isPresent()&&partner2Optional.isPresent()){
                    try {
                        int dialogPartner=partner1Optional.get();
                        if(dialogPartner==mainUser.getId()){
                            dialogPartner=partner2Optional.get();
                        }
                        req.setAttribute("dialogPartner",dialogPartner);
                        List<Message> betweenUserMessages = messageDao.getMessagesBetweenIds(partner1Optional.get(),partner2Optional.get());
                        req.setAttribute("dialog",betweenUserMessages);

                        req.getRequestDispatcher(USER_MESSAGES_WITH_ANOTHER_USER_PAGE).forward(req,resp);
                    }
                    catch(DaoException e){
                        logger.warn("Error list all messages for user " + mainUser.getLogin(), e);
                        req.getRequestDispatcher(ERROR_PAGE).forward(req, resp);
                    }
                }
            }

            }
    }

    private Optional<Integer> getIntParameterAsOptional(HttpServletRequest request,String parameterName) {
        return Optional.ofNullable(request.getParameter(parameterName)).flatMap(id->{
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException ex) {
                logger.warn("Error parse Integer in message controller",ex);
                return Optional.empty();
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User sender = (User) req.getSession().getAttribute("user");
        Optional<String> action = Optional.ofNullable(req.getParameter("action")).filter(value -> value.matches("^send$"));
        if (sender == null) {
            req.getRequestDispatcher(NOT_REGISTERED_USER_PAGE).forward(req, resp);
        } else {
            int from = sender.getId();
            Optional<Integer> toOptional=getIntParameterAsOptional(req,"to");
                if(toOptional.isPresent()){
                    String text = req.getParameter("text");
                    if (text != null) {
                        try {
                            messageDao.insertMessage(from, toOptional.get(), text);
                            List<Message> betweenUserMessages = messageDao.getMessagesBetweenIds(toOptional.get(),sender.getId());
                            req.setAttribute("dialog",betweenUserMessages);
                            req.getRequestDispatcher(USER_MESSAGES_WITH_ANOTHER_USER_PAGE).forward(req, resp);
                        }
                        catch(DaoException ex){
                                logger.warn("Error send message to server",ex);
                        }
                    }
                }

            }
        }


}



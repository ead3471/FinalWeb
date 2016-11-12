package controllers;

import dao.MessageDao;
import model.Message;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-13.
 */
public class Messages extends HttpServlet {

    MessageDao messageDao;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> action=Optional.ofNullable(req.getParameter("action")).filter(value->value.matches("^(list|send)$"));

        User sender= (User) req.getSession().getAttribute("user");
        if(sender==null){
            //WTF??????
        }

        if(sender!=null&&action.isPresent()){
            if(action.get().equals("list")){
                //1 put list of messages into request
                //2 forward to messages.jsp
                return;
            }
            if(action.get().equals("send"))
            {
                Optional.ofNullable(req.getParameter("to")).ifPresent(id->{
                    Optional.ofNullable(req.getParameter("text")).filter(text->text.length()>0).ifPresent(text->
                        //messageDao.insertMessage(new Message(sender.getId(),Integer.parseInt(id),text));
                            System.out.println("1")
                    );


                });
            }
        }
        else{
            //forward to error page?
        }

    }
}

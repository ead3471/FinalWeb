package dao;

import dao.exceptions.DaoException;
import model.Message;
import model.User;
import org.junit.Test;
import pool.ConnectionPool;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Created by Freemind on 2016-11-09.
 */
public class MessageDaoTest {

    @Test
    public void testGetLastMessages() throws IOException, SQLException, DaoException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);

        MessageDao messageDao=new MessageDao(pool);

        List<Message> userLastMessages=messageDao.getLastMessages(5);

        userLastMessages.stream().forEach(message ->
                {
                    System.out.println(message);
                }
        );





    }

    @Test
    public void testInsertToBase() throws IOException, SQLException, DaoException, InterruptedException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);

        MessageDao messageDao=new MessageDao(pool);
        UserDao userDao=new UserDao(pool);
     List<User> usersInSystem=userDao.getAllUsers();
        Random rnd=new Random();



         Scanner dialogsScanner=new Scanner(new InputStreamReader(new FileInputStream("src/test/resources/Dialogs"), Charset.forName("UTF8")));
        dialogsScanner.useDelimiter(Pattern.compile("\\*"));
        HashMap<Integer,List<String>> dialogs=new HashMap<>();

        int i=0;
        while(dialogsScanner.hasNext()){
           String nextString=dialogsScanner.next();
          //  System.out.println(i+" |"+nextString+"|");

                dialogs.put(i++,Arrays.asList(nextString.trim().split("\\n")));


        }







        for(User user:usersInSystem){
            User dialogPartner=null;
            do{
                dialogPartner=usersInSystem.get(rnd.nextInt(usersInSystem.size()));
            }
            while(dialogPartner.getLogin().equals(user.getLogin()));

            List<String> dialog=dialogs.get(rnd.nextInt(dialogs.size()));

            int fromUserId=user.getId();
            int toUserId=dialogPartner.getId();


            for(String message:dialog){
                if(message!="\n")
                {
                    messageDao.insertMessage(fromUserId,toUserId,message);
                    int temp=fromUserId;
                    fromUserId=toUserId;
                    toUserId=temp;
                  Thread.sleep(rnd.nextInt(1000));
                }
            }
            System.out.println("User "+user.getId()+" ready");


        }


    }

}
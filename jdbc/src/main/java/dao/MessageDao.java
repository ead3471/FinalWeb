package dao;


import dao.exceptions.DaoException;
import model.Message;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freemind on 2016-11-07.
 */
public class MessageDao {

    private final Logger logger = LogManager.getLogger(UserDao.class);
    private ConnectionPool connectionPool;

    private final static String DIALOGS_TABLE = "dialogs";
    private static final String FROM = "from_id";
    private static final String TO = "to_id";
    private static final String TIMESTAMP = "timestamp";
    private static final String TEXT = "text";
    private static final String DIALOG_ID="dialog_id";


    private final static String USERS_TABLE = "users";
    private final static String USER_NAME ="name";
    private final static String USER_PHOTO="photo_url";
    private final static String USER_ROLE="role";
    private final static String USER_ID="id";




    private final static String INSERT_SQL="INSERT INTO "+DIALOGS_TABLE+" ("+FROM+","+TO+","+TEXT+") VALUES (";
    private final static String PREPARED_INSERT_SQL=INSERT_SQL+"?,?,?)";



    public MessageDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insertMessage(Message message) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
        Statement st=con.createStatement();){
            String sql=INSERT_SQL+"'"+message.getFromUser()+"','"+message.getToUser()+"','"+message.getText()+"')";
            st.executeUpdate(sql);
        }
        catch(InterruptedException|SQLException ex){
        throw new DaoException(ex);
        }
    }

    public void insertMessage(int fromId,int toId,String text) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
            Statement st=con.createStatement();){
            String sql=INSERT_SQL+"'"+fromId+"','"+toId+"','"+text+"')";
            st.executeUpdate(sql);
        }
        catch(InterruptedException|SQLException ex){
            throw new DaoException(ex);
        }
    }


    public void insertMessages(List<Message> messagesForInsert) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
            PreparedStatement preparedStatement=con.prepareStatement(PREPARED_INSERT_SQL)){

            for(Message message:messagesForInsert){
                try{
                    preparedStatement.setInt(1,message.getFromUser().getId());
                    preparedStatement.setInt(2,message.getToUser().getId());
                    preparedStatement.setString(3,message.getText());
                    preparedStatement.executeUpdate();
                }
                catch(SQLException ex){
                    logger.warn("Error insert message "+message,ex);

                }
            }
        }
        catch(SQLException|InterruptedException ex){
            throw new DaoException(ex);
        }
    }

    private List<Message> getMessagesBySql(String sql) throws DaoException {
        List<Message> resultList = new ArrayList<>();
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql);
        ) {
            while (rs.next()) {
                try {
                    User fromUser=new User(rs.getInt(FROM),rs.getString("from_"+USER_NAME),rs.getString("from_"+USER_PHOTO),rs.getString("from_"+USER_ROLE));
                    User toUser=new User(rs.getInt(TO),rs.getString("to_"+USER_NAME),rs.getString("to_"+USER_PHOTO),rs.getString("to_"+USER_ROLE));
                    resultList.add(new Message(fromUser, toUser, rs.getString(TEXT), rs.getTimestamp(TIMESTAMP).toInstant()));
                } catch (Exception ex) {
                    logger.warn("Error at create message", ex);
                }
            }
        } catch (SQLException | InterruptedException ex) {
            throw new DaoException(ex);
        }

        return resultList;

    }

    public List<Message> getMessagesByFilter(DaoFilter filter) throws DaoException {
        return getMessagesBySql(filter.build());
    }

    public List<Message> getAllUserMessages(int userId) throws DaoException {
        return getMessagesByFilter(filter().allWithUserId(userId).orderBy(TIMESTAMP, "DESC"));
    }

    public List<Message> getMessagesBetweenIds(int fromId, int toId) throws DaoException {
        return getMessagesByFilter(filter().
                addSpecialCondition(" WHERE "+TO+"="+toId+" AND "+FROM+"="+fromId).
                addSpecialCondition(" OR "+TO+"="+fromId+" AND "+FROM+"="+toId)
                .orderBy(TIMESTAMP, "DESC"));
    }

    public List<Message> getLastMessages(int userId) throws DaoException {
//        select from_id,to_id,text, max(timestamp) timestamp , u1.name as from_name, u2.name as to_name from dialogs d1
//        join users u1 on d1.from_id=u1.id
//        join users u2 on d1.to_id=u2.id
//        where d1.from_id=10 or d1.to_id=10
//        group by d1.dialog_id
//        order by timestamp desc;

        String sql="SELECT "+FROM+","+TO+","+TEXT+",MAX("+TIMESTAMP+") "+TIMESTAMP
                +",users_from."+USER_NAME+" AS from_"+USER_NAME+",users_from."+USER_ROLE+" AS from_"+USER_ROLE+",users_from."+USER_PHOTO+" AS from_"+USER_PHOTO
                +",users_to."+USER_NAME+" AS to_"+USER_NAME+",users_to."+USER_ROLE+" AS to_"+USER_ROLE+",users_to."+USER_PHOTO+" AS to_"+USER_PHOTO
                +" FROM "+DIALOGS_TABLE+" dialogs"
                +" JOIN "+USERS_TABLE+" users_from on dialogs."+FROM+"=users_from."+ USER_ID
                +" JOIN "+USERS_TABLE+" users_to on dialogs."+TO+"=users_to."+USER_ID
                +" WHERE dialogs."+FROM+"="+userId+" OR dialogs."+TO+"="+userId
                +" GROUP BY dialogs."+DIALOG_ID
                +" ORDER BY dialogs."+TIMESTAMP+" DESC";
      //  System.out.println(sql);
        return getMessagesBySql(sql);
    }


    public MessageFilter filter() {
        return new MessageFilter();
    }

    public class MessageFilter extends DaoFilter {
        //private final static String BASE_SELECT_SQL=

        private MessageFilter() {


            super("SELECT "+FROM+","+TO+","+TEXT+","+TIMESTAMP
                    +",users_from."+USER_NAME+" AS from_"+USER_NAME+",users_from."+USER_ROLE+" AS from_"+USER_ROLE+",users_from."+USER_PHOTO+" AS from_"+USER_PHOTO
                    +",users_to."+USER_NAME+" AS to_"+USER_NAME+",users_to."+USER_ROLE+" AS to_"+USER_ROLE+",users_to."+USER_PHOTO+" AS to_"+USER_PHOTO
                    +" FROM "+DIALOGS_TABLE+" dialogs"
                    +" JOIN "+USERS_TABLE+" users_from on dialogs."+FROM+"=users_from."+USER_ID
                    +" JOIN "+USERS_TABLE+" users_to on dialogs."+TO+"=users_to."+USER_ID
                    );
        }

        public MessageFilter allWithUserId(int userId) {
            return (MessageFilter) addOrCondition("dialogs."+FROM, String.valueOf(userId)).addOrCondition("dialogs."+TO, String.valueOf(userId));
        }

    }


}

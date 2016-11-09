package dao;

import dao.exceptions.DaoException;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pool.ConnectionPool;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freemind on 2016-11-07.
 */
public class MessageDao {

    private final Logger logger = LogManager.getLogger(UserDao.class);
    private ConnectionPool connectionPool;

    private final static String TABLE = "dialogs";
    private static final String FROM = "from_id";
    private static final String TO = "to_id";
    private static final String TIMESTAMP = "time_stamp";
    private static final String TEXT = "text";

    private final static String INSERT_SQL="INSERT INTO "+TABLE+" ("+FROM+","+TO+","+TEXT+") VALUES (";
    private final static String PREPARED_INSERT_SQL=INSERT_SQL+"?,?,?)";


    public MessageDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insertMessage(Message message) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
        Statement st=con.createStatement();){
            String sql=INSERT_SQL+"'"+message.getFromUserId()+"','"+message.getToUserId()+"','"+message.getText()+"')";
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
                    preparedStatement.setInt(1,message.getFromUserId());
                    preparedStatement.setInt(2,message.getToUserId());
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
                    resultList.add(new Message(rs.getInt(FROM), rs.getInt(TO), rs.getString(TEXT), rs.getTimestamp(TIMESTAMP).toInstant()));
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

    private List<Message> getMessagesBetweenIds(int fromId, int toId) throws DaoException {
        return getMessagesByFilter(filter().allWithUserId(fromId).allWithUserId(toId).orderBy(TIMESTAMP, "DESC"));
    }

    private List<Message> getLastMessages(int userId){

        //TODO:How  query only last messages??????
        return null;
    }


    public MessageFilter filter() {
        return new MessageFilter();
    }

    public class MessageFilter extends DaoFilter {
        private MessageFilter() {
            super("SELECT"
                    + FROM + ","
                    + TO + ","
                    + TEXT + " FROM " + TABLE);
        }

        public MessageFilter allWithUserId(int userId) {
            return (MessageFilter) addOrCondition(FROM, String.valueOf(userId)).addOrCondition(TO, String.valueOf(userId));
        }

    }


}

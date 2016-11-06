package dao;

import dao.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.User;

import pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-06.
 */
public class UserDao {
    private final ConnectionPool connectionPool;
    private final Logger logger= LogManager.getLogger(UserDao.class);


    private final static String ID ="short_name";
    private final static String FULL_NAME="name";
    private final static String PASS="password";
    private final static String PHOTO="photo_url";
    private final static String ROLE="role";
    private final static String TABLE="USERS";

    private final static String DELETE_SQL="DELETE FROM" +TABLE+ "WHERE"+ ID +"=";

    public UserDao(ConnectionPool connectionPool){
        this.connectionPool=connectionPool;
    }

    private List<User> getUsersBySql(String sql) throws DaoException {
        try( Connection con=connectionPool.takeConnection();
             Statement st=con.createStatement();
             ResultSet rs=st.executeQuery(sql)){

            List<User> resultList=new ArrayList<>();

            while(rs.next()){
                resultList.add(new User(rs.getInt(ID),
                        rs.getString(FULL_NAME),
                        rs.getString(PASS),
                        rs.getString(PHOTO),
                        rs.getString(ROLE)
                ));
            }
            return resultList;
        }
        catch (SQLException|InterruptedException ex){
            throw new DaoException(ex);
        }
    }

    public List<User> getUsersByFilter(UserFilter userFilter) throws DaoException {
        return getUsersBySql(userFilter.build());
    }

    public Optional<User> getUserByFilter(UserFilter userFilter) throws DaoException {
        userFilter.withLimit(1);
        List<User> resultList=getUsersByFilter(userFilter);
        if(resultList.size()>0){
            return Optional.of(resultList.get(1));
        }
        else
            return Optional.empty();
    }

    public  UserFilter filter(){
        return new UserFilter();
    }

    public void deleteUser(User user) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
            Statement st=con.createStatement();
        ){
            st.executeUpdate(DELETE_SQL+user.getId());
        }
        catch(InterruptedException|SQLException ex){
            throw new DaoException(ex);
        }

    }

    public void updateUser(User user) throws DaoException{
        StringBuilder stringBuilder=new StringBuilder("UPDATE"+TABLE+"SET"+FULL_NAME+"='")
                .append(user.getFullName()).append("',")
                .append(PASS).append("='").append(user.getPassword()).append("',")
                .append(ROLE).append("='").append(user.getRole()).append("',")
                .append(PHOTO).append("='").append(user.getPhotoUrl()).append("',")
                .append(" WHERE"+ ID).append("='").append(user.getId()).append("'");
        try(Connection con=connectionPool.takeConnection();
            Statement st=con.createStatement() ){
            st.executeUpdate(stringBuilder.toString());

        }
        catch (InterruptedException|SQLException ex){
            throw new DaoException(ex);
        }
    }

    public class UserFilter extends DaoFilter{


        private UserFilter(){
            resultSqlBuilder= new StringBuilder("SELECT"
                    + ID +","
                    +FULL_NAME+","
                    +PASS+","
                    +PHOTO+","
                    +ROLE+" FROM" +TABLE);

        }


        public DaoFilter withID(int id){
            return addCondition(ID,String.valueOf(id));
        }
        public DaoFilter withFullName(String fullName){
            return addCondition(FULL_NAME,fullName);
        }
        public DaoFilter withRole(String roleName){
            return addCondition(ROLE,roleName);
        }




    }
}

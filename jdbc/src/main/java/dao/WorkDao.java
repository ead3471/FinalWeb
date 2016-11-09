package dao;


import dao.exceptions.DaoException;
import model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pool.ConnectionPool;

import java.sql.*;
import java.util.*;

public class WorkDao {

    private final static String TABLE = "works";
    private final static String ID = "id";
    private final static String CREATOR = "creator_id";
    private static final String DESCRIPTION = "description";
    private static final String STATUS = "status";
    private static final String SHORT_DESCRIPTION = "short_description";
    private static final String REWARD="reward";
    private static final String RATE="rate";

    public static final String INSERT_SQL="INSERT INTO "+TABLE+" ("+CREATOR+","+DESCRIPTION+","+STATUS+","+SHORT_DESCRIPTION+","+REWARD+","+RATE+") VALUES (";
    public static final String PREPARED_INSERT_SQL=INSERT_SQL+"?,?,?,?,?,?)";



    private final ConnectionPool connectionPool;
    private final Logger logger = LogManager.getLogger(UserDao.class);

    public WorkDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private List<Work> getWorksBySql(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Work> resultList = new ArrayList<>();
            while (rs.next()) {
                try{
                    resultList.add(new Work(rs.getInt(ID),
                            rs.getInt(CREATOR),
                            rs.getString(DESCRIPTION),
                            rs.getInt(STATUS),
                            rs.getString(SHORT_DESCRIPTION),
                            rs.getInt(REWARD),
                            rs.getFloat(RATE)
                    ));
                }
                catch(SQLException ex){
                    logger.warn("Error create work",ex);
                }
            }
            return resultList;
        } catch (SQLException | InterruptedException ex) {
            throw new DaoException(ex);
        }
    }

    public List<Work> getWorksByFilter(WorkFilter workFilter) throws DaoException {
        return getWorksBySql(workFilter.build());
    }



    public Optional<Work> getWorkByFilter(WorkFilter workFilter) throws DaoException {
        workFilter.withLimit(1);
        List<Work> resultList = getWorksByFilter(workFilter);
        if (resultList.size() > 0) {
            return Optional.of(resultList.get(1));
        } else
            return Optional.empty();
    }

    public void insertWork(Work work) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
            Statement st=con.createStatement()){
            st.executeUpdate(INSERT_SQL
                    +"'"+work.getCreatorId()+"',"
                    +"'"+work.getDescription()+"',"
                    +"'"+work.getStatus()+"',"
                    +"'"+work.getShortDescription()+"',"
                    +"'"+work.getReward()+"',"
                    +"'"+work.getRate()+"')"

            );
            //+CREATOR+","+DESCRIPTION+","+STATUS+","+SHORT_DESCRIPTION+","+REWARD+","+RATE+
        }
        catch(InterruptedException|SQLException ex){
            throw new DaoException(ex);
        }
    }
    public void insertWork(List<Work> works) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
        PreparedStatement preparedStatement=con.prepareStatement(PREPARED_INSERT_SQL)){
            for(Work work:works){
                try {
                    preparedStatement.setInt(1, work.getCreatorId());
                    preparedStatement.setString(2, work.getDescription());
                    preparedStatement.setInt(3, work.getStatus());
                    preparedStatement.setString(4, work.getShortDescription());
                    preparedStatement.setInt(5, work.getReward());
                    preparedStatement.setFloat(6, work.getRate());
                    preparedStatement.executeUpdate();
                }
                catch(SQLException ex){
                    logger.warn("Error create work in base",ex);
                }
            }

            //+CREATOR+","+DESCRIPTION+","+STATUS+","+SHORT_DESCRIPTION+","+REWARD+","+RATE+
        }
        catch(InterruptedException|SQLException ex){
            throw new DaoException(ex);
        }
    }



    public WorkFilter filter() {
        return new WorkFilter();
    }

    public class WorkFilter extends DaoFilter {
        private WorkFilter() {
            super("SELECT"
                    + ID + ","
                    + CREATOR + ","
                    + STATUS + ","
                    + DESCRIPTION + ","
                    + SHORT_DESCRIPTION +","
                    + REWARD+","
                    + RATE+","
                    +" FROM" + TABLE);
        }

        public DaoFilter withID(int id) {
            return addAndCondition(ID, String.valueOf(id));
        }

        public DaoFilter withCreatorId(int id) {
            return addAndCondition(CREATOR, String.valueOf(id));
        }

        public DaoFilter withStatus(int status) {
            return addAndCondition(STATUS, String.valueOf(status));
        }
    }
}

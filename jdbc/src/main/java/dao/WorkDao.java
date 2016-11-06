package dao;


import dao.exceptions.DaoException;
import model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.security.SHA256;
import pool.ConnectionPool;

import java.sql.*;
import java.util.*;

public class WorkDao {

    private final String TABLE = "WORKS";
    private final String ID = "id";
    private final String CREATOR = "creator_id";
    private static final String DESCRIPTION = "description";
    private static final String STATUS = "status";
    private static final String SHORT_DESCRIPTION = "short_description";

    public static final String WORK_SPECS_SQL="SELECT spec_id FROM WORKS_SPECS WHERE work_id=?";



    private final ConnectionPool connectionPool;
    private final Logger logger = LogManager.getLogger(UserDao.class);

    public WorkDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private List<Work> getWorksBySql(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             PreparedStatement preparedStatement=con.prepareStatement(WORK_SPECS_SQL);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Work> resultList = new ArrayList<>();

            while (rs.next()) {
                try{
                    resultList.add(new Work(rs.getInt(ID),
                            rs.getInt(CREATOR),
                            rs.getString(DESCRIPTION),
                            rs.getInt(STATUS),
                            rs.getString(SHORT_DESCRIPTION)
                    ));
                }
                catch(SQLException ex){
                    logger.warn("Error create work",ex);
                }
            }

/*            rs.close();



            for(Work work:resultList){
                try(preparedStatement.setInt(1,work.getId());
                ResultSet resultSet=preparedStatement.executeQuery())
                {
                    preparedStatement.setInt(1, work.getId());
                    Set<String> workSpecsSet = new HashSet<>();
                }




            }
*/


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

    public WorkFilter filter() {
        return new WorkFilter();
    }

    public class WorkFilter extends DaoFilter {
        private WorkFilter() {
            resultSqlBuilder = new StringBuilder("SELECT"
                    + ID + ","
                    + CREATOR + ","
                    + STATUS + ","
                    + DESCRIPTION + ","
                    + SHORT_DESCRIPTION + " FROM" + TABLE);

        }

        public DaoFilter withID(int id) {
            return addCondition(ID, String.valueOf(id));
        }

        public DaoFilter withCreatorId(int id) {
            return addCondition(CREATOR, String.valueOf(id));
        }

        public DaoFilter withStatus(int status) {
            return addCondition(STATUS, String.valueOf(status));
        }
    }
}

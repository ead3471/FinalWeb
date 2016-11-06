package dao;


import dao.exceptions.DaoException;
import model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.security.SHA256;
import pool.ConnectionPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WorkDao {

    private final String TABLE = "WORKS";
    private final String ID = "id";
    private final String CREATOR = "creator_id";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String SHORT_DESCRIPTION = "short_description";

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
                resultList.add(new Work(rs.getInt(ID),
                        rs.getInt(CREATOR),
                        rs.getString(DESCRIPTION),
                        rs.getInt(STATUS),
                        rs.getString(SHORT_DESCRIPTION)
                ));
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

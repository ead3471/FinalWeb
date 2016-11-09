package dao;

import dao.exceptions.DaoException;
import model.Specialisation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-09.
 */
public class SpecialisationDao {
    private final Logger logger = LogManager.getLogger(SpecialisationDao.class);
    private final ConnectionPool connectionPool;


    private final static String TABLE = "specs";
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String ROOT = "root";


    private final static String DELETE_SQL = "DELETE FROM " + TABLE + " WHERE " + ID + "=";
    private final static String INSERT_SQL = "INSERT INTO "+TABLE+" (" + ID + "," + NAME + "," + ROOT + ") VALUES (";
    private final static String PREPARED_INSERT_SQL = INSERT_SQL + "?,?,?)";


    public SpecialisationDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private List<Specialisation> getSpecialisationsBySql(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Specialisation> resultList = new ArrayList<>();
            while (rs.next()) {
                try {
                    resultList.add(new Specialisation(rs.getInt(ID), rs.getInt(ROOT), rs.getString(NAME)));
                } catch (SQLException ex) {
                    logger.warn("Error create Specialisation", ex);
                }
            }
            return resultList;
        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }

    }

    public List<Specialisation> getAll() throws DaoException {
        return getSpecialisationsBySql(filter().build());
    }

    public List<Specialisation> getByFilter(DaoFilter filter) throws DaoException {
        return getSpecialisationsBySql(filter.build());
    }

    public Optional<Specialisation> getOneByFilter(SpecialisationFilter filter) throws DaoException {
        List<Specialisation> resultList = getByFilter(filter.withLimit(1));
        if (resultList.size() > 0) {
            return Optional.of(resultList.get(0));
        }
        return Optional.empty();
    }

    public void insertSpecialisation(Specialisation specialisation) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
        ) {
            System.out.println(INSERT_SQL + "'" + specialisation.getId() + "','" + specialisation.getName() + "','" + specialisation.getRoot() + "')");
            st.executeUpdate(INSERT_SQL + "'" + specialisation.getId() + "','" + specialisation.getName() + "','" + specialisation.getRoot() + "')");
        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }
    }


    public void insertSpecialisations(List<Specialisation> specialisationsList) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             PreparedStatement preparedStatement = con.prepareStatement(PREPARED_INSERT_SQL);
        ) {
            for (Specialisation spec : specialisationsList) {
                try {
                    preparedStatement.setInt(1, spec.getId());
                    preparedStatement.setString(2, spec.getName());
                    preparedStatement.setInt(3, spec.getRoot());
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    logger.warn("Error insert Specialisation", ex);
                }
            }

        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }
    }


    public SpecialisationFilter filter() {
        return new SpecialisationFilter();
    }

    public class SpecialisationFilter extends DaoFilter {
        private SpecialisationFilter() {
            super("SELECT " + ID + "," + NAME + "," + ROOT + " FROM " + TABLE);
        }

        public DaoFilter withId(int id) {
            return addAndCondition(ID, String.valueOf(id));
        }


        public DaoFilter withRoot(int root) {
            return addAndCondition(ROOT, String.valueOf(root));
        }

    }


}

package dao;

import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
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


    private final static String SPECS_TABLE = "specs";
    private final static String SPECS_ID = "id";
    private final static String SPECS_NAME = "name";
    private final static String SPECS_ROOT = "root";

//
//    private final static String USERS_TABLE ="users";
//    private final static String USER_ID="id";
//    private final static String USER_NAME="name";
//    private final static String USER_PHOTO="photo_url";
//    private final static String USER_ROLE="role";

    private final static String USERS_SPECS_TABLE="users_specs";
    private final static String USERS_SPECS_ID="spec_id";
    private final static String USERS_SPECS_USER_ID="user_id";


    private final static String DELETE_SQL = "DELETE FROM " + SPECS_TABLE + " WHERE " + SPECS_ID + "=";
    private final static String INSERT_SPEC_SQL = "INSERT INTO "+ SPECS_TABLE +" (" + SPECS_ID + "," + SPECS_NAME + "," + SPECS_ROOT + ") VALUES (";
    private final static String PREPARED_INSERT_SPEC_SQL = INSERT_SPEC_SQL + "?,?,?)";


    private final static String SELECT_SPECS_BY_USER_ID="SELECT "+SPECS_TABLE+"."+SPECS_ID+","
            +SPECS_TABLE+"."+SPECS_NAME+","
            +SPECS_TABLE+"."+SPECS_ROOT
            +" FROM "+SPECS_TABLE
            +" JOIN "+USERS_SPECS_TABLE+" ON "+USERS_SPECS_TABLE+"."+USERS_SPECS_ID+"="+SPECS_TABLE+"."+SPECS_ID
            +" WHERE "+USERS_SPECS_TABLE+"."+USERS_SPECS_USER_ID+"=";

    private final static String INSERT_USER_SPECS="INSERT INTO "+USERS_SPECS_TABLE+"("+USERS_SPECS_USER_ID+","+USERS_SPECS_ID+") VALUES (?,?)";

//FOR USER DAO
//    private final static String SELECT_SPECS_BY_USER_ID="SELECT "+ USERS_TABLE+"."+USER_ID+","+
//            USERS_TABLE+"."+USER_NAME+", "+USERS_TABLE+"."+USER_PHOTO+", "+USERS_TABLE+"."+USER_ROLE +" FROM "+USERS_TABLE
//            +" JOIN "+USERS_SPECS_TABLE+" US ON US."+USERS_SPECS_USER_ID+"="+USERS_TABLE+"."+USER_ID
//            +" JOIN "+SPECS_TABLE+ " SP ON US."+USERS_SPECS_ID+"="+"SP."+SPECS_ID
//            +"WHERE "+USERS_TABLE+"."+USER_ID+"=";



    public SpecialisationDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }



    public void insertUserSpecialisations(User user, List<Specialisation> userSpecs) throws DaoException {
        try(Connection con=connectionPool.takeConnection();
                PreparedStatement prep=con.prepareStatement(INSERT_USER_SPECS)){

            for(Specialisation specialisation:userSpecs){
                prep.setInt(1,user.getId());
                prep.setInt(2,specialisation.getId());
                prep.addBatch();
            }
            prep.executeBatch();

        }
        catch(SQLException|InterruptedException ex){
            throw new DaoException(ex);
        }
    }

    private List<Specialisation> getSpecialisationsBySql(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Specialisation> resultList = new ArrayList<>();
            while (rs.next()) {
                try {
                    resultList.add(new Specialisation(rs.getInt(SPECS_ID), rs.getInt(SPECS_ROOT), rs.getString(SPECS_NAME)));
                } catch (SQLException ex) {
                    logger.warn("Error create Specialisation", ex);
                }
            }
            return resultList;
        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }

    }

    public List<Specialisation> getByUserId(int userId) throws DaoException {
        return getSpecialisationsBySql(SELECT_SPECS_BY_USER_ID+userId);
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
            System.out.println(INSERT_SPEC_SQL + "'" + specialisation.getId() + "','" + specialisation.getName() + "','" + specialisation.getRoot() + "')");
            st.executeUpdate(INSERT_SPEC_SQL + "'" + specialisation.getId() + "','" + specialisation.getName() + "','" + specialisation.getRoot() + "')");
        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }
    }


    public void insertSpecialisations(List<Specialisation> specialisationsList) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             PreparedStatement preparedStatement = con.prepareStatement(PREPARED_INSERT_SPEC_SQL);
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
            super("SELECT " + SPECS_ID + "," + SPECS_NAME + "," + SPECS_ROOT + " FROM " + SPECS_TABLE);
        }

        public DaoFilter withId(int id) {
            return addAndCondition(SPECS_ID, String.valueOf(id));
        }




        public DaoFilter withRoot(int root) {
            return addAndCondition(SPECS_ROOT, String.valueOf(root));
        }

    }


}

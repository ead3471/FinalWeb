package dao;

import dao.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.User;

import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Freemind on 2016-11-06.
 */
public class UserDao {

    private final ConnectionPool connectionPool;
    private final Logger logger = LogManager.getLogger(UserDao.class);


    private final static String ID = "id";
    private final static String LOGIN = "login";
    private final static String FULL_NAME = "name";
    private final static String PASS = "password";
    private final static String PHOTO = "photo_url";
    private final static String ROLE = "role";
    private static final String RATE = "rate";
    private final static String TABLE = "users";

    private final static String DELETE_SQL = "DELETE FROM" + TABLE + "WHERE" + ID + "=";
    private final static String INSERT_SQL = new StringBuilder("INSERT INTO ").append(TABLE).append(" (")
            .append(LOGIN).append(",")
            .append(FULL_NAME).append(",")
            .append(PASS).append(",")
            .append(ROLE).append(") VALUES ").toString();

    private final static String PREPARED_INSERT_SQL = INSERT_SQL + "(?,?,?,?)";


    public UserDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    private List<User> getUsersBySql(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<User> resultList = new ArrayList<>();

            while (rs.next()) {
                resultList.add(new User(rs.getInt(ID),
                        rs.getString(LOGIN),
                        rs.getString(FULL_NAME),
                        rs.getString(PASS),
                        rs.getString(PHOTO),
                        rs.getString(ROLE),
                        rs.getFloat(RATE)
                ));
            }
            return resultList;
        } catch (SQLException | InterruptedException ex) {
            throw new DaoException(ex);
        }
    }

    public List<User> getUsersByFilter(UserFilter userFilter) throws DaoException {
        return getUsersBySql(userFilter.build());
    }


    public Optional<User> getUserByFilter(UserFilter userFilter) throws DaoException {
        userFilter.withLimit(1);
        List<User> resultList = getUsersByFilter(userFilter);
        if (resultList.size() > 0) {
            return Optional.of(resultList.get(0));
        } else
            return Optional.empty();
    }

    public List<User> getAllUsers() throws DaoException {
        return getUsersByFilter(filter());
    }

    public UserFilter filter() {
        return new UserFilter();
    }

    public void deleteUser(User user) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement();
        ) {
            st.executeUpdate(DELETE_SQL + user.getId());
        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }

    }

    public void fullUpdateUser(User newUser) throws DaoException {
        StringBuilder stringBuilder = new StringBuilder("UPDATE" + TABLE + " SET " + FULL_NAME + "='")
                .append(newUser.getFullName()).append("',")
                .append(PASS).append("='").append(newUser.getPassword()).append("',")
                .append(ROLE).append("='").append(newUser.getRole()).append("',")
                .append(PHOTO).append("='").append(newUser.getPhotoUrl()).append("',")
                .append(RATE).append("='").append(newUser.getRate()).append("',")
                .append(" WHERE " + ID).append("='").append(newUser.getId()).append("'");
        executeUpdate(stringBuilder.toString());
    }

    public void updateUserLimited(int userId, String newFullName, String newPassword, String newPhotoUrl) throws DaoException {
        StringBuilder stringBuilder = new StringBuilder("UPDATE " + TABLE + " SET " + FULL_NAME + "='" + newFullName)
                .append("',").append(PASS).append("='").append(newPassword)
                .append("',").append(PHOTO).append("='").append(newPhotoUrl)
                .append("' WHERE id=").append(userId);
        executeUpdate(stringBuilder.toString());
    }



    private void executeUpdate(String sql) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate(sql);

        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }
    }


    public void insertUser(User user) throws DaoException {
        StringBuilder stringBuilder = new StringBuilder(INSERT_SQL)
                .append("('").append(user.getLogin()).append("'").append(",")
                .append("'").append(user.getFullName()).append("'").append(",")
                .append("'").append(user.getPassword()).append("'").append(",")
                .append("'").append(user.getRole()).append("')");

        try (Connection con = connectionPool.takeConnection();
             Statement st = con.createStatement()) {
            st.executeUpdate(stringBuilder.toString());
        } catch (SQLException | InterruptedException ex) {
            throw new DaoException(ex);
        }
    }


    public void insertUsers(List<User> users) throws DaoException {
        try (Connection con = connectionPool.takeConnection();
             PreparedStatement preparedStatement = con.prepareStatement(PREPARED_INSERT_SQL);) {

            for (User user : users) {
                try {
                    preparedStatement.setString(1, user.getLogin());
                    preparedStatement.setString(2, user.getFullName());
                    preparedStatement.setString(3, user.getPassword());
                    preparedStatement.setString(4, user.getRole());
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    logger.warn("Error at user add " + user, ex);
                }
            }

        } catch (InterruptedException | SQLException ex) {
            throw new DaoException(ex);
        }


    }

    public class UserFilter extends DaoFilter {
        private UserFilter() {
            super("SELECT "
                    + ID + ","
                    + LOGIN + ","
                    + FULL_NAME + ","
                    + PASS + ","
                    + PHOTO + ","
                    + ROLE + ","
                    + RATE
                    + " FROM " + TABLE);
        }


        public DaoFilter withID(int id) {
            return addAndCondition(ID, String.valueOf(id));
        }

        public DaoFilter withFullName(String fullName) {
            return addAndCondition(FULL_NAME, fullName);
        }

        public DaoFilter withRole(String roleName) {
            return addAndCondition(ROLE, roleName);
        }

        public DaoFilter withRateGreaterThen(String value) {
            return addAndCondition(ROLE, ">", value);
        }


        public UserFilter withLogin(String login) {
            return (UserFilter) addAndCondition(LOGIN, login);
        }


        public UserFilter withPass(String pass) {
            return (UserFilter) addAndCondition(PASS, pass);
        }

    }
}

package dao.exceptions;

import pool.ConnectionPool;

/**
 * Created by Freemind on 2016-11-17.
 */
public class UserSpecialisationDao {
    private ConnectionPool connectionPool;
    private final static String USER_SPECS_TABLE="users_specs";
    private final static String USER_ID="user_id";
    private final static String USER_SPEC_ID="spec_id";

    private final static String SPECS_TABLE="specs";
    private final static String SPEC_ID="id";
    private final static String SPEC_NAME="name";
    private final static String SPEC_ROOT="root";

    private final static String SELECT_BY_USER_ID="SELECT ";



    public UserSpecialisationDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }


}

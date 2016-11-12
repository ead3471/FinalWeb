package listeners;

import dao.MessageDao;
import dao.SpecialisationDao;
import dao.UserDao;
import dao.WorkDao;
import model.RolesInspector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pool.ConnectionPool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class ServerSetupLoader implements ServletContextListener {

    private final Logger logger= LogManager.getLogger(ServerSetupLoader.class);

    private final  String DB_CONN_INIT_KEY="sqlConnectionProperties";

    private final static String USER_DAO_KEY="userDao";
    private final static String WORK_DAO_KEY="workDao";
    private final static String ROLES_INSPECTOR_KEY="rolesInspector";
    private final static String MESSAGES_DAO_KEY="messagesDao";
    private final static String SPEC_DAO_KEY="specDao";


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPool connectionPool=initConnectionPool(sce.getServletContext());
            initDAO(connectionPool,sce.getServletContext());
            initRolesInspector(sce);
        } catch (Exception e) {
            logger.warn("Error during connection pool init",e);
        }

    }

    private void initDAO(ConnectionPool connectionPool, ServletContext servletContext){
        UserDao userDao=new UserDao(connectionPool);
        servletContext.setAttribute(USER_DAO_KEY,userDao);

        WorkDao workDao=new WorkDao(connectionPool);
        servletContext.setAttribute(WORK_DAO_KEY,workDao);

        MessageDao messageDao=new MessageDao(connectionPool);
        servletContext.setAttribute(MESSAGES_DAO_KEY,messageDao);

        SpecialisationDao specialisationDao=new SpecialisationDao(connectionPool);
        servletContext.setAttribute(SPEC_DAO_KEY,specialisationDao);
    }

    private ConnectionPool initConnectionPool(ServletContext servletContext) throws IOException, SQLException {
        String initBaseConnectionFile=servletContext.getRealPath("/WEB-INF/classes/")+servletContext.getInitParameter(DB_CONN_INIT_KEY);

        Properties dbConnSetup=new Properties();
        try(FileReader reader=new FileReader(initBaseConnectionFile) ){
            dbConnSetup.load(reader);
            return ConnectionPool.getInstance(dbConnSetup);
        }
    }
    private void initRolesInspector(ServletContextEvent sce){
        sce.getServletContext().setAttribute(ROLES_INSPECTOR_KEY, RolesInspector.getMockRolesInspector());
    }




}

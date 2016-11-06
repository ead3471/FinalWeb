package pool;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by Freemind on 2016-10-16.
 */


@SuppressWarnings("Duplicates")
public class ConnectionPool implements AutoCloseable {

    private final static Logger logger= LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool(){};
    private  ArrayBlockingQueue<PooledConnection> connectionsQueue;

    public static ConnectionPool getInstance(Properties setup) throws SQLException {
        int poolSize=Integer.parseInt(setup.getProperty("poolSize","5"));
        ConnectionPool resultPool=new ConnectionPool();

        resultPool.connectionsQueue =new ArrayBlockingQueue<PooledConnection>(poolSize);
        try {
            Class.forName(setup.getProperty("driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(int i=0;i<poolSize;i++){
            resultPool.connectionsQueue.add(new PooledConnection(DriverManager.getConnection(setup.getProperty("url"),setup),resultPool));
        }
        return resultPool;
    }

    public Connection takeConnection() throws InterruptedException {
        return connectionsQueue.take();
    }

    void freeConnection(PooledConnection freeConnection){
        try {
            connectionsQueue.put(freeConnection);
        } catch (InterruptedException e) {
           logger.warn(e);
        }
    }

    public void close(){
        for(PooledConnection connection: connectionsQueue){
            try{
                connection.reallyCloseInternalConnection();
            }
            catch(Exception ex){
                logger.error("Error closing connection "+connection);
            }

        }
    }
     public static void executeSQLFromFile(String fileWithSql, Connection connection) throws FileNotFoundException, SQLException, UnsupportedEncodingException {
        try(Scanner fileScanner=new Scanner(new InputStreamReader(new FileInputStream(fileWithSql), "UTF8"));
            Statement st=connection.createStatement()){
            fileScanner.useDelimiter(";");
            while (fileScanner.hasNext())
            {
                String sqlString=fileScanner.next().replace("\n","");
                if(!sqlString.startsWith("//")) {
                    st.addBatch(sqlString);
                    st.executeBatch();
                }
            }
        }
    }

}

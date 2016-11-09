package dao;

import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
import org.junit.Test;
import pool.ConnectionPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Freemind on 2016-11-08.
 */
public class UserDaoTest {
    @Test
    public void createUsersBase() throws IOException, SQLException, DaoException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);
        Charset charset=Charset.forName("UTF8");
        List<String> names= Files.readAllLines(new File("src/test/resources/MaleNames").toPath(), charset);
        List<String> lastNames= Files.readAllLines(new File("src/test/resources/MaleLastNames").toPath(),charset);
        UserDao userDao=new UserDao(pool);
        List<User> testList=new ArrayList<>();
        int i=0;
        Random rnd=new Random();
        int namesSize=names.size();
        int lastNamesSize=lastNames.size();
        while(i++<200)
        {
            String name=names.get(rnd.nextInt(namesSize));
            String login=name+rnd.nextInt(200);
            User newUser=new User(name+" "+lastNames.get(rnd.nextInt(lastNamesSize)),login,String.valueOf(rnd.nextInt(100)),null,"client") ;
            testList.add(newUser);
        }
        userDao.insertUsers(testList);
    }


    @Test
    public void insertUserRates() throws IOException, SQLException, DaoException, InterruptedException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);

        Random rnd=new Random();

        SpecialisationDao specialisationDao=new SpecialisationDao(pool);
        UserDao userDao=new UserDao(pool);

        List<Specialisation> allSpecs=specialisationDao.getAll();
        List<User> allUsers=userDao.getUsersByFilter((UserDao.UserFilter) userDao.filter().withRole("worker"));

        Connection workConnection=pool.takeConnection();

        for(User user:allUsers) {
            int specsCount = rnd.nextInt(10) + 1;
            Set<Specialisation> specs = new HashSet<>();
            while (specs.size() < specsCount) {
                specs.add(allSpecs.get(rnd.nextInt(allSpecs.size())));
            }

            insertUserRates(workConnection,specs,user);


        }




    }



    private void insertUserRates(Connection con,Set<Specialisation> specs,User user) throws SQLException {
        Random rnd=new Random();

        for(Specialisation spec:specs) {
            con.createStatement().executeUpdate("INSERT INTO rates (worker_id,spec_id,rate) VALUES ('" + user.getId() + "','" +spec.getId()+"','"+rnd.nextFloat()*5.0+"')");
        }
    }

}
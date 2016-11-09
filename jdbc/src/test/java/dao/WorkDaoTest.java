package dao;

import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
import model.Work;
import org.junit.Test;
import pool.ConnectionPool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Freemind on 2016-11-10.
 */
public class WorkDaoTest {

    @Test
    public void testInsertWork() throws DaoException, SQLException, IOException, InterruptedException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);
        SpecialisationDao specialisationDao=new SpecialisationDao(pool);
        UserDao userDao=new UserDao(pool);
        WorkDao workDao=new WorkDao(pool);

        List<Specialisation> allSpecs=specialisationDao.getAll();
        List<User> allUsers=userDao.getUsersByFilter((UserDao.UserFilter) userDao.filter().withRole("client"));

        Random rnd=new Random();


        for(int i=0;i<500;i++){
            int specsCount=rnd.nextInt(5)+1;
            User workStartUser=allUsers.get(rnd.nextInt(allUsers.size()));


            Set<Specialisation> specs=new HashSet<>();

            String shortDescription="Short Desc:";
            String description="FULL DESCRIPTION:";
            while(specs.size()<specsCount){
                specs.add(allSpecs.get(rnd.nextInt(allSpecs.size())));
            }

            for(Specialisation spec:specs){
                shortDescription+=":"+spec.getName();
                description+=":"+spec.getName()+"\n";
            }

            Work work=new Work(workStartUser.getId(),description,0,shortDescription,rnd.nextInt(500),rnd.nextInt(5));
            workDao.insertWork(work);
            insertWorkSpecifications(pool.takeConnection(),work,specs);




        }



    }

    private void insertWorkSpecifications(Connection connection, Work work,Set<Specialisation> specs) throws SQLException {
        Statement st=connection.createStatement();
        for(Specialisation spec:specs){
            st.executeUpdate("INSERT INTO work_specs (work_id,spec_id) VALUES ('"+work.getId()+"','"+spec.getId()+"')");
        }
        connection.close();
    }

}
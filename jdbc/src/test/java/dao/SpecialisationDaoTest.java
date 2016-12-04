package dao;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import dao.exceptions.DaoException;
import model.Specialisation;
import model.User;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pool.ConnectionPool;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Freemind on 2016-11-09.
 */
public class SpecialisationDaoTest {

    @Test
     public void insertSpecialisationTest() throws SQLException, IOException, DaoException, SAXException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
       ConnectionPool pool=ConnectionPool.getInstance(prop);
      SpecialisationDao specialisationDao=new SpecialisationDao(pool);

        DOMParser parser = new DOMParser();

        parser.parse("src/test/resources/Specs.xml");

        Document document = parser.getDocument();
        Element root = document.getDocumentElement();
        NodeList mainNodes=root.getChildNodes();
        List<Specialisation> resultList=new ArrayList<>();
        int specsCount=0;

        for(int i=0;i<mainNodes.getLength();i++){
            Node currentNode=mainNodes.item(i);
            if(currentNode.getNodeType()==Node.ELEMENT_NODE) {
                Element mainNode = (Element) (mainNodes.item(i));
                Specialisation mainSpec = new Specialisation(++specsCount, 0, mainNode.getAttribute("name"));
                resultList.add(mainSpec);
                NodeList subNodes = mainNode.getElementsByTagName("spec");
                for (int j = 0; j < subNodes.getLength(); j++) {
                    {
                        Element subSpec = (Element) subNodes.item(j);
                        resultList.add(new Specialisation(++specsCount, mainSpec.getId(), subSpec.getAttribute("name")));
                    }
                }
            }
        }
        specialisationDao.insertSpecialisations(resultList);
//        resultList.stream().filter(spec->spec.getRoot()==0).forEach(spec-> System.out.println(spec));
    }

    private List<Specialisation> getAllSpecialisations(Element rootElement,int rootElementId,int elementsCount){
        NodeList mainNodes=rootElement.getElementsByTagName("spec");
        List<Specialisation> resultList=new ArrayList<>();
        if(mainNodes.getLength()>0){
            for(int i=0;i<mainNodes.getLength();i++)
                resultList.addAll(getAllSpecialisations((Element)mainNodes.item(i),rootElementId,++elementsCount));
        }
        else{
            if(rootElement.hasAttribute("name"))
                resultList.add(new Specialisation(elementsCount,rootElementId,rootElement.getAttribute("name")));
        }
        return resultList;
    }


    @Test
    public void testInsertUserSpecialisations() throws IOException, SQLException, DaoException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);
        SpecialisationDao specialisationDao=new SpecialisationDao(pool);
        UserDao userDao=new UserDao(pool);

        List<User> users=userDao.getAllUsers();
        List<Specialisation> rootSpecs=specialisationDao.getByFilter(specialisationDao.filter().withRoot(0));

        Random rnd=new Random();

        for(User user:users){
            List<Specialisation> userSpecsList=new ArrayList<>();
            for(Specialisation rootSpec:rootSpecs){
                if(rnd.nextBoolean()) {
                    List<Specialisation> subSpec = specialisationDao.getByFilter(specialisationDao.filter().withRoot(rootSpec.getId()));
                    for (int i = 0; i < subSpec.size(); i++) {
                        if (rnd.nextBoolean()) {
                            userSpecsList.add(subSpec.get(i));
                        }
                    }

                }
            }
            specialisationDao.insertUserSpecialisations(user,userSpecsList);


        }




    }

    @Test
    public void testGetSpecialisationsTree() throws SQLException, IOException, DaoException {

        Properties prop=new Properties();
        prop.load(new FileInputStream("src/test/resources/db/db.properties"));
        ConnectionPool pool=ConnectionPool.getInstance(prop);
        SpecialisationDao specialisationDao=new SpecialisationDao(pool);
        Map<Specialisation,Collection<Specialisation>> specsTree=specialisationDao.getUserSpecialisationsTree(1);

        specsTree.keySet().stream().forEach(rootSpec ->{
            System.out.println(rootSpec.getName());
            specsTree.get(rootSpec).forEach(subSpec-> System.out.println("  "+subSpec.getName()) );
        }
        );
        //System.out.println(specsTree);



    }


}
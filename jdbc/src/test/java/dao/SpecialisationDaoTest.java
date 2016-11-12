package dao;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import dao.exceptions.DaoException;
import model.Specialisation;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pool.ConnectionPool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.*;

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



}
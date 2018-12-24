package sc.ustc.dao;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sc.ustc.bean.OR_class;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private static String path = Thread.currentThread().getContextClassLoader().getResource("or_mapping.xml").getPath();

    public static Map<String, String> jdbc_config() {
        try {
            Document document = getDoc();
            NodeList jdbc = document.getElementsByTagName("jdbc");
            NodeList jdbcPs = ((Element) jdbc.item(0)).getElementsByTagName("property");

            Map<String, String> jdbcConfig = new HashMap<String, String>();
            for (int i = 0; i < jdbcPs.getLength(); i++) {
                jdbcConfig.put(((Element) jdbcPs.item(i)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue(), ((Element) jdbcPs.item(i))
                        .getElementsByTagName("value").item(0).getFirstChild().getNodeValue());
            }

            return jdbcConfig;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static OR_class class_config(String className) {

        try {
            Document document = getDoc();
            NodeList classList = document.getElementsByTagName("class");
            OR_class orCla = new OR_class();

            for (int i = 0; i < classList.getLength(); i++) {
                Element cla = (Element) classList.item(i);
                System.out.println(cla.getAttribute("name"));
                if (cla.getAttribute("name").equals(className)) {
                    System.out.println("equals");
                    orCla.setName(cla.getAttribute("name"));
                    orCla.setTable(cla.getAttribute("table"));
                    NodeList propertyLi = cla.getElementsByTagName("property");
                    List<List<String>> propertyList = new ArrayList<List<String>>();
                    for (int j = 0; j < propertyLi.getLength(); j++) {
                        List<String> property = new ArrayList<String>();

                        property.add(((Element) propertyLi.item(j)).getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
                        property.add(((Element) propertyLi.item(j)).getElementsByTagName("column").item(0).getFirstChild().getNodeValue());
                        property.add(((Element) propertyLi.item(j)).getElementsByTagName("type").item(0).getFirstChild().getNodeValue());
                        property.add(((Element) propertyLi.item(j)).getElementsByTagName("lazy").item(0).getFirstChild().getNodeValue());

                        propertyList.add(property);
                    }
                    orCla.setPropertyList(propertyList);
                    return orCla;
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Document getDoc() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(path));
        return document;
    }
}
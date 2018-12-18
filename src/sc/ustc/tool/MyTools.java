package sc.ustc.tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import sc.ustc.bean.ActionBean;

public class MyTools {

    public ActionBean readXml_dom(String actionName, String path) {

        try {
            // controller.xml解析（DOM）
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder buillder = dbf.newDocumentBuilder();
            Document doc = buillder.parse(new File(path));
            // 解析interceptor节点
            NodeList interceptorsLi = doc.getElementsByTagName("interceptor");
            Map<String, List<String>> InterceptorMap = new HashMap<String, List<String>>();
            for (int i = 0; i < interceptorsLi.getLength(); i++) {
                List<String> interDirts = new ArrayList<String>();
                Element interceptor = (Element) interceptorsLi.item(i);
                interDirts.add(interceptor.getAttribute("name"));
                interDirts.add(interceptor.getAttribute("class"));
                interDirts.add(interceptor.getAttribute("predo"));
                interDirts.add(interceptor.getAttribute("afterdo"));
                InterceptorMap.put(interceptor.getAttribute("name"), interDirts);
            }
            // 解析action节点
            NodeList actions = doc.getElementsByTagName("action");
            for (int i = 0; i < actions.getLength(); i++) {
                Element action = (Element) actions.item(i);
                if (action.getAttribute("name").equals(actionName)) {
                    // 获取result节点
                    Map<String, List<String>> actionResultMap = new HashMap<String, List<String>>();
                    NodeList results = action.getElementsByTagName("result");
                    for (int j = 0; j < results.getLength(); j++) {
                        Element childNode = (Element) results.item(j);
                        List<String> resultList = new ArrayList<String>();
                        resultList.add(childNode.getAttribute("name"));
                        resultList.add(childNode.getAttribute("type"));
                        resultList.add(childNode.getAttribute("value"));
                        actionResultMap.put("result" + childNode.getAttribute("name"), resultList);

                    }
                    // 获取interceptor节点
                    Map<String, List<String>> actionInterceptors = new HashMap<String, List<String>>();
                    NodeList interceptors = action.getElementsByTagName("interceptro-ref");
                    for (int j = 0; j < interceptors.getLength(); j++) {
                        Element childNode = (Element) interceptors.item(j);
                        actionInterceptors.put(String.valueOf(j), InterceptorMap.get(childNode.getAttribute("name")));
                    }
                    return new ActionBean(actionName, action.getAttribute("class"), action.getAttribute("method"), actionResultMap, actionInterceptors);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
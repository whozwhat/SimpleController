package sc.ustc.tool;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sc.ustc.bean.DIBean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DITools {
    public static DIBean readXML(String beanName, String path) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder buillder = dbf.newDocumentBuilder();
            Document doc = buillder.parse(new File(path));
            // 解析DI节点
            NodeList beanList = doc.getElementsByTagName("bean");
            for (int i = 0; i < beanList.getLength(); i++) {
                Element bean = (Element) beanList.item(i);
                if (bean.getAttribute("id").equals(beanName)) {
                    List<String> reList = new ArrayList<>();
                    reList.add(bean.getAttribute("id"));
                    reList.add(bean.getAttribute("class"));
                    NodeList diList = bean.getElementsByTagName("field");
                    List<List<String>> reDIList = new ArrayList<>();
                    for (int n = 0; n < diList.getLength(); n++) {
                        Element di = (Element) diList.item(n);
                        List<String> field = new ArrayList<>();
                        field.add(di.getAttribute("name"));
                        field.add(di.getAttribute("bean-ref"));
                        reDIList.add(field);
                    }
                    return new DIBean(reList.get(0), reList.get(1), reDIList);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object di(Object o, DIBean diBean, String path) {
        try {
            Object reBean=null;
            if (o == null) {
                Class cl = Class.forName(diBean.getClassName());
                System.out.println(diBean.getClassName());
                reBean = cl.newInstance();
            } else reBean = o;
            for (int i = 0; i < diBean.getDiList().size(); i++) {
                System.out.println(i);
                DIBean refbean = DITools.readXML(diBean.getDiList().get(i).get(1), path);
                System.out.println("refbean="+refbean);
                PropertyDescriptor propertyDescriptor =
                        new PropertyDescriptor(diBean.getDiList().get(i).get(0), reBean.getClass());
                Method method = propertyDescriptor.getWriteMethod();
                System.out.println(method);
                method.invoke(reBean, DITools.di(null, refbean, path));
            }
            return reBean;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
  /*  public static Object getBean(DIBean diBean,String path){
        try {
            Class cl = Class.forName(diBean.getClassName());
                    Object reBean = cl.newInstance();
                    for(int i=0;diBean.getDiList()!=null&&i<diBean.getDiList().size();i++){
                        DIBean refbean = DITools.readXML(diBean.getDiList().get(i).get(1),path);
                        PropertyDescriptor propertyDescriptor =
                                new PropertyDescriptor(diBean.getDiList().get(i).get(0),Class.forName(refbean.getClassName()));
                        Method method = propertyDescriptor.getWriteMethod();
                        method.invoke(reBean,DITools.getBean(refbean,path));
                    }
                    return reBean;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}*/
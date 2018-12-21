package sc.ustc.tool;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.FileOutputStream;

public class Xml2Html {
    public static String getHtmlPath(String realPath){
        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();

            Transformer transformer =
                    tFactory.newTransformer
                            (new javax.xml.transform.stream.StreamSource
                                    (realPath+"WEB-INF/classes/xslt.xsl"));

            transformer.transform
                    (new javax.xml.transform.stream.StreamSource
                                    (realPath+"WEB-INF/classes/success_view.xml"),
                            new javax.xml.transform.stream.StreamResult
                                    ( new FileOutputStream(realPath+"/LogWelcome.html")));
        }
        catch (Exception e) {
            e.printStackTrace( );
        }
        return ("/LogWelcome.html");
    }
}

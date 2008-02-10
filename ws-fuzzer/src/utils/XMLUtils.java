/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.util.XMLPrettyPrinter;

/**
 *
 * @author chang
 */
public class XMLUtils {

    public static String prettify(String xmlString) throws XMLStreamException, Exception{
        
        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
        StAXOMBuilder builder = new StAXOMBuilder(xmlStreamReader);
        
        return toPrettifiedString(builder.getDocumentElement());
        
    }
    
    public static String toPrettifiedString(OMElement element) throws Exception{
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLPrettyPrinter.prettify(element, baos);
        
        String prettyXMLString = baos.toString();
        baos.close();
        
        return prettyXMLString;
        
    }
    
}

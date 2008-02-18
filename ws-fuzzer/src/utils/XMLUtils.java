/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
        return toPrettifiedString(toOMElement(xmlString));
    }
    
    public static String toPrettifiedString(OMElement element) throws Exception{
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLPrettyPrinter.prettify(element, baos);
        
        String prettyXMLString = baos.toString();
        baos.close();
        
        return prettyXMLString;
        
    }
    
    public static OMElement toOMElement(String xmlString) throws XMLStreamException{
        
        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
        StAXOMBuilder builder = new StAXOMBuilder(xmlStreamReader);
        
        return builder.getDocumentElement();
    }
    
    public static OMElement toOMElement(File xmlFile) throws FileNotFoundException, XMLStreamException{
        
        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(xmlFile));
        StAXOMBuilder builder = new StAXOMBuilder(xmlStreamReader);
        
        OMElement element = builder.getDocumentElement();
//        xmlStreamReader.close();
        
        return element;
    }
    
    public static void saveToFile(OMElement element, File file) throws IOException, XMLStreamException, Exception{
        
        if(!file.exists()){
            if(!file.createNewFile()){
                return;
            }
        }
        
        if(file.exists() && file.canWrite()){
            FileWriter writer = new FileWriter(file);
            writer.write(toPrettifiedString(element));
            writer.flush();
            writer.close();
        }
    }
    
}

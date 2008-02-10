/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import com.ibm.wsdl.extensions.schema.SchemaImpl;
import com.ibm.wsdl.xml.WSDLReaderImpl;
import com.ibm.wsdl.xml.WSDLWriterImpl;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.w3c.dom.Element;

/**
 *
 * @author chang
 */
public class WSDLUtils {

    private Definition wsdl4jDef;
    
    public WSDLUtils(Definition wsdl4jDef){
        this.wsdl4jDef = wsdl4jDef;
    }
    
    public WSDLUtils(String wsdlURI) throws WSDLException{
        WSDLReader wsdlReader = new WSDLReaderImpl();
        wsdlReader.setFeature("javax.wsdl.verbose", false);
        wsdl4jDef = wsdlReader.readWSDL(wsdlURI);
    }
    
    public Definition getWSDL4jDef(){
        return wsdl4jDef;
    }
    
    public String getWSDLRaw() throws WSDLException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WSDLWriter wsdlWriter = new WSDLWriterImpl();
        wsdlWriter.writeWSDL(wsdl4jDef, baos);
        return new String(baos.toByteArray());
    }
    
    public ArrayList<XmlSchema> getXmlSchemaFromTypes() {

        List<ExtensibilityElement> elements = this.wsdl4jDef.getTypes().getExtensibilityElements();
        ArrayList<XmlSchema> schemas = new ArrayList<XmlSchema>();

        for (ExtensibilityElement element : elements) {

            if (element instanceof SchemaImpl) {

                Element domElement = ((SchemaImpl) element).getElement();

                QName qName = element.getElementType();
                String prefix = wsdl4jDef.getPrefix(qName.getNamespaceURI());

                XmlSchemaCollection schemaCol = new XmlSchemaCollection();
                XmlSchema schema = schemaCol.read(domElement);
                schemas.add(schema);

            } else {
                // TODO: Throw Exception
                System.out.println("not schema elements in Types Element of wsdl");
            }

        }

        return schemas;
    }
    
    
}

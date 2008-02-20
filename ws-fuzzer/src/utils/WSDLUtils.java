/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import com.ibm.wsdl.extensions.schema.SchemaImpl;
import com.ibm.wsdl.xml.WSDLReaderImpl;
import com.ibm.wsdl.xml.WSDLWriterImpl;
import datamodel.WSFDataElement;
import datamodel.WSFOperation;
import datamodel.WSFPort;
import datamodel.WSFService;
import exceptions.UnSupportedException;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPBinding;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPHeader;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.wsdl.extensions.soap12.SOAP12Body;
import javax.wsdl.extensions.soap12.SOAP12Header;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
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
    private XSDUtils xsdHelper;
    
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
    
    public ArrayList<WSFService> getServices() throws UnSupportedException{
        ArrayList<WSFService> services = new ArrayList<WSFService>();
        
        for (Entry serviceEntry : (Set<Entry>) wsdl4jDef.getServices().entrySet()) {

            Service service = (Service) serviceEntry.getValue();
            
            WSFService wsfService = new WSFService();
            
            wsfService.setName(service.getQName());
            if (service.getDocumentationElement() != null) {
                wsfService.setDocument(service.getDocumentationElement().getTextContent());
            }
            
//            System.out.println(service.getKey() + "[::::::::::]\n" + service.getValue());
            for (Entry portEntry : (Set<Entry>) service.getPorts().entrySet()) {

                Port port = (Port) portEntry.getValue();
                
                WSFPort wsfPort = new WSFPort(wsfService);
                wsfPort.setName(port.getName());
                
                // set service:port
                ExtensibilityElement element = (ExtensibilityElement) port.getExtensibilityElements().get(0);
                if (element instanceof SOAPAddress) {
                    wsfPort.setPortAddressType("SOAPAddress");
                    wsfPort.setPortLocation(((SOAPAddress) element).getLocationURI());
                } else if (element instanceof HTTPAddress) {
                    wsfPort.setPortAddressType("HTTPAddress");
                    wsfPort.setPortLocation(((HTTPAddress) element).getLocationURI());
                    wsfPort.setSupported(false);
                    wsfPort.setCause("Not a SOAPBinding");
                } else {
                    wsfPort.setSupported(false);
                    wsfPort.setCause("Not a SOAPBinding");
                }
                
                // set wsdl:binding
                Binding binding = port.getBinding();
                element = (ExtensibilityElement) binding.getExtensibilityElements().get(0);
                if (element instanceof SOAPBinding) {
                    wsfPort.setBindingType("SOAPBinding");
                    wsfPort.setBindingSOAPTransport(((SOAPBinding) element).getTransportURI());
                    wsfPort.setBindingSOAPStyle(((SOAPBinding) element).getStyle());
                    wsfPort.setSupported(true);
                } else if(element instanceof SOAP12Binding){
                    wsfPort.setBindingType("SOAP12Binding");
                    wsfPort.setBindingSOAPTransport(((SOAP12Binding) element).getTransportURI());
                    wsfPort.setBindingSOAPStyle(((SOAP12Binding) element).getStyle());
                    wsfPort.setSupported(true);
                } else if (element instanceof HTTPBinding) {
                    wsfPort.setBindingType("HTTPBinding");
                    wsfPort.setBindingHTTPVerb(((HTTPBinding) element).getVerb());
                    wsfPort.setSupported(false);
                    wsfPort.setCause("Not a SOAPBinding");
                } else {
                    wsfPort.setSupported(false);
                    wsfPort.setCause("Not a SOAPBinding");
                }
                
                for (BindingOperation bindingOperation : (List<BindingOperation>) port.getBinding().getBindingOperations()) {
                    
                    Operation operation = bindingOperation.getOperation();

                    WSFOperation op = new WSFOperation(wsfPort);
                    
                    element = (ExtensibilityElement) bindingOperation.getExtensibilityElements().get(0);
                    if (element instanceof SOAPOperation) {
                        op.setBindingOperationType("SOAPOperation");
                        op.setBindingSoapAction(((SOAPOperation) element).getSoapActionURI());
                        
                        op.setBindingSOAPStyle(((SOAPOperation) element).getStyle());
                        
                        List<ExtensibilityElement> extensibilityElements = bindingOperation.getBindingInput().getExtensibilityElements();
                        for(ExtensibilityElement e : extensibilityElements){
                            if(e instanceof SOAPBody){
                                op.setRequestDataMessageUse(((SOAPBody)e).getUse());
                            }
                            if(e instanceof SOAPHeader){
                                op.setRequestHeaderMessageQName(((SOAPHeader)e).getMessage());
                                op.setRequestHeaderMessagePart(((SOAPHeader)e).getPart());
                                op.setRequestHeaderMessageUse(((SOAPHeader)e).getUse());
                                op.setRequestHeaderData(this.getDataElement(((SOAPHeader)e).getMessage(), ((SOAPHeader)e).getPart()));    
                            }
                        }
                        
                        extensibilityElements = bindingOperation.getBindingOutput().getExtensibilityElements();
                        for(ExtensibilityElement e : extensibilityElements){
                            if(e instanceof SOAPBody){
                                op.setBindingResponseSOAPUse(((SOAPBody)e).getUse());
                            }
                            if(e instanceof SOAPHeader){
                                op.setResponseHeaderMessageQName(((SOAPHeader)e).getMessage());
                                op.setResponseHeaderMessagePart(((SOAPHeader)e).getPart());
                                op.setResponseHeaderMessageUse(((SOAPHeader)e).getUse());
                                op.setResponseHeaderData(this.getDataElement(((SOAPHeader)e).getMessage(), ((SOAPHeader)e).getPart()));
                            }
                        }
                        
                        op.setSupported(true);
                    } else if(element instanceof SOAP12Operation){
                        op.setBindingOperationType("SOAPOperation");
                        op.setBindingSoapAction(((SOAP12Operation) element).getSoapActionURI());
                        
                        op.setBindingSOAPStyle(((SOAP12Operation) element).getStyle());
                        
                        List<ExtensibilityElement> extensibilityElements = bindingOperation.getBindingInput().getExtensibilityElements();
                        for(ExtensibilityElement e : extensibilityElements){
                            if(e instanceof SOAP12Body){
                                op.setRequestDataMessageUse(((SOAP12Body)e).getUse());
                            }
                            if(e instanceof SOAP12Header){
                                op.setRequestHeaderMessageQName(((SOAP12Header)e).getMessage());
                                op.setRequestHeaderMessagePart(((SOAP12Header)e).getPart());
                                op.setRequestHeaderMessageUse(((SOAP12Header)e).getUse());
                                op.setRequestHeaderData(this.getDataElement(((SOAP12Header)e).getMessage(), ((SOAP12Header)e).getPart()));    
                            }
                        }
                        
                        extensibilityElements = bindingOperation.getBindingOutput().getExtensibilityElements();
                        for(ExtensibilityElement e : extensibilityElements){
                            if(e instanceof SOAP12Body){
                                op.setBindingResponseSOAPUse(((SOAP12Body)e).getUse());
                            }
                            if(e instanceof SOAP12Header){
                                op.setResponseHeaderMessageQName(((SOAP12Header)e).getMessage());
                                op.setResponseHeaderMessagePart(((SOAP12Header)e).getPart());
                                op.setResponseHeaderMessageUse(((SOAP12Header)e).getUse());
                                op.setResponseHeaderData(this.getDataElement(((SOAP12Header)e).getMessage(), ((SOAP12Header)e).getPart()));
                            }
                        }
                        
                        op.setSupported(true);
                    }else if (element instanceof HTTPOperation) {
                        op.setBindingOperationType("HTTPOperation");
                        op.setBindingHttpLocation(((HTTPOperation) element).getLocationURI());
                        op.setSupported(false);
                        op.setCause("Not a SOAPBinding");
                    } else {
                        op.setSupported(false);
                        op.setCause("Not a SOAPBinding");
                    }

                    // wsdl:Operaion
                    op.setName(new QName(wsdl4jDef.getTargetNamespace(),operation.getName()));
                    if (operation.getDocumentationElement() != null) {
                        op.setDocument(operation.getDocumentationElement().getTextContent());
                    }
                    op.setMEP(operation.getStyle().toString());
                    
                    // Out Message
                    if (operation.getOutput().getMessage() != null) {
                        QName messageQName = operation.getOutput().getMessage().getQName();
                        op.setResponseMessageQName(messageQName);
                        op.setResponseData(this.getDataElement(messageQName, null));
                    }
                    
                    // In Message
                    if (operation.getInput().getMessage() != null) {
                        QName messageQName = operation.getInput().getMessage().getQName();
                        op.setRequestMessageQName(messageQName);
                        op.setRequestData(this.getDataElement(messageQName, null));
                    }
                    
                    if(op.getBindingSOAPStyle()!=null && op.getBindingSOAPStyle().equalsIgnoreCase("rpc")){
                        if(op.getRequestData()!=null)
                            op.getRequestData().setName(op.getName());
                        if(op.getRequestHeaderData()!=null)
                            op.getRequestHeaderData().setName(op.getName());
                        
                        if(op.getRequestDataMessageUse().equalsIgnoreCase("encoded")){
                            op.setSupported(false);
                            op.setCause("Unsupported SOAP Format \"rpc/encoded\"");
                        }
                    }
                    
                    wsfPort.addOperation(op);
                }
                wsfService.addPort(wsfPort);
            }
            services.add(wsfService);
        }
        
        return services;
    }

    public void setXSDHelper(XSDUtils xsdHelper) {
        this.xsdHelper = xsdHelper;
    }
    
    private WSFDataElement getDataElement(QName messageQName, String partName) throws UnSupportedException{
        
        Message message = wsdl4jDef.getMessage(messageQName);
        
        if(partName != null){
            Part part = message.getPart(partName);
            return getDataElement(part);
        }
        
        List<Part> parts = message.getOrderedParts(null);
        
        if(parts.size() == 1){
            return getDataElement(parts.get(0));
        }
        
        WSFDataElement dataElement = new WSFDataElement();
        dataElement.setName(messageQName);
        dataElement.setSimpleType(false);
        dataElement.setMinOccurs(1);
        dataElement.setMaxOccurs(1);
        
        for(Part part : parts){
            dataElement.addDataElement(getDataElement(part));
        }
        
        return dataElement;
    }
    
    private WSFDataElement getDataElement(Part part) throws UnSupportedException{
        WSFDataElement dataElement = null;
        if(part.getElementName() != null){
            
            dataElement = this.xsdHelper.createDataElement(part.getElementName());
            return dataElement;
        }
        
        if(part.getTypeName() != null){
            
            dataElement = this.xsdHelper.createDataElement(part.getTypeName());
            
            if(dataElement != null){
                dataElement.setName(new QName("",part.getName()));
                return dataElement;
            }
            
            dataElement = new WSFDataElement();
            
            dataElement.setName(new QName("",part.getName()));
            
            dataElement.setMinOccurs(1);
            dataElement.setMaxOccurs(1);
            
            dataElement.setSimpleType(true);
            dataElement.setType(part.getTypeName());
            
            return dataElement;
        }
        
        
        return null;
    }
    
}

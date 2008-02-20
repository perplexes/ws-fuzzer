/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import exceptions.UnSupportedException;
import datamodel.WSFDataElement;
import datamodel.WSFDataAttribute;
import java.util.ArrayList;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Logger;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAnnotated;
import org.apache.ws.commons.schema.XmlSchemaAttribute;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaComplexContent;
import org.apache.ws.commons.schema.XmlSchemaComplexContentExtension;
import org.apache.ws.commons.schema.XmlSchemaComplexContentRestriction;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContent;
import org.apache.ws.commons.schema.XmlSchemaContentModel;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaGroupBase;
import org.apache.ws.commons.schema.XmlSchemaGroupRef;
import org.apache.ws.commons.schema.XmlSchemaObjectCollection;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSimpleContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.XmlSchemaType;

/**
 *
 * @author chang
 */
public class XSDUtils {
    
    private static Logger logger = Logger.getLogger(XSDUtils.class);
    private ArrayList<XmlSchema> xmlSchemas;
    
    public XSDUtils(ArrayList<XmlSchema> xmlSchemas){
        this.xmlSchemas = xmlSchemas;
    }

    public XmlSchemaElement getXmlSchemaElement(QName qName) {

        XmlSchemaElement schemaElement = null;
        
        for (XmlSchema xmlSchema : xmlSchemas) {
            schemaElement = xmlSchema.getElementByName(qName);
            if (schemaElement != null) {
                break;
            }
        }
        
        return schemaElement;
    }
    
    public XmlSchemaType getXmlSchemaType(QName qName){
        XmlSchemaType schemaType = null;
        
        for (XmlSchema xmlSchema : xmlSchemas) {
            schemaType = xmlSchema.getTypeByName(qName);
            if (schemaType != null) {
                break;
            }
        }
        
        return schemaType;
    }
    
    public WSFDataElement createDataElement(QName msgQName) throws UnSupportedException{
        XmlSchemaElement xmlSchemaElement = this.getXmlSchemaElement(msgQName);
        
        if(xmlSchemaElement != null){
            return this.createDataElement(xmlSchemaElement);
        }
        
        XmlSchemaType schemaType = this.getXmlSchemaType(msgQName);
        if(schemaType != null){
            return this.createDataElement(schemaType);
        }
        
        return null;
    }

    public WSFDataElement createDataElement(XmlSchemaAnnotated xmlSchemaAnnotated) throws UnSupportedException {

        XmlSchemaElement schemaElement = null;
        XmlSchemaType schemaType = null;
        
        if(xmlSchemaAnnotated instanceof XmlSchemaElement){
            schemaElement = (XmlSchemaElement)xmlSchemaAnnotated;
        }
        
        if(xmlSchemaAnnotated instanceof XmlSchemaType){
            schemaType = (XmlSchemaType)xmlSchemaAnnotated;
        }
        
        XmlSchemaType elementSchemaType = schemaElement != null ? schemaElement.getSchemaType() : schemaType;

        if (elementSchemaType instanceof XmlSchemaSimpleType) {
            return createSimpleTypeDataElement(schemaElement);
        }

        if (elementSchemaType instanceof XmlSchemaComplexType) {
            
            XmlSchemaComplexType complexSchemaType = (XmlSchemaComplexType) elementSchemaType;

            WSFDataElement dataElement = new WSFDataElement();
            dataElement.setSimpleType(false);
            
            if (complexSchemaType.isMixed()) {
                //TODO: throw exception
//                throw new UnSupportedException("UnSupported XmlSchemaType: ComplexType(mixed)");
                dataElement.setSimpleType(true);
                dataElement.setType(new QName("mixed"));
            }
            
            
            if(schemaElement != null){
                dataElement.setName(schemaElement.getQName());
                dataElement.setMinOccurs(schemaElement.getMinOccurs());
                dataElement.setMaxOccurs(schemaElement.getMaxOccurs());
            }else{
                dataElement.setName(elementSchemaType.getQName());
                dataElement.setMinOccurs(1);
                dataElement.setMaxOccurs(1);
            }
            
            if(dataElement.getType()!=null && dataElement.isSimpleType()) return dataElement;
            
            ArrayList<XmlSchemaAttribute> schemaAttributes = getXmlSchemaAttributes(complexSchemaType);
            for(XmlSchemaAttribute xmlSchemaAttribute : schemaAttributes){
                dataElement.addDataAttribute(createDataAttribute(xmlSchemaAttribute));
            }
            
            ArrayList<XmlSchemaElement> schemaElements = getChildrenXmlSchemaElements(complexSchemaType);
            for(XmlSchemaElement xmlSchemaElement : schemaElements){
                
                WSFDataElement element = createDataElement(xmlSchemaElement);
                
                if(element != null){
                    dataElement.addDataElement(element);
                    continue;
                }
                
                element = createDataElement(getXmlSchemaType(xmlSchemaElement.getSchemaTypeName()));
                
                if(element != null){
                    element.setName(xmlSchemaElement.getQName());
                    dataElement.addDataElement(element);
                    continue;
                }
                
                if(element == null){
                    logger.warn("not found: " + xmlSchemaElement.getSchemaTypeName());
                }
            }
            
            return dataElement;
        }
        
            
        return null;
    }

    private ArrayList<XmlSchemaAttribute> getXmlSchemaAttributes(XmlSchemaComplexType complexSchemaType) throws UnSupportedException {
        
        return new ArrayList<XmlSchemaAttribute>();
    }

    private ArrayList<XmlSchemaElement> getChildrenXmlSchemaElements(XmlSchemaComplexType complexSchemaType) throws UnSupportedException {

        XmlSchemaParticle particle = null;
        XmlSchemaGroupBase groupBase = null;
        ArrayList<XmlSchemaElement> xmlSchemaElements = new ArrayList<XmlSchemaElement>();
        
        XmlSchemaContentModel contentModel = complexSchemaType.getContentModel();
        if(contentModel != null){
            if(contentModel instanceof XmlSchemaComplexContent){
            
                XmlSchemaContent content = contentModel.getContent();
                if(content instanceof XmlSchemaComplexContentExtension){

                    XmlSchemaComplexContentExtension complexContentExtension = (XmlSchemaComplexContentExtension)content;
                    
                    XmlSchemaType schemaType = this.getXmlSchemaType(complexContentExtension.getBaseTypeName());
                    
                    if(schemaType instanceof XmlSchemaComplexType){
                        ArrayList<XmlSchemaElement> elements = this.getChildrenXmlSchemaElements((XmlSchemaComplexType)schemaType);
                        
                        for(XmlSchemaElement element : elements){
                            xmlSchemaElements.add(element);
                        }
                        
                    }
                    
                    particle = complexContentExtension.getParticle();

                }else if(content instanceof XmlSchemaComplexContentRestriction){
                    
                    XmlSchemaComplexContentRestriction complexContentRestriction = (XmlSchemaComplexContentRestriction)content;
                    
//                    System.out.println(complexContentRestriction.getBaseTypeName());
                    
                    particle = complexContentRestriction.getParticle();

                }else {
                    throw new UnSupportedException("UnSupported XmlSchemaType: ContentModel" + contentModel);
                }
                
            }else if(contentModel instanceof XmlSchemaSimpleContent){
                throw new UnSupportedException("UnSupported XmlSchemaType: SimpleContent");
            }
            
            
        }else {
            particle = complexSchemaType.getParticle();
        }
        
        
        if (particle instanceof XmlSchemaGroupRef) {
            groupBase = ((XmlSchemaGroupRef) particle).getParticle();
        } else if (particle instanceof XmlSchemaGroupBase) {
            groupBase = (XmlSchemaGroupBase) particle;
        } else {
            return new ArrayList<XmlSchemaElement>();   // empty ComplexType
//            System.out.println(complexSchemaType.);
//            throw new UnSupportedException("UnSupported XmlSchemaType: " + particle);
        }

        if (groupBase instanceof XmlSchemaChoice) {
            throw new UnSupportedException("UnSupported XmlSchemaType: ComplexType(choice)");
        }
        
        XmlSchemaObjectCollection collection = groupBase.getItems();
        
        for (int i = 0; i < collection.getCount(); i++) {
//            System.out.println("getChildrenXmlSchemaElements: "+collection.getItem(i));
            xmlSchemaElements.add((XmlSchemaElement)collection.getItem(i));
        }
        
        return xmlSchemaElements;
    }

    private WSFDataElement createSimpleTypeDataElement(XmlSchemaElement schemaElement) throws UnSupportedException {
        WSFDataElement dataElement = new WSFDataElement();

        dataElement.setSimpleType(true);
        dataElement.setName(schemaElement.getQName());
        dataElement.setMinOccurs(schemaElement.getMinOccurs());
        dataElement.setMaxOccurs(schemaElement.getMaxOccurs());
        
        XmlSchemaSimpleType xmlSchemaSimpleType = (XmlSchemaSimpleType)schemaElement.getSchemaType();
        
        if(xmlSchemaSimpleType.getContent() == null){
            dataElement.setType(xmlSchemaSimpleType.getQName());
        }else{
            XmlSchemaSimpleTypeContent content = xmlSchemaSimpleType.getContent();
            
            if( !( content instanceof XmlSchemaSimpleTypeRestriction) ){
                throw new UnSupportedException("UnSupported XmlSchemaType: SimpleType(List or Union)");
            }
            
            dataElement.setType(((XmlSchemaSimpleTypeRestriction)content).getBaseTypeName());
        }

        return dataElement;
    }

    private WSFDataAttribute createDataAttribute(XmlSchemaAttribute xmlSchemaAttribute) {
        return null;
    }

    public static void main(String[] args) throws WSDLException, UnSupportedException, XMLStreamException, Exception {
        String wsdlURL = null;
        wsdlURL = "http://localhost/~chang/tmp/CurrencyConvertor.asmx%3fwsdl";
//	wsdlURL = "http://localhost/~chang/tmp/xwatchlists.asmx%3fWSDL";
//	wsdlURL = "http://localhost/~chang/tmp/AmortizationCalculator.cfc%3fwsdl";
        wsdlURL = "http://ws.strikeiron.com/WSHRealTimeCompanyEarnings?WSDL";
//        wsdlURL = "http://ws.srlink.com/BusTools/BankValIntl.asmx?WSDL";
        
        WSDLUtils wsdlUtils = new WSDLUtils(wsdlURL);
        XSDUtils xsdUtils = new XSDUtils(wsdlUtils.getXmlSchemaFromTypes());
                
        QName qName = null;
//        qName = new QName("http://www.webserviceX.NET/","ConversionRate");
        qName = new QName("http://www.webserviceX.NET/","ConversionRateResponse");
        qName = new QName("http://www.strikeiron.com","CalendarInformationByTickerResponse");
//        qName = new QName("http://ws.levelsoft.net/","SEKHeader");
        
//        XmlSchemaElement xmlSchemaElement = wsdlUtils.getXmlSchemaElement(qName);
        WSFDataElement dataElement = xsdUtils.createDataElement(qName);
        
        System.out.println("..:: Start ::..\n");
        
        if(dataElement != null){
            
            System.out.println(XMLUtils.toPrettifiedString(dataElement.toOMElement(null, true)));
            
            System.out.println("\n+++++++++++++++++++++\n");
            
            WSFDataElement.print(dataElement);
        }
        
        System.out.println("\n..:: End ::..");
        

    }
}

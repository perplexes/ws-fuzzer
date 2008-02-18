/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import exceptions.UnSupportedException;
import exceptions.WSFProjectNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import utils.WSDLUtils;
import utils.XMLUtils;
import utils.XSDUtils;

/**
 *
 * @author chang
 */
public class WSFProject {
    
    private String name;
    private File path;
    
    private String wsdlURI;
    private String wsdlRaw;
    private Definition wsdl4jDef;
    
    private ArrayList<WSFService> services;
    private ArrayList<WSFTestCase> testCases;
    
    private WSDLUtils wsdlHelper;
    private XSDUtils xsdHelper;
    
    public WSFProject(String name, File path, String wsdlURI) throws WSDLException, UnSupportedException, XMLStreamException, Exception{
        
        this.name = name;
        this.path = path;
        this.wsdlURI = wsdlURI;
        
        wsdlHelper = new WSDLUtils(wsdlURI);
        xsdHelper = new XSDUtils(getWsdlHelper().getXmlSchemaFromTypes());
        wsdlHelper.setXSDHelper(xsdHelper);
        
        this.wsdlRaw = XMLUtils.prettify(wsdlHelper.getWSDLRaw());
        this.wsdl4jDef = wsdlHelper.getWSDL4jDef();
        
        this.services = new ArrayList<WSFService>();
        this.testCases = new ArrayList<WSFTestCase>();
        
        setServices(wsdlHelper.getServices());
    }
    
    public WSFProject(WSFProjectInfo projectInfo, File parentDirectory) throws WSDLException, UnSupportedException, WSFProjectNotFoundException, XMLStreamException, Exception{
        
        this.name = projectInfo.getName();
        this.path = new File(parentDirectory, this.name);
        
        if(!path.exists()){
            throw new WSFProjectNotFoundException("Directory for Project \""+this.name+"\" doesn't exist!");
        }
        
        this.wsdlURI = projectInfo.getWSDLURI();
        
        File wsdlFile = new File(path, name+".wsdl");
        
        wsdlHelper = new WSDLUtils(wsdlFile.getAbsolutePath());
        xsdHelper = new XSDUtils(getWsdlHelper().getXmlSchemaFromTypes());
        wsdlHelper.setXSDHelper(xsdHelper);
        
        this.wsdlRaw = XMLUtils.prettify(wsdlHelper.getWSDLRaw());
        this.wsdl4jDef = wsdlHelper.getWSDL4jDef();
        
        this.services = new ArrayList<WSFService>();
        this.testCases = new ArrayList<WSFTestCase>();
        
        setServices(wsdlHelper.getServices());
        
        loadTestCasesFromFile();
    }
    
    public boolean save() throws IOException, Exception{
        if(!path.exists()){
            if(!path.mkdirs()){
                return false;
            }
        }
        
        File wsdlFile = new File(path, name+".wsdl");
        
        if(!wsdlFile.exists()){
            FileWriter writer = new FileWriter(wsdlFile);
            writer.write(wsdlRaw);
            writer.flush();
            writer.close();
        }
        
        saveTestCasesToFile();
        
        return true;
    }
    
    public static void main(String[] args) throws MalformedURLException, URISyntaxException{
        
        ArrayList<String> wsdlURLs = new ArrayList<String>();
        wsdlURLs.add("http://localhost/~chang/tmp/CurrencyConvertor.asmx%3fwsdl");
        wsdlURLs.add("http://localhost/~chang/tmp/xwatchlists.asmx%3fWSDL");
        wsdlURLs.add("http://localhost/~chang/tmp/AmortizationCalculator.cfc%3fwsdl");
        wsdlURLs.add("http://ws.strikeiron.com/WSHRealTimeCompanyEarnings?WSDL");
        wsdlURLs.add("http://ws.srlink.com/BusTools/BankValIntl.asmx?WSDL");
        
        String uri = wsdlURLs.get(0);
        
        System.out.println(uri.replaceAll("^.*/", "").replaceAll("\\..*$", ""));
        
    }

    public WSDLUtils getWsdlHelper() {
        return wsdlHelper;
    }

    public void setWsdlHelper(WSDLUtils wsdlHelper) {
        this.wsdlHelper = wsdlHelper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public String getWsdlURI() {
        return wsdlURI;
    }

    public void setWsdlURI(String wsdlURI) {
        this.wsdlURI = wsdlURI;
    }

    public String getWsdlRaw() {
        return wsdlRaw;
    }

    public void setWsdlRaw(String wsdlRaw) throws XMLStreamException, Exception {
        this.wsdlRaw = XMLUtils.prettify(wsdlRaw);
    }

    public Definition getWsdl4jDef() {
        return wsdl4jDef;
    }

    public void setWsdl4jDef(Definition wsdl4jDef) {
        this.wsdl4jDef = wsdl4jDef;
    }

    public ArrayList<WSFService> getServices() {
        return services;
    }

    public void setServices(ArrayList<WSFService> services) {
        this.services = services;
        for(WSFService service : services){
            service.setProject(this);
        }
    }

    public WSFService getService(QName name){
        for(WSFService service : services){
            if(service.getName().equals(name))
                return service;
        }
        return null;
    }
    
    public ArrayList<WSFTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(ArrayList<WSFTestCase> testCases) {
        this.testCases = testCases;
        for(WSFTestCase testCase : testCases){
            testCase.setProject(this);
        }
    }

    public XSDUtils getXsdHelper() {
        return xsdHelper;
    }

    public void setXsdHelper(XSDUtils xsdHelper) {
        this.xsdHelper = xsdHelper;
    }
    
    public void addService(WSFService service){
        this.services.add(service);
    }
    
    public void addTestCase(WSFTestCase testCase){
        this.testCases.add(testCase);
    }
    
    public void print(){
        System.out.println("...:: project ::...");
        System.out.println("name:        " + this.name);
        System.out.println("path:        " + this.path);
        System.out.println("services#:   " + services.size());
        System.out.println("(1.S)ports#: " + services.get(0).getPorts().size());
        System.out.println();
    }
    
    public String toString(){
        return this.name;
    }
    
    public OMElement serializeTestCasesToOMElement(OMElement parent){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement testCasesElement = null;
        
        if(parent == null){
            testCasesElement = omDOMFactory.createOMElement(new QName("testcases"));
        }else {
            testCasesElement = omDOMFactory.createOMElement(new QName("testcases"), parent);
        }
        
        for(WSFTestCase testCase : this.testCases){
            testCase.serializeToOMElement(testCasesElement);
        }
        
        return testCasesElement;
    }
    
    public void deserializeTestCasesFromOMElement(OMElement omElement){
        
        Iterator iterator = omElement.getChildElements();
        while(iterator.hasNext()){
            OMElement element = (OMElement)iterator.next();
            testCases.add(WSFTestCase.deserializeFromOMElement(element, this));
        }
    }
    
    public void saveTestCasesToFile() throws Exception{
        File testCasesFile = new File(path, "testcases.xml");
        XMLUtils.saveToFile(serializeTestCasesToOMElement(null), testCasesFile);
    }
    
    public void loadTestCasesFromFile() throws FileNotFoundException, XMLStreamException {
        File testCasesFile = new File(path, "testcases.xml");
        deserializeTestCasesFromOMElement(XMLUtils.toOMElement(testCasesFile));
    }
    
}

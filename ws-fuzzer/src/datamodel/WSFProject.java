/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import utils.WSDLUtils;
import utils.XSDUtils;

/**
 *
 * @author chang
 */
public class WSFProject {
    
    private String name;
    
    private String wsdlURI;
    private String wsdlRaw;
    private Definition wsdl4jDef;
    
    private ArrayList<WSFService> services;
    private ArrayList<WSFTestCase> testCases;
    
    private WSDLUtils wsdlHelper;
    private XSDUtils xsdHelper;
    
    public WSFProject(String wsdlURI) throws WSDLException{
        this.wsdlURI = wsdlURI;
        
        wsdlHelper = new WSDLUtils(wsdlURI);
        xsdHelper = new XSDUtils(wsdlHelper.getXmlSchemaFromTypes());
        
        this.wsdlRaw = wsdlHelper.getWSDLRaw();
        this.wsdl4jDef = wsdlHelper.getWSDL4jDef();
        
        this.services = new ArrayList<WSFService>();
        this.testCases = new ArrayList<WSFTestCase>();
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
    
}

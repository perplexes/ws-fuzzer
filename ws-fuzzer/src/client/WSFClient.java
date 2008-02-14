/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import datamodel.WSFConfiguration;
import datamodel.WSFDataElement;
import datamodel.WSFDictionary;
import datamodel.WSFDictionaryInfo;
import datamodel.WSFInputSource;
import datamodel.WSFOperation;
import datamodel.WSFPort;
import datamodel.WSFProject;
import datamodel.WSFProjectInfo;
import datamodel.WSFResult;
import datamodel.WSFTestCase;
import exceptions.UnSupportedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.wsdl.WSDLException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 *
 * @author chang
 */
public class WSFClient {
 
    
    private final Hook hook = new Hook();
    
    private WSFTestCase testcase;
    private ServiceClient serviceClient;
    
    
    public WSFClient(WSFTestCase testCase, int maxNOCPerHost, int maxNOCOverall) throws AxisFault{
        
        this.testcase = testCase;
        
        ConfigurationContext configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(System.getProperty("axis2.repo"), System.getProperty("axis2.xml"));
        configurationContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, "true");

        MultiThreadedHttpConnectionManager conmgr = new MultiThreadedHttpConnectionManager() {

            @Override
            public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
                HttpConnection connection = super.getConnectionWithTimeout(hostConfiguration, timeout);
                hook.setHttpConnection(connection);
                hook.setResult(new WSFResult());
                return new HttpConnectionAdapter(connection, hook);
            }
        };

        conmgr.getParams().setDefaultMaxConnectionsPerHost(maxNOCPerHost);
        conmgr.getParams().setMaxTotalConnections(maxNOCOverall);
        
        HttpClient client = new HttpClient(conmgr);
        configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, client);
        configurationContext.setProperty(HTTPConstants.CHUNKED, false);
        
        serviceClient = new ServiceClient(configurationContext, testcase.getProject().getWsdl4jDef(), testcase.getOperation().getPort().getService().getName(), testcase.getOperation().getPort().getName());
	if(testcase.getOperation().getBindingSoapAction()!=null)
            serviceClient.getOptions().setAction(testcase.getOperation().getBindingSoapAction());
        
        serviceClient.getOptions().setUseSeparateListener(false);
    }
    
    public WSFTestCase getTestcase() {
        return testcase;
    }

    public void setTestcase(WSFTestCase testcase) {
        this.testcase = testcase;
    }
    
   public ArrayList<WSFResult> doJob() throws AxisFault, XMLStreamException{
        

            ArrayList<WSFResult> results = new ArrayList<WSFResult>();
            WSFOperation operation = testcase.getOperation();
            ArrayList<OMElement> payloads = testcase.getInputDataVector();
            
            for (int i = 0; i < payloads.size(); i++) {
                serviceClient.sendReceiveNonBlocking(operation.getName(), payloads.get(i), new AxisCallbackImpl(hook, results, i, Thread.currentThread()));
            }
            
            while(true){
                
                try {
                
                    Thread.sleep(1000);
                
                } catch (InterruptedException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    
                    synchronized(results){
                        if(results.size()==payloads.size())
                            break;
                    }
                }
            }
            
            serviceClient.cleanupTransport();
            serviceClient.cleanup();
            return results;
       
    }
    
    public static void main(String[] args) throws WSDLException, UnSupportedException, FileNotFoundException, IOException, AxisFault, XMLStreamException{
        
        WSFConfiguration config1 = new WSFConfiguration(null);
        config1.setMaxNumberOfConnectionsPerHost(5);
        config1.setMaxNumberOfConnectionsOverall(10);
        config1.addDictionary(new WSFDictionaryInfo("Currency_1","test/dict1.txt"));
        config1.addDictionary(new WSFDictionaryInfo("Currency_2","test/dict2.txt"));
        config1.setProjectsDirectory(new File("projects"));
        
        String wsdlURI = null;
        wsdlURI = "http://localhost/~chang/tmp/CurrencyConvertor.asmx%3fwsdl";
//	wsdlURI = "http://localhost/~chang/tmp/xwatchlists.asmx%3fWSDL";
//	wsdlURI = "http://localhost/~chang/tmp/AmortizationCalculator.cfc%3fwsdl";
        
        WSFProjectInfo projectInfo = new WSFProjectInfo("testProject", wsdlURI);
        config1.addProject(projectInfo);
        
        WSFProject project = new WSFProject(projectInfo.getName(), config1.getProjectsDirectory(), projectInfo.getWSDLURI());
        
        project.print();
        
        WSFPort port = project.getServices().get(0).getPorts().get(1);
        port.print();
        
        WSFOperation operation = port.getOperations().get(0);
        operation.print();
        
        WSFDictionary dictionary1 = new WSFDictionary("Currency_1", "test/dict1.txt");
        WSFDictionary dictionary2 = new WSFDictionary("Currency_2", "test/dict2.txt");
        
        
        WSFInputSource source1 = WSFInputSource.createInputSourceFromDictionary(dictionary1);
        WSFInputSource source2 = WSFInputSource.createInputSourceFromDictionary(dictionary2);
        
        WSFInputSource source3 = WSFInputSource.createSourceFromDefaultValue("EUR");
        WSFInputSource source4 = WSFInputSource.createSourceFromDefaultValue("USD");
        
        WSFDataElement data = operation.getRequestData();
        data.getDataElements().get(0).setSource(source3);
        data.getDataElements().get(1).setSource(source4);
        
        WSFTestCase testCase = new WSFTestCase("test", operation);
        testCase.generateInputsVector();
        
        System.out.println(testCase.getInputDataVector().size());
        
        WSFClient client = new WSFClient(testCase, config1.getMaxNumberOfConnectionsPerHost(), config1.getMaxNumberOfConnectionsOverall());
        ArrayList<WSFResult> results = client.doJob();
        
        for(WSFResult result : results){
            System.out.println();
            System.out.println("Time: " + result.getTime());
            System.out.println();
            System.out.println("In:   \n" + result.getInRaw());
            System.out.println("\n");
            System.out.println("Out:  \n" + result.getOutRaw());
            System.out.println();
        }
    }
}

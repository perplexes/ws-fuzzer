/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import datamodel.WSFConfiguration;
import datamodel.WSFDictionaryInfo;
import datamodel.WSFOperation;
import datamodel.WSFProject;
import datamodel.WSFProjectInfo;
import datamodel.WSFResult;
import datamodel.WSFTestCase;
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
            ArrayList<OMElement> payloads = testcase.getInputsVector();
            
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
    
    public static void main(String[] args) throws WSDLException{
        
        WSFConfiguration config1 = new WSFConfiguration();
        config1.setMaxNumberOfConnectionsPerHost(5);
        config1.setMaxNumberOfConnectionsOverall(10);
        config1.addDictionary(new WSFDictionaryInfo("Currency_1","test/dict1.txt"));
        config1.addDictionary(new WSFDictionaryInfo("Currency_2","test/dict2.txt"));
        config1.setProjectsDirectory("projects");
        
        String wsdlURI = null;
        wsdlURI = "http://localhost/~chang/tmp/CurrencyConvertor.asmx%3fwsdl";
//	wsdlURI = "http://localhost/~chang/tmp/xwatchlists.asmx%3fWSDL";
//	wsdlURI = "http://localhost/~chang/tmp/AmortizationCalculator.cfc%3fwsdl";
        
        WSFProjectInfo projectInfo = new WSFProjectInfo("testProject", wsdlURI);
        config1.addProject(projectInfo);
        
        WSFProject project = new WSFProject(projectInfo.getName(), config1.getProjectsDirectory(), projectInfo.getWSDLURI());
        
    }
}

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
import gui.testcase.ExecuteTestCase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.jdesktop.application.Task;

/**
 *
 * @author chang
 */
public class WSFClient {
 
    
    private final Hook hook = new Hook();
    private ExecuteTestCase executeTestCase;
    private WSFTestCase testcase;
    private ServiceClient serviceClient;
    private ArrayList<WSFResult> results;
    
    
    public WSFClient(ExecuteTestCase executeTestCase, int maxNOCPerHost, int maxNOCOverall) throws AxisFault{
        
        this.executeTestCase = executeTestCase;
        this.testcase = executeTestCase.getTestCase();
        this.results = new ArrayList<WSFResult>();
        
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
    
   public void doJob() throws AxisFault, XMLStreamException{
        

            WSFOperation operation = testcase.getOperation();
            ArrayList<WSFDataElement> header = testcase.getInputHeaderVector();
            ArrayList<WSFDataElement> payloads = testcase.getInputDataVector();
            
            int totalNumberOfRequest = payloads.size();
            int requestCounter = 0;
            int resultCounter = 0;
            
            while(true){
                
                if(Thread.interrupted()){
                    
                    if(this.executeTestCase.isCancelled()){
                        
                        System.out.println("WSFClient: Task Canceling detected! <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                        break;
                    }
                    
                    synchronized(results){

                        int n = results.size();

                        if(n>0){
                            for(int i=0; i< n; i++){
                                WSFResult result = results.remove(0);
                                executeTestCase.pulishResult(result);
                                resultCounter++;
                                System.out.println("WSFClient: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName() + " || id of result: "+ result.getInputIndex());
                            }
                        }
                    }
                }
                
                if( requestCounter < totalNumberOfRequest ){
                    if(requestCounter < header.size())
                        serviceClient.addHeader(header.get(requestCounter).toOMElement(null, false));
                    serviceClient.sendReceiveNonBlocking(operation.getName(), payloads.get(requestCounter).toOMElement(null, false), new AxisCallbackImpl(hook, results, requestCounter, Thread.currentThread()));
                    requestCounter++;
                    continue;
                }
                
                if( requestCounter == totalNumberOfRequest && resultCounter != requestCounter ){
                    
                    try {
                        
                        Thread.sleep(1000);
                        
                    } catch (InterruptedException ex) {
                        
                    } finally{
                        
                        Thread.currentThread().interrupt();
                        continue;
                    }
                    
                }
                
                if( requestCounter == totalNumberOfRequest && resultCounter == requestCounter ){
                    break;
                }
            }
            
            serviceClient.cleanupTransport();
            serviceClient.cleanup();
    }
    
    public static void main(String[] args) throws WSDLException, UnSupportedException, FileNotFoundException, IOException, AxisFault, XMLStreamException, Exception{
        
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
        
        // 
//        WSFClient client = new WSFClient(testCase, config1.getMaxNumberOfConnectionsPerHost(), config1.getMaxNumberOfConnectionsOverall());
//        ArrayList<WSFResult> results = client.doJob();
//        
//        for(WSFResult result : results){
//            System.out.println();
//            System.out.println("Time: " + result.getTime());
//            System.out.println();
//            System.out.println("In:   \n" + result.getInRaw());
//            System.out.println("\n");
//            System.out.println("Out:  \n" + result.getOutRaw());
//            System.out.println();
//        }
    }
}

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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import javax.wsdl.WSDLException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.log4j.Logger;

/**
 *
 * @author chang
 */
public class WSFClient {
 
    private static Logger logger = Logger.getLogger(WSFClient.class);
    
    private final static Hook hook = new Hook();
//    private static MultiThreadedHttpConnectionManager conmgr = new MultiThreadedHttpConnectionManager() {
//
//            @Override
//            public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
//                HttpConnection connection = super.getConnectionWithTimeout(hostConfiguration, timeout);
////                hook.setHttpConnection(connection);
////                hook.setResult(new WSFResult());
//                return new HttpConnectionAdapter(connection, hook);
//            }
//        };
//    private static HttpClient httpClient = new HttpClient(conmgr);
    
//    private ConfigurationContext configurationContext; 
    
    private ExecuteTestCase executeTestCase;
    private WSFTestCase testcase;
    private ArrayList<WSFTask> resultTasks;
    
//    private ServiceClient serviceClient;
    
    private int maxNOCPerHost;
    private static int maxNOCOverall;
    
    public WSFClient(ExecuteTestCase executeTestCase, int maxNOCPerHost, int maxNOCOverall) throws AxisFault{
        
        this.maxNOCPerHost = maxNOCPerHost;
        WSFClient.maxNOCOverall = maxNOCOverall;
        
        this.executeTestCase = executeTestCase;
        this.testcase = executeTestCase.getTestCase();
        this.resultTasks = new ArrayList<WSFTask>();
        
//        conmgr.getParams().setDefaultMaxConnectionsPerHost(maxNOCPerHost);
//        conmgr.getParams().setMaxTotalConnections(maxNOCOverall);
        
    }
    
    public ServiceClient createServiceClient() throws AxisFault{
        
        MultiThreadedHttpConnectionManager conmgr = new MultiThreadedHttpConnectionManager() {

            @Override
            public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException {
                HttpConnection connection = super.getConnectionWithTimeout(hostConfiguration, timeout);
//                hook.setHttpConnection(connection);
//                hook.setResult(new WSFResult());
                return new HttpConnectionAdapter(connection, hook);
            }
        };
        conmgr.getParams().setDefaultMaxConnectionsPerHost(1);
        conmgr.getParams().setMaxTotalConnections(1);
        HttpClient httpClient = new HttpClient(conmgr);
        
        ConfigurationContext configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(System.getProperty("axis2.repo"), System.getProperty("axis2.xml"));
        configurationContext.setProperty(HTTPConstants.REUSE_HTTP_CLIENT, "true");
        configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient);
        configurationContext.setProperty(HTTPConstants.CHUNKED, false);
        
        ServiceClient serviceClient = new ServiceClient(configurationContext, testcase.getProject().getWsdl4jDef(), testcase.getOperation().getPort().getService().getName(), testcase.getOperation().getPort().getName());
	if(testcase.getOperation().getBindingSoapAction()!=null)
            serviceClient.getOptions().setAction(testcase.getOperation().getBindingSoapAction());
        
        serviceClient.getOptions().setUseSeparateListener(false);
        
        return serviceClient;
    }
    
    class WSFTask implements Callable<WSFResult>{
        
        private int taskIndex;
        private WSFDataElement header;
        private WSFDataElement body;
        private Thread thread;
        
        private WSFResult result;
        private ServiceClient serviceClient;
        
        private Exception exception;
        
        public WSFTask(int taskIndex, WSFDataElement header, WSFDataElement body, Thread thread){
            this.taskIndex = taskIndex;
            this.header = header;
            this.body = body;
            this.thread = thread;
            this.result = new WSFResult(taskIndex);
        }
        
        
        public WSFResult call() throws Exception {
            
            try{
                
                hook.setResult(result);
                
                serviceClient = hook.getClient();
                if(serviceClient==null){
                   serviceClient = createServiceClient();
                    hook.setClient(serviceClient);
                }

                if(header != null)
                    serviceClient.addHeader(header.toOMElement(null, false));

                logger.info("Send Request: index " + taskIndex);
                result.setTime(System.currentTimeMillis());
                serviceClient.sendReceive(testcase.getOperation().getName(), body.toOMElement(null, false));
            
            
            }catch(Exception ex){
                
                exception = ex;
                logger.warn(ex.getCause().getClass() + " - " + ex.getMessage() + " - index: " + taskIndex);
                
            }finally{
                
                result.setTime(System.currentTimeMillis());
                
                try{
                    
                    serviceClient.cleanupTransport();
                    serviceClient.cleanup();
                    
//                    result = hook.getResult();
                    
                    result.prettify();
                    
                    if(result.getInRaw().equals("")){
                        
                        result.setStatus(WSFResult.NOTFINISHED);
                        
                        if(exception != null){
                            result.setInRaw(exception.getMessage());

                            if(result.getOutRaw().equals(""))
                                result.setOutRaw(exception.getMessage());
                        }
                        
                        logger.info("Request Failed: index " + taskIndex);
                    }else{
                        
                        result.setStatus(WSFResult.FINISHED);
                        logger.info("Receive Response: index " + taskIndex);
                    }
                    
                    synchronized(resultTasks){
                        resultTasks.add(this);
                    }
                    
                    
                }catch(Exception ex){
                    
                }finally{
                    
                    thread.interrupt();
                    serviceClient.removeHeaders();
                }
                
                return result;
            }
        }
        
        public WSFResult getWSFResult(){
            return this.result;
        }
        
        public int getTaskIndex(){
            return this.taskIndex;
        }
        
    } 
    
    // index == -1, means to execute all
    public void doJob(int index) {
        
        ExecutorService executor = Executors.newFixedThreadPool(maxNOCPerHost);

        int sum = 0;
        int finishedCounter = 0;

        if(index == -1){
            
            ArrayList<WSFResult> results = testcase.getResults();
            for(int i=0; i<results.size(); i++){

                if(results.get(i).getStatus() == WSFResult.FINISHED)
                    continue;

                FutureTask task = getWSFTask(i);
                if(task != null){
                    executor.submit(task);
                    sum++;
                }
            }
            
        }else if(index > -1){
            
            executor.submit(getWSFTask(index));
            sum = 1;
            
        }else{
            
            return;
        }
        
        executeTestCase.setProgress(finishedCounter, sum);


        while(true){

            if(Thread.interrupted()){

                if(executeTestCase.isCancelled()){
                    logger.info("TestCase Execution Canceled");
                    break;
                }
                int counter = 0;
                WSFTask[] taskBuffer = null;

                // take finished tasks in "taskBuffer"
                synchronized(resultTasks){

                    counter = resultTasks.size();

                    if(counter > 0){
                        taskBuffer = new WSFTask[counter];
                        resultTasks.toArray(taskBuffer);
                        resultTasks.clear();
                    }
                }

                for(int i=0; i< counter; i++){
                    WSFTask t = taskBuffer[i];
                    WSFResult r = t.getWSFResult();

                    if(r.getStatus() != WSFResult.FINISHED){
                        logger.info("Request Retry: index " + t.getTaskIndex());
                        executor.submit(getWSFTask(t.getTaskIndex()));
                    }else{
                        finishedCounter++;
                    }
                    
                    if(r != null){
                        executeTestCase.pulishResult(r);
                        executeTestCase.setProgress(finishedCounter, sum);
                    }
                }

                if(finishedCounter == sum)
                    break;

            }else{

                try{
                    Thread.sleep(500);
                }catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }

            }
        }

        executor.shutdownNow();
    }
    
//    private SOAPFactory getSOAPFactory(ServiceClient serviceClient) {
//        String soapVersionURI = serviceClient.getOptions().getSoapVersionURI();
//        if (SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI.equals(soapVersionURI)) {
//            return OMAbstractFactory.getSOAP12Factory();
//        } else {
//            // make the SOAP 1.1 the default SOAP version
//            return OMAbstractFactory.getSOAP11Factory();
//        }
//    }
//    
//    private void fillSOAPEnvelope(ServiceClient serviceClient, MessageContext messageContext, OMElement xmlPayload)
//            throws AxisFault {
//        
//        messageContext.setServiceContext(serviceClient.getServiceContext());
//        SOAPFactory soapFactory = getSOAPFactory(serviceClient);
//        SOAPEnvelope envelope = soapFactory.getDefaultEnvelope();
//        if (xmlPayload != null) {
//            envelope.getBody().addChild(xmlPayload);
//        }
//        messageContext.setEnvelope(envelope);
//    }
    
    private ArrayList<FutureTask> getWSFTasks() {
        ArrayList<FutureTask> tasks = new ArrayList<FutureTask>();
        
        ArrayList<WSFDataElement> payloads = testcase.getInputDataVector();
        for(int i=0; i<payloads.size(); i++){
            tasks.add(getWSFTask(i));
        }
        
        return tasks;
    }
    
    private FutureTask getWSFTask(int index){
        
        ArrayList<WSFDataElement> headers = testcase.getInputHeaderVector();
        ArrayList<WSFDataElement> payloads = testcase.getInputDataVector();
        
        WSFDataElement header = null;
        if(index < headers.size())
            header = headers.get(index);
        WSFDataElement payload = payloads.get(index);

        return new FutureTask(new WSFTask(index,header,payload, Thread.currentThread()));
        
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

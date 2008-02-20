/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.testcase;

import client.WSFClient;
import datamodel.WSFConfiguration;
import datamodel.WSFResult;
import datamodel.WSFTestCase;
import gui.WSFApplication;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

/**
 *
 * @author chang
 */
public class ExecuteTestCase extends Task<Void, WSFResult> {

    private static Logger logger = Logger.getLogger(ExecuteTestCase.class);
    private TestCasePanel testCasePanel;
    private WSFTestCase testCase;

    public ExecuteTestCase(WSFApplication app, WSFTestCase testCase, TestCasePanel testCasePanel){
        super(app);
        this.testCase = testCase;
        this.testCasePanel = testCasePanel;
    }
    
    public WSFTestCase getTestCase(){
        return this.testCase;
    }

    @Override
    protected Void doInBackground() throws Exception {
        logger.info("Start the Execution: " + testCase.getName());
        testCase.setSaveNeeded(true);
        WSFConfiguration config = WSFApplication.getApplication().getWSFConfiguration();
        WSFClient client = new WSFClient(this, config.getMaxNumberOfConnectionsPerHost(), config.getMaxNumberOfConnectionsOverall());
        client.doJob();
        return null;
    }

    @Override
    protected void succeeded(Void v){
        logger.info("The Execution is done successfully");
    }

    @Override
    protected void process(List<WSFResult> results){
        for(WSFResult result : results){
            logger.info("process result: index " + result.getInputIndex());
            testCase.getResults().set(result.getInputIndex(), result);
            testCasePanel.updateIndexList(result.getInputIndex());
        }
    }

    @Override
    protected void failed(Throwable cause){
        WSFApplication.showMessage(cause.getMessage());
        StringWriter sWriter = new StringWriter();
        cause.printStackTrace(new PrintWriter(sWriter));
        logger.error(sWriter.toString());
    }

    @Override
    protected void cancelled(){
        logger.info("The execution is canceled: " + testCase.getName());
    }

    @Override
    protected void interrupted(InterruptedException e){
        super.interrupted(e);
        logger.info("The execution is interruppted: " + e);
    }

    @Override
    protected void finished(){
        logger.info("Execution finished: " + testCase.getName());
//        System.out.println("finished: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
    }
    
    public void pulishResult(WSFResult result){
        publish(result);
    }
    
}

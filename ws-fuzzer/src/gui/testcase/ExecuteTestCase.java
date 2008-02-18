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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Task;

/**
 *
 * @author chang
 */
public class ExecuteTestCase extends Task<Void, WSFResult> {

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
        testCase.setSaveNeeded(true);
        WSFConfiguration config = WSFApplication.getApplication().getWSFConfiguration();
        WSFClient client = new WSFClient(this, config.getMaxNumberOfConnectionsPerHost(), config.getMaxNumberOfConnectionsOverall());
        client.doJob();
        return null;
    }

    @Override
    protected void succeeded(Void v){
        System.out.println("succeeded: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
    }

    @Override
    protected void process(List<WSFResult> results){
        for(WSFResult result : results){
            testCase.getResults().set(result.getInputIndex(), result);
            testCasePanel.updateIndexList(result.getInputIndex());
            System.out.println("process: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName() + "  | id of result: " + result.getInputIndex());
        }
    }

    @Override
    protected void failed(Throwable cause){
        
        System.out.println("failed: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
        
        cause.printStackTrace();
    }

    @Override
    protected void cancelled(){
        System.out.println("canceled: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
    }

    @Override
    protected void interrupted(InterruptedException e){
        super.interrupted(e);
        System.out.println("interrupted: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
    }

    @Override
    protected void finished(){
        System.out.println("finished: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
        
    }
    
    public void pulishResult(WSFResult result){
        publish(result);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.testcase;

import client.WSFClient;
import datamodel.WSFConfiguration;
import datamodel.WSFProject;
import datamodel.WSFResult;
import datamodel.WSFTestCase;
import gui.WSFApplication;
import gui.WSFApplicationView;
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
    private WSFProject project;
    
    // index = -1, means execute all
    private int index;
    
    private int totalNumberOfWSFTasks;
    private int executedWSFTasks;
    
    private boolean saveNeeded;
    
    public ExecuteTestCase(WSFApplication app, WSFTestCase testCase, int index, TestCasePanel testCasePanel){
        super(app);
        this.testCase = testCase;
        testCase.setStatus(WSFTestCase.EXECUTION);
        this.testCasePanel = testCasePanel;
        this.project = testCase.getProject();
        this.saveNeeded = false;
        this.index = index;
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
        client.doJob(index);
        return null;
    }

    @Override
    protected void succeeded(Void v){
        if(index == -1)
            testCase.setStatus(WSFTestCase.FINISHED);
        else{
            
        }
        logger.info("The Execution is done successfully");
    }

    @Override
    protected void process(List<WSFResult> results){
        for(WSFResult result : results){
            logger.info("process result: index " + result.getInputIndex());
            
            if(result.getStatus() == WSFResult.PENDING || result.getStatus() == WSFResult.PLACEHOLDER)
                continue;
            
            testCase.getResults().set(result.getInputIndex(), result);
            testCasePanel.updateIndexList(result);
        }
        
        if(!this.saveNeeded){
            this.saveNeeded = true;
            this.project.setSaveTestCaseNeeded(true);
        }
        
        ((WSFApplicationView)WSFApplication.getApplication().getMainView()).setProgressBarMessage(executedWSFTasks+"/"+totalNumberOfWSFTasks);
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
        ((WSFApplicationView)WSFApplication.getApplication().getMainView()).setProgressBarMessage(null);
        logger.info("The execution is canceled: " + testCase.getName());
    }

    @Override
    protected void interrupted(InterruptedException e){
        super.interrupted(e);
        logger.info("The execution is interruppted: " + e);
    }

    @Override
    protected void finished(){
        
        testCase.setExecutor(null);
        testCasePanel.tryUpdateButtons(testCase);
        
        logger.info("Execution finished: " + testCase.getName());
    }
    
    public void pulishResult(WSFResult result){
        synchronized(this){
            publish(result);
        }
    }
    
    public void setProgress(int value, int max){
        this.setProgress(value, 0, max);
        this.executedWSFTasks = value;
        this.totalNumberOfWSFTasks = max;
    }
    
    @Override
    public void setMessage(String msg){
        super.setMessage(msg);
    }
    
    @Override
    public void setTitle(String title){
        super.setTitle(title);
    }
    
}

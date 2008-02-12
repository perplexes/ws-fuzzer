/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author chang
 */
public class WSFTestCase {

    private String name;
    
    private WSFProject project;
    
    private WSFOperation operation;
    
    private ArrayList<OMElement> inputHeaderVector;
    
    private ArrayList<OMElement> inputDataVector;
    
    private ArrayList<WSFResult> results;
    
    private boolean executed;
    private boolean finished;
    
    private WSFStatistic statistic;

    public WSFTestCase(String name, WSFOperation operation){
        this.name = name;
        this.project = operation.getPort().getService().getProject();
        this.operation = operation;
        inputHeaderVector = new ArrayList<OMElement>();
        inputDataVector = new ArrayList<OMElement>();
        results = new ArrayList<WSFResult>();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WSFProject getProject() {
        return project;
    }

    public void setProject(WSFProject project) {
        this.project = project;
    }

    public WSFOperation getOperation() {
        return operation;
    }

    public void setOperation(WSFOperation operation) {
        this.operation = operation;
    }

    public ArrayList<OMElement> getInputDataVector() {
        return inputDataVector;
    }

    public void setInputDataVector(ArrayList<OMElement> inputsVector) {
        this.inputDataVector = inputsVector;
    }

    public ArrayList<WSFResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<WSFResult> results) {
        this.results = results;
    }

    public WSFStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(WSFStatistic statistic) {
        this.statistic = statistic;
    }
    
    public void generateInputsVector(){
        
        ArrayList<WSFInputSource> sources = new ArrayList<WSFInputSource>();
        
        WSFDataElement data = operation.getRequestData();
        
        data.getAllInputSource(sources);
        
        while(true){
            
            this.inputDataVector.add(data.toOMElement(null, false));
            
            int i = 0;
            for(WSFInputSource source : sources){
                if(source.isEnd())
                    i++;
            }
            if(i == sources.size())
                break;
        }
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
}

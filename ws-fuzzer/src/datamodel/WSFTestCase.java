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
    
    private ArrayList<OMElement> inputsVector;
    
    private ArrayList<WSFResult> results;
    
    private WSFStatistic statistic;

    public WSFTestCase(String name, WSFProject project){
        this.name = name;
        this.project = project;
        inputsVector = new ArrayList<OMElement>();
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

    public ArrayList<OMElement> getInputsVector() {
        return inputsVector;
    }

    public void setInputsVector(ArrayList<OMElement> inputsVector) {
        this.inputsVector = inputsVector;
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
    
}

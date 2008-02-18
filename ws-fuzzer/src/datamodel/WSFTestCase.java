/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;

/**
 *
 * @author chang
 */
public class WSFTestCase {

    private String name;
    
    private WSFProject project;
    
    private WSFOperation operation;
    
    private ArrayList<WSFDataElement> inputHeaderVector;
    
    private ArrayList<WSFDataElement> inputDataVector;
    
    private ArrayList<WSFResult> results;
    
    private boolean executed;
    private boolean finished;
    
    private WSFStatistic statistic;
    
    public WSFTestCase(String name, WSFOperation operation){
        this.name = name;
        this.project = operation.getPort().getService().getProject();
        this.operation = operation;
        inputHeaderVector = new ArrayList<WSFDataElement>();
        inputDataVector = new ArrayList<WSFDataElement>();
        results = new ArrayList<WSFResult>();
    }
    
    private WSFTestCase(WSFProject project){
        this.project = project;
        inputHeaderVector = new ArrayList<WSFDataElement>();
        inputDataVector = new ArrayList<WSFDataElement>();
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

    public ArrayList<WSFDataElement> getInputDataVector() {
        return inputDataVector;
    }

    public void setInputDataVector(ArrayList<WSFDataElement> inputsVector) {
        this.inputDataVector = inputsVector;
    }

    public ArrayList<WSFDataElement> getInputHeaderVector() {
        return inputHeaderVector;
    }

    public void setInputHeaderVector(ArrayList<WSFDataElement> inputsVector) {
        this.inputHeaderVector = inputsVector;
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
        WSFDataElement headerData = operation.getRequestHeaderData();
        WSFDataElement data = operation.getRequestData();
        
        if(headerData != null){
            headerData.getAllInputSource(sources);
        }
        if(data != null){
            data.getAllInputSource(sources);
        }
        
        while(true){
            if(headerData != null)
                this.inputHeaderVector.add(headerData.clone().setValues());
            
            if(data != null)
                this.inputDataVector.add(data.clone().setValues());
            
            int i = 0;
            for(WSFInputSource source : sources){
                if(source.isEnd())
                    i++;
            }
            
            if(i == sources.size())
                break;
        }
        
        WSFResult result = new WSFResult();
        result.setInputIndex(-1);
        for(int i=0; i<inputDataVector.size(); i++){
            this.results.add(result);
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
    
    public String toString(){
        return this.name;
    }
    
    public DefaultMutableTreeNode getRequestTreeNode(int indexes) {
        
        WSFDataElement headerElement = null;
        WSFDataElement dataElement = null;
        
        if(indexes < inputHeaderVector.size()){
            headerElement = this.inputHeaderVector.get(indexes);
        }if(indexes < inputDataVector.size()){
            dataElement = this.inputDataVector.get(indexes);
        }
        
        DefaultMutableTreeNode requestTreeNode = new DefaultMutableTreeNode("Request Message");

        if(operation.getPort().getBindingType().equalsIgnoreCase("SOAPBinding")){
            
            if (headerElement != null) {
                DefaultMutableTreeNode requestHeaderTreeNode = new DefaultMutableTreeNode("SOAPHeader");
                requestTreeNode.add(requestHeaderTreeNode);
                headerElement.toTreeNode(requestHeaderTreeNode);
            }
            
            if (dataElement != null) {
                DefaultMutableTreeNode requestBodyTreeNode = new DefaultMutableTreeNode("SOAPbody");
                requestTreeNode.add(requestBodyTreeNode);
                dataElement.toTreeNode(requestBodyTreeNode);
            }
            
            return requestTreeNode;
        }
        
        if(operation.getPort().getBindingType().equalsIgnoreCase("HTTPBinding")){
            dataElement.toTreeNode(requestTreeNode);
            return requestTreeNode;
        }

        requestTreeNode.add(new DefaultMutableTreeNode("Not Supported!"));
        return requestTreeNode;
    }
    
    
    public OMElement serializeToOMElement(OMElement parent){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement root = omDOMFactory.createOMElement(new QName("testcase"), parent);
        OMElement omElement1 = null;
        OMElement omElement2 = null;
        QName qName = null;
        
        // name
        omElement1 = omDOMFactory.createOMElement(new QName("name"), root);
        omElement1.setText(""+this.name);
        
        // project
        omElement1 = omDOMFactory.createOMElement(new QName("project"), root);
        omElement1.setText(""+this.getProject().getName());
        
        // service
        omElement1 = omDOMFactory.createOMElement(new QName("service"), root);
        omElement2 = omDOMFactory.createOMElement(new QName("uri"), omElement1);        
        omElement2.setText(this.operation.getPort().getService().getName().getNamespaceURI());
        omElement2 = omDOMFactory.createOMElement(new QName("localpart"), omElement1);  
        omElement2.setText(this.operation.getPort().getService().getName().getLocalPart());
        
        // port
        omElement1 = omDOMFactory.createOMElement(new QName("port"), root);   
        omElement1.setText(this.operation.getPort().getName());
        
        // operation
        omElement1 = omDOMFactory.createOMElement(new QName("operation"), root);
        omElement2 = omDOMFactory.createOMElement(new QName("uri"), omElement1);        
        omElement2.setText(this.operation.getName().getNamespaceURI());
        omElement2 = omDOMFactory.createOMElement(new QName("localpart"), omElement1);  
        omElement2.setText(this.operation.getName().getLocalPart());
        
        // inputdefinition
        omElement1 = omDOMFactory.createOMElement(new QName("inputdefinition"), root);
        omElement2 = omDOMFactory.createOMElement(new QName("header"), omElement1);
        if(this.operation.getRequestHeaderData() != null){
            this.operation.getRequestHeaderData().serializeToOMElement(omElement2);
        }
        omElement2 = omDOMFactory.createOMElement(new QName("body"), omElement1);
        if(this.operation.getRequestData() != null){
            this.operation.getRequestData().serializeToOMElement(omElement2);
        }
        
        // inputVector
        omElement1 = omDOMFactory.createOMElement(new QName("inputvector"), root);
        for(int i=0; i<this.getInputDataVector().size(); i++){
            
            omElement2 = omDOMFactory.createOMElement(new QName("input"), omElement1);
            
            OMElement header = omDOMFactory.createOMElement(new QName("header"), omElement2);
            
            if(this.inputHeaderVector.size() != 0){
                inputHeaderVector.get(i).serializeToOMElement(header);
            }
        
            OMElement body = omDOMFactory.createOMElement(new QName("body"), omElement2);
            this.inputDataVector.get(i).serializeToOMElement(body);
        }
        
        // results
        omElement1 = omDOMFactory.createOMElement(new QName("results"), root);
        for(int i=0; i<this.results.size(); i++){
            this.results.get(i).serializeToOMElement(omElement1);
        }
        
        return root;
    }
    
    public static WSFTestCase deserializeFromOMElement(OMElement omElement, WSFProject project){
        WSFTestCase testCase = new WSFTestCase(project);
        
        OMElement element = null;
        
        // name
        element = omElement.getFirstChildWithName(new QName("name"));
        testCase.name = element.getText();
        
        // service
        element = omElement.getFirstChildWithName(new QName("service"));
        QName serviceQname = new QName(element.getFirstChildWithName(new QName("uri")).getText(),element.getFirstChildWithName(new QName("localpart")).getText());
        
        // port
        element = omElement.getFirstChildWithName(new QName("port"));
        String portName = element.getText();
        
        // operation
        element = omElement.getFirstChildWithName(new QName("operation"));
        QName operationQname = new QName(element.getFirstChildWithName(new QName("uri")).getText(),element.getFirstChildWithName(new QName("localpart")).getText());
        
        testCase.operation = project.getService(serviceQname).getPort(portName).getOperation(operationQname);
        
        
        // inputdefinition
        // skiped, should be no harm
        
        // inputVector
        element = omElement.getFirstChildWithName(new QName("inputvector"));
        Iterator iterator = element.getChildElements();
        while(iterator.hasNext()){
            OMElement nextElement = (OMElement)iterator.next();
            
            OMElement headerElement = nextElement.getFirstChildWithName(new QName("header")).getFirstChildWithName(new QName("data"));
            
            if(headerElement != null){
                WSFDataElement headerData = WSFDataElement.deserializeFromOMElement(headerElement);
                testCase.inputHeaderVector.add(headerData);
            }
            
            OMElement bodyElement = nextElement.getFirstChildWithName(new QName("body")).getFirstChildWithName(new QName("data"));
            if(bodyElement != null){
                WSFDataElement bodyData = WSFDataElement.deserializeFromOMElement(bodyElement);
                testCase.inputDataVector.add(bodyData);
            }
        }
        
        // results
        element = omElement.getFirstChildWithName(new QName("results"));
        iterator = element.getChildElements();
        while(iterator.hasNext()){
            OMElement nextElement = (OMElement)iterator.next();
            testCase.results.add(WSFResult.deserializeFromOMElement(nextElement));
        }
        
        return testCase;
    }
}

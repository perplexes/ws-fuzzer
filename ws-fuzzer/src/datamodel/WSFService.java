/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;
import javax.xml.namespace.QName;

/**
 *
 * @author chang
 */
public class WSFService {

    private WSFProject project;
    
    private QName name;
    private String document;
    
    private ArrayList<WSFPort> ports;
    
    public WSFService(WSFProject project){
        this.project = project;
        ports = new ArrayList<WSFPort>();
    }
    
    public WSFService(){
        ports = new ArrayList<WSFPort>();
    }

    public WSFProject getProject() {
        return project;
    }

    public void setProject(WSFProject project) {
        this.project = project;
    }

    public QName getName() {
        return name;
    }

    public void setName(QName name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public ArrayList<WSFPort> getPorts() {
        return ports;
    }

    public void setPorts(ArrayList<WSFPort> ports) {
        this.ports = ports;
    }
    
    public void addPort(WSFPort port){
        this.ports.add(port);
    }
    
    public String toString(){
        return this.getName().getLocalPart()+" : Service";
    }
}

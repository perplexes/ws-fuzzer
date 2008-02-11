/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import utils.XMLUtils;

/**
 *
 * @author chang
 */
public class WSFConfiguration {

    private int maxNumberOfConnectionsPerHost;
    private int maxNumberOfConnectionsOverall;
    
    private ArrayList<WSFDictionaryInfo> dictionaries;
    
    private String projectsDirectory;
    private ArrayList<WSFProjectInfo> projects;
    
    public WSFConfiguration(){
        dictionaries = new ArrayList<WSFDictionaryInfo>();
        projects = new ArrayList<WSFProjectInfo>();
    }
    
    public WSFConfiguration(OMElement wsfConfigurationElement){
        
        dictionaries = new ArrayList<WSFDictionaryInfo>();
        projects = new ArrayList<WSFProjectInfo>();
        
        OMElement connectionsElement = wsfConfigurationElement.getFirstChildWithName(new QName("","connections"));
        
        maxNumberOfConnectionsPerHost = Integer.parseInt(connectionsElement.getFirstChildWithName(new QName("","perhost")).getText());
        maxNumberOfConnectionsOverall = Integer.parseInt(connectionsElement.getFirstChildWithName(new QName("","overall")).getText());
        
        OMElement dictionariesElement = wsfConfigurationElement.getFirstChildWithName(new QName("","dictionaries"));
        Iterator dictionariesIterator =  dictionariesElement.getChildElements();
        while(dictionariesIterator.hasNext()){
            dictionaries.add(new WSFDictionaryInfo((OMElement)dictionariesIterator.next()));
        }
        
        OMElement projectsElement = wsfConfigurationElement.getFirstChildWithName(new QName("","projects"));
        Iterator projectsIterator =  projectsElement.getChildElements();
        while(projectsIterator.hasNext()){
            OMElement element = (OMElement)projectsIterator.next();
            if(element.getLocalName().equalsIgnoreCase("directory")){
                projectsDirectory = element.getText();
                continue;
            }
            
            projects.add(new WSFProjectInfo(element));
            
        }
    }

    public int getMaxNumberOfConnectionsPerHost() {
        return maxNumberOfConnectionsPerHost;
    }

    public void setMaxNumberOfConnectionsPerHost(int maxNumberOfConnectionsPerHost) {
        this.maxNumberOfConnectionsPerHost = maxNumberOfConnectionsPerHost;
    }

    public int getMaxNumberOfConnectionsOverall() {
        return maxNumberOfConnectionsOverall;
    }

    public void setMaxNumberOfConnectionsOverall(int maxNumberOfConnectionsOverall) {
        this.maxNumberOfConnectionsOverall = maxNumberOfConnectionsOverall;
    }

    public ArrayList<WSFDictionaryInfo> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(ArrayList<WSFDictionaryInfo> dictionaries) {
        this.dictionaries = dictionaries;
    }

    public String getProjectsDirectory() {
        return projectsDirectory;
    }

    public void setProjectsDirectory(String projectsDirectory) {
        this.projectsDirectory = projectsDirectory;
    }

    public ArrayList<WSFProjectInfo> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<WSFProjectInfo> projects) {
        this.projects = projects;
    }
    
    public void addDictionary(WSFDictionaryInfo dict){
        this.dictionaries.add(dict);
    }
    
    public void removeDictionary(String name){
        for(WSFDictionaryInfo dict : dictionaries ){
            if(dict.getName().equalsIgnoreCase(name))
                dictionaries.remove(dict);
        }
    }
    
    public boolean hasDictionary(String name){
        for(WSFDictionaryInfo dict : dictionaries ){
            if(dict.getName().equalsIgnoreCase(name))
                return true;
        }
        
        return false;
    }
    
    public void addProject(WSFProjectInfo proj){
        this.projects.add(proj);
    }
    
    public void removeProject(String name){
        for(WSFProjectInfo proj : projects){
            if(proj.getName().equalsIgnoreCase(name)){
                projects.remove(proj);
            }
        }
    }
    
    public boolean hasProject(String name){
        for(WSFProjectInfo proj : projects){
            if(proj.getName().equalsIgnoreCase(name))
                return true;
        }
        
        return false;
    }
    
    public OMElement toOMElement(OMElement parent){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement wsfConfigurationElement = null;
        
        if(parent == null){
            wsfConfigurationElement = omDOMFactory.createOMElement(new QName("", "wsfconfiguration"));
        }else{
            wsfConfigurationElement = omDOMFactory.createOMElement(new QName("", "wsfconfiguration"), parent);
        }
        
        // connections
        OMElement connectionsElement = omDOMFactory.createOMElement(new QName("", "connections"), wsfConfigurationElement);
        OMElement perhostElement = omDOMFactory.createOMElement(new QName("", "perhost"), connectionsElement);
        perhostElement.setText(""+this.maxNumberOfConnectionsPerHost);
        OMElement overallElement = omDOMFactory.createOMElement(new QName("", "overall"), connectionsElement);
        overallElement.setText(""+this.maxNumberOfConnectionsOverall);
        
        // dictionaries
        OMElement dictionariesElement = omDOMFactory.createOMElement(new QName("", "dictionaries"), wsfConfigurationElement);
        for(WSFDictionaryInfo dict : this.dictionaries){
            dictionariesElement.addChild(dict.toOMElement(parent));
        }
        
        // projects
        OMElement projectsElement = omDOMFactory.createOMElement(new QName("", "projects"), wsfConfigurationElement);
        OMElement projectsDirectoryElement = omDOMFactory.createOMElement(new QName("", "directory"), projectsElement);
        projectsDirectoryElement.setText(this.projectsDirectory);
        for(WSFProjectInfo proj : this.projects){
            projectsElement.addChild(proj.toOMElement(parent));
        }
        
        return wsfConfigurationElement;
    }
    
    public static void main(String[] args) throws Exception{
        
        WSFConfiguration config1 = new WSFConfiguration();
        config1.setMaxNumberOfConnectionsPerHost(5);
        config1.setMaxNumberOfConnectionsOverall(10);
        config1.addDictionary(new WSFDictionaryInfo("Currency_1","test/dict1.txt"));
        config1.addDictionary(new WSFDictionaryInfo("Currency_2","test/dict2.txt"));
        config1.setProjectsDirectory("projects");
        config1.addProject(new WSFProjectInfo("project_1","wsdl_uri_1"));
        config1.addProject(new WSFProjectInfo("project_2","wsdl_uri_2"));
        
        
        System.out.println("+++++++++ xmlstring << configuration +++++++++");
        
        System.out.println(XMLUtils.toPrettifiedString(config1.toOMElement(null)));
        
        System.out.println("xxxxxxxxx xmlstring >> configuration xxxxxxxxx");
        
        OMElement element = XMLUtils.toOMElement(XMLUtils.toPrettifiedString(config1.toOMElement(null)));
        System.out.println(element);
        WSFConfiguration config2 = new WSFConfiguration(element);
        System.out.println(XMLUtils.toPrettifiedString(config2.toOMElement(null)));
    }
}

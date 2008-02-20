/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import org.apache.log4j.Logger;
import org.jdesktop.application.AbstractBean;
import utils.XMLUtils;

/**
 *
 * @author chang
 */
public class WSFConfiguration extends AbstractBean{

    private static Logger logger = Logger.getLogger(WSFConfiguration.class);
    
    private int maxNumberOfConnectionsPerHost;
    private int maxNumberOfConnectionsOverall;
    
    private Vector<WSFDictionaryInfo> dictionaries;
    
    private File projectsDirectory;
    private ArrayList<WSFProjectInfo> projects;
    
    private boolean changed;
    private File file;
    
    public WSFConfiguration(File file){
        
        changed = false;
        this.file = file;
        
        dictionaries = new Vector<WSFDictionaryInfo>();
        projects = new ArrayList<WSFProjectInfo>();
    }
    
    public static WSFConfiguration createWSFConfiguration(OMElement wsfConfigurationElement, File file){
        
        WSFConfiguration conf = new WSFConfiguration(file);
        
        conf.dictionaries = new Vector<WSFDictionaryInfo>();
        conf.projects = new ArrayList<WSFProjectInfo>();
        
        OMElement connectionsElement = wsfConfigurationElement.getFirstChildWithName(new QName("","connections"));
        
        conf.maxNumberOfConnectionsPerHost = Integer.parseInt(connectionsElement.getFirstChildWithName(new QName("","perhost")).getText());
        conf.maxNumberOfConnectionsOverall = Integer.parseInt(connectionsElement.getFirstChildWithName(new QName("","overall")).getText());
        
        OMElement dictionariesElement = wsfConfigurationElement.getFirstChildWithName(new QName("","dictionaries"));
        Iterator dictionariesIterator =  dictionariesElement.getChildElements();
        while(dictionariesIterator.hasNext()){
            conf.dictionaries.add(new WSFDictionaryInfo((OMElement)dictionariesIterator.next()));
        }
        
        OMElement projectsElement = wsfConfigurationElement.getFirstChildWithName(new QName("","projects"));
        Iterator projectsIterator =  projectsElement.getChildElements();
        while(projectsIterator.hasNext()){
            OMElement element = (OMElement)projectsIterator.next();
            if(element.getLocalName().equalsIgnoreCase("directory")){
                conf.projectsDirectory = new File(element.getText());
                continue;
            }
            
            conf.projects.add(new WSFProjectInfo(element));   
        }
        
        conf.changed = false;
        
        return conf;
    }
    
    public static WSFConfiguration createWSFConfigruation(File xmlFile) throws FileNotFoundException, XMLStreamException{
        OMElement wsfConfigurationElement = XMLUtils.toOMElement(xmlFile);
        return createWSFConfiguration(wsfConfigurationElement, xmlFile);
    }

    public boolean isChanged(){
        return changed;
    }
    
    public void setChanged(boolean changed){
        this.changed = changed;
    }
    
    public int getMaxNumberOfConnectionsPerHost() {
        return maxNumberOfConnectionsPerHost;
    }

    public void setMaxNumberOfConnectionsPerHost(int maxNumberOfConnectionsPerHost) {
        changed = true;
        this.maxNumberOfConnectionsPerHost = maxNumberOfConnectionsPerHost;
    }

    public int getMaxNumberOfConnectionsOverall() {
        return maxNumberOfConnectionsOverall;
    }

    public void setMaxNumberOfConnectionsOverall(int maxNumberOfConnectionsOverall) {
        changed = true;
        this.maxNumberOfConnectionsOverall = maxNumberOfConnectionsOverall;
    }

    public Vector<WSFDictionaryInfo> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Vector<WSFDictionaryInfo> dictionaries) {
        changed = true;
        this.dictionaries = dictionaries;
    }

    public File getProjectsDirectory() {
        return projectsDirectory;
    }

    public void setProjectsDirectory(File projectsDirectory) {
        changed = true;
        this.projectsDirectory = projectsDirectory;
    }

    public ArrayList<WSFProjectInfo> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<WSFProjectInfo> projects) {
        changed = true;
        this.projects = projects;
    }
    
    public int getDictionaryIndex(String dictionaryName){
        for(int i=0; i<dictionaries.size(); i++)
            if(dictionaries.get(i).getName().equalsIgnoreCase(dictionaryName))
                return i;
        return -1;
    }
    
    public void addDictionary(WSFDictionaryInfo dict){
        changed = true;
        this.dictionaries.add(dict);
        this.firePropertyChange("dictionaries", null, dictionaries);
    }
    
    public void removeDictionary(String name){
        changed = true;
        for(int i=0; i<dictionaries.size(); i++){
            if(dictionaries.get(i).getName().equalsIgnoreCase(name))
                dictionaries.remove(i);
        }
        this.firePropertyChange("dictionaries", null, dictionaries);
    }
    
    public void removeDictionary(int i){
        changed = true;
        dictionaries.remove(i);
        this.firePropertyChange("dictionaries", null, dictionaries);
    }
    
    public boolean hasDictionary(String name){
        for(WSFDictionaryInfo dict : dictionaries ){
            if(dict.getName().equalsIgnoreCase(name))
                return true;
        }
        
        return false;
    }
    
    public void addProject(WSFProjectInfo proj){
        changed = true;
        this.projects.add(proj);
    }
    
    public void removeProject(String name){
        changed = true;
        
        for(int i=0; i<projects.size(); i++){
             if(projects.get(i).getName().equalsIgnoreCase(name)){
                projects.remove(i);
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
        projectsDirectoryElement.setText(this.projectsDirectory.getAbsolutePath());
        for(WSFProjectInfo proj : this.projects){
            projectsElement.addChild(proj.toOMElement(parent));
        }
        
        return wsfConfigurationElement;
    }
    
    public void saveChanges() throws IOException, XMLStreamException, Exception{
        
        if(!this.changed || this.file == null)
            return;
        
        logger.info("Save Configuration to File: " + this.file.getAbsolutePath());
        XMLUtils.saveToFile(this.toOMElement(null), file);
        
        this.changed = false;
    }
    
    public static void main(String[] args) throws Exception{
        
        WSFConfiguration config1 = new WSFConfiguration(null);
        config1.setMaxNumberOfConnectionsPerHost(5);
        config1.setMaxNumberOfConnectionsOverall(10);
        config1.addDictionary(new WSFDictionaryInfo("Currency_1","test/dict1.txt"));
        config1.addDictionary(new WSFDictionaryInfo("Currency_2","test/dict2.txt"));
        config1.setProjectsDirectory(new File("projects"));
        config1.addProject(new WSFProjectInfo("project_1","wsdl_uri_1"));
        config1.addProject(new WSFProjectInfo("project_2","wsdl_uri_2"));
        
        
        System.out.println("+++++++++ xmlstring << configuration +++++++++");
        
        System.out.println(XMLUtils.toPrettifiedString(config1.toOMElement(null)));
        
        System.out.println("xxxxxxxxx xmlstring >> configuration xxxxxxxxx");
        
        OMElement element = XMLUtils.toOMElement(XMLUtils.toPrettifiedString(config1.toOMElement(null)));
        System.out.println(element);
        WSFConfiguration config2 = WSFConfiguration.createWSFConfiguration(element, null);
        System.out.println(XMLUtils.toPrettifiedString(config2.toOMElement(null)));
    }
}

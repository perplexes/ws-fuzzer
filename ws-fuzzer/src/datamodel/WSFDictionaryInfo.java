/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import utils.XMLUtils;

/**
 *
 * @author chang
 */
public class WSFDictionaryInfo {

    private String name;
    private String path;

    public WSFDictionaryInfo(String name, String path){
        this.name = name;
        this.path = path;
    }
    
    public WSFDictionaryInfo(OMElement element){
        this.name = element.getFirstChildWithName(new QName("","name")).getText();
        this.path = element.getFirstChildWithName(new QName("","path")).getText();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public OMElement toOMElement(OMElement parent){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement dictionaryElement = null;
        
        if(parent == null){
            dictionaryElement = omDOMFactory.createOMElement(new QName("", "dictionary"));
        }else{
            dictionaryElement = omDOMFactory.createOMElement(new QName("", "dictionary"), parent);
        }
        
        OMElement nameElement = omDOMFactory.createOMElement(new QName("", "name"), dictionaryElement);
        nameElement.setText(this.name);

        OMElement pathElement = omDOMFactory.createOMElement(new QName("", "path"), dictionaryElement);
        pathElement.setText(this.path);
            
        return dictionaryElement;
    }
    
    public static void main(String[] args) throws Exception{
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        // create <dictionaries>
        OMElement dictionaries = omDOMFactory.createOMElement(new QName("", "dictionaries"));
        
        // creante and add one <dictionary>
        OMElement dictionary1 = omDOMFactory.createOMElement(new QName("", "dictionary"), dictionaries);
        OMElement nameElement1 = omDOMFactory.createOMElement(new QName("", "name"), dictionary1);
        nameElement1.setText("sql_injection_1");
        OMElement pathElement1 = omDOMFactory.createOMElement(new QName("", "path"), dictionary1);
        pathElement1.setText("path/to/dictionary_1");
        
        // creante and add a second <dictionary>
        OMElement dictionary2 = omDOMFactory.createOMElement(new QName("", "dictionary"), dictionaries);
        OMElement nameElement2 = omDOMFactory.createOMElement(new QName("", "name"), dictionary2);
        nameElement2.setText("sql_injection_2");
        OMElement pathElement2 = omDOMFactory.createOMElement(new QName("", "path"), dictionary2);
        pathElement2.setText("path/to/dictionary_2");
        
        
        System.out.println("+++++++++ xmlstring <<>> dictionaryIfno +++++++++");
        
        System.out.println(XMLUtils.toPrettifiedString(dictionaries));
        
        System.out.println("+++++++++ xmlstring >> dictionaryIfno +++++++++");
        
        WSFDictionaryInfo dict1 = new WSFDictionaryInfo(dictionary1);
        
        WSFDictionaryInfo dict2 = new WSFDictionaryInfo(dictionary2);
        
        System.out.println("+++++++++ xmlstring << dictionaryIfno +++++++++");
        
        System.out.println(XMLUtils.toPrettifiedString(dict1.toOMElement(null)));
        
        System.out.println(XMLUtils.toPrettifiedString(dict2.toOMElement(null)));
    }
}

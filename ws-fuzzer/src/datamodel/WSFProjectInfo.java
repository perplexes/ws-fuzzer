/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;

/**
 *
 * @author chang
 */
public class WSFProjectInfo {

    private String name;
    private String wsdlURI;
    
    public WSFProjectInfo(String name, String wsdlURI){
        this.name = name;
        this.wsdlURI = wsdlURI;
    }
    
    public WSFProjectInfo(OMElement element){
        this.name = element.getFirstChildWithName(new QName("","name")).getText();
        this.wsdlURI = element.getFirstChildWithName(new QName("","wsdluri")).getText();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getWSDLURI(){
        return this.wsdlURI;
    }
    
    public void setWSDLURI(String wsdlURI){
        this.wsdlURI = wsdlURI;
    }
    
    public OMElement toOMElement(OMElement parent){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement projectElement = null;
        
        if(parent == null){
            projectElement = omDOMFactory.createOMElement(new QName("", "project"));
        }else{
            projectElement = omDOMFactory.createOMElement(new QName("", "project"), parent);
        }
        
        OMElement nameElement = omDOMFactory.createOMElement(new QName("", "name"), projectElement);
        nameElement.setText(this.name);

        OMElement wsdlURIElement = omDOMFactory.createOMElement(new QName("", "wsdluri"), projectElement);
        wsdlURIElement.setText(this.wsdlURI);
            
        return projectElement;
    }
}

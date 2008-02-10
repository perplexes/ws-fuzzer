/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAttribute;

/**
 *
 * @author chang
 */
public class WSFDataAttribute {

    private QName name;
    private QName type;
    private String value;

    public WSFDataAttribute(){
        
    }

    public QName getName() {
        return name;
    }

    public void setName(QName name) {
        this.name = name;
    }

    public QName getType() {
        return type;
    }

    public void setType(QName type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public OMAttribute toOMAttribute(){
//        OMDOMFactory omDomFactory = new OMDOMFactory();
//        return omDomFactory.createOMAttribute(name.getLocalPart(), omDomFactory.createOMNamespace(name.getNamespaceURI(),), value);
        return null;
    }
}

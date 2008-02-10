/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;


import java.util.ArrayList;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;

/**
 *
 * @author Chang Shu
 */
public class WSFDataElement {
    
    private QName name;
    
    private boolean simpleType;
    private WSFInputSource source;
    
    private QName type;
    private String value;
    
    private long minOccurs;
    private long maxOccurs;
    
    private ArrayList<WSFDataAttribute> dataAttributes;
    private ArrayList<WSFDataElement> dataElements;

    public WSFDataElement(){
        dataAttributes = new ArrayList<WSFDataAttribute>();
        dataElements = new ArrayList<WSFDataElement>();
    }

    public QName getName() {
        return name;
    }

    public void setName(QName name) {
        this.name = name;
    }

    public boolean isSimpleType() {
        return simpleType;
    }

    public void setSimpleType(boolean simpleType) {
        this.simpleType = simpleType;
    }
    
    public WSFInputSource getSource(){
        return this.source;
    }
    
    public void setSource(WSFInputSource source){
        this.source = source;
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

    public ArrayList<WSFDataAttribute> getDataAttributes() {
        return dataAttributes;
    }
    
    public void setDataAttributes(ArrayList<WSFDataAttribute> dataAttributes) {
        this.dataAttributes = dataAttributes;
    }
    
    public void addDataAttribute(WSFDataAttribute dataAttribute){
        this.dataAttributes.add(dataAttribute);
    }
    
    public boolean hasDataAttributes(){
        if(this.dataAttributes.size() > 0)
            return true;
        return false;
    }
    
    public ArrayList<WSFDataElement> getDataElements() {
        return dataElements;
    }
    
    public void setDataElements(ArrayList<WSFDataElement> dataElements) {
        this.dataElements = dataElements;
    }
    
    public void addDataElement(WSFDataElement element){
        this.dataElements.add(element);
    }
    
    public long getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs(long minOccurs) {
        this.minOccurs = minOccurs;
    }

    public long getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs(long maxOccurs) {
        this.maxOccurs = maxOccurs;
    }
    
    // 
    public OMElement toOMElement(OMElement parent, boolean isMuster){
        
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement omElement = null;
        if(parent == null)
            omElement = omDOMFactory.createOMElement(this.name);
        else 
            omElement = omDOMFactory.createOMElement(this.name, parent);
        
        if(this.simpleType){
            omElement.setText(isMuster ? ( source==null ? "undefined" : source.toString() ) : ( source == null ? "undefined" : source.getNextValue() ) );
            return omElement;
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            dataElement.toOMElement(omElement, isMuster);
        }
        
        return omElement;
    }
    
    public String toString(){
        
        if(this.simpleType)
            return "["+this.minOccurs+".."+this.maxOccurs+"] "+this.name.getLocalPart()+" : "+this.type.getLocalPart()+" { "+" }";
        
        return "["+this.minOccurs+".."+this.maxOccurs+"] "+this.name.getLocalPart();
    }
    
    public static void print(WSFDataElement element){
        
        System.out.println(element.toString());
        
        if(element.isSimpleType()){
            return;
        }
        
        for(WSFDataElement dataElement : element.getDataElements()){
            print(dataElement);
        }
    }
}
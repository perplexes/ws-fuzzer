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
 * @author Chang Shu
 */
public class WSFDataElement {
    
    private boolean isRequest;
    
    private QName name;
    
    private boolean simpleType;
    private WSFInputSource source;
    
    private QName type;
    private String value;
    
    private long minOccurs;
    private long maxOccurs;
    private boolean enabled;
    
    private ArrayList<WSFDataAttribute> dataAttributes;
    private ArrayList<WSFDataElement> dataElements;

    public WSFDataElement(){
        this.enabled = true;
        dataAttributes = new ArrayList<WSFDataAttribute>();
        dataElements = new ArrayList<WSFDataElement>();
    }

    public QName getName() {
        return name;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    
    public boolean isEnabled(){
        return this.enabled;
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
    
    public void getAllInputSource(ArrayList<WSFInputSource> sources){
        if(this.simpleType){
            sources.add(source);
            return;
        }
        
        for(WSFDataElement element : dataElements){
            if(!element.isEnabled())
                continue;
            element.getAllInputSource(sources);
        }
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
            omElement.setText(isMuster ? "[WSF_INPUT_DEFINITION_BEGINN]" + ( source==null ? "undefined" : (source.getInputSourceType() == WSFInputSource.INPUT_FROM_FIXED_VALUE ? ("FixedValue( " + source.toString() + " )" ) : ("FromDictionary( " + source.toString() + " )" ))) + "[WSF_INPUT_DEFINITION_END]" : ( value ) );
            return omElement;
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            if(dataElement.isEnabled())
                dataElement.toOMElement(omElement, isMuster);
        }
        
        return omElement;
    }
    
    public DefaultMutableTreeNode toTreeNode(DefaultMutableTreeNode parent){
        
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(this);
        
        if(parent != null)
            parent.add(treeNode);
        
        if(this.simpleType)
            return treeNode;
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            dataElement.toTreeNode(treeNode);
        }
        
        return treeNode;
    }
    
    @Override
    public String toString(){
        
        if(this.value != null){
            if(this.simpleType)
                return this.name.getLocalPart()+" : "+this.type.getLocalPart()+" { "+ value +" }";
            else
                return this.name.getLocalPart();
        }
        
        if(this.value == null && this.simpleType){
            if(this.isRequest)
                return "["+this.minOccurs+".."+this.maxOccurs+"] "+this.name.getLocalPart()+" : "+this.type.getLocalPart()+" { "+ ( source == null ? "undefined" : (source.getInputSourceType() == WSFInputSource.INPUT_FROM_FIXED_VALUE ? "FixedValue("+ source.toString() +")" : "FromDictionary("+ source.toString() +")" ) ) +" }";
            else
                return "["+this.minOccurs+".."+this.maxOccurs+"] "+this.name.getLocalPart()+" : "+this.type.getLocalPart();
        }
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
    
    public boolean isInputDefinitionComplete(){
        if(this.isSimpleType() && this.source != null){
            return true;
        }
        
        if(this.isSimpleType() && this.source == null){
            return false;
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            if(!dataElement.isEnabled())
                continue;
            
            if(!dataElement.isInputDefinitionComplete())
                return false;
        }
        
        return true;
    }
    
    public void setIsRequest(boolean isRequest){
        
        this.isRequest = isRequest;
        
        if(this.isSimpleType()){
            return;
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
//            System.out.println(dataElement);
            dataElement.setIsRequest(isRequest);
        }
    }
    
    public void clearInputDefinition(){
        if(simpleType){
            this.source = null;
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            dataElement.clearInputDefinition();
        }
    }
    
    @Override
    public WSFDataElement clone(){
        WSFDataElement dataElement = new WSFDataElement();
        
        dataElement.isRequest = isRequest;
        dataElement.name = name;
        dataElement.simpleType = simpleType;
        dataElement.source = source;
        dataElement.type = type;
        dataElement.value = value;

        dataElement.enabled = enabled;
        
        dataElement.minOccurs = minOccurs;
        dataElement.maxOccurs = maxOccurs;
        
        if(dataAttributes.size() != 0){
             for(WSFDataAttribute dataAttribute : dataAttributes){
                dataElement.addDataAttribute(dataAttribute);
            }
        }
        
        if(dataElements.size() != 0){
            for(WSFDataElement element : dataElements) {
                dataElement.addDataElement(element.clone());
            }
        }
        
        return dataElement;
    }
    
    public WSFDataElement setValues(){
        
        if(simpleType){
            value = source.getNextValue();
            return this;
        }else{
            value = "";
        }
        
        for(WSFDataAttribute dataAttribute : dataAttributes){
            // TODO: to support attribute
        }
        
        for(WSFDataElement dataElement : dataElements) {
            if(!dataElement.isEnabled())
                continue;
            dataElement.setValues();
        }
        
        return this;
    }
    
    
    
    public OMElement serializeToOMElement(OMElement parent){
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement data = omDOMFactory.createOMElement(new QName("data"), parent);
        
        OMElement omElement1 = null;
        OMElement omElement2 = null;
        
        // isRequest
        omElement1 = omDOMFactory.createOMElement(new QName("isrequest"), data);
        omElement1.setText(this.isRequest ? "true" : "false");
        
        // name
        omElement1 = omDOMFactory.createOMElement(new QName("name"), data);
        omElement2 = omDOMFactory.createOMElement(new QName("uri"), omElement1);        omElement2.setText(this.name.getNamespaceURI());
        omElement2 = omDOMFactory.createOMElement(new QName("localpart"), omElement1);  omElement2.setText(this.name.getLocalPart());
        
        // simpleType
        omElement1 = omDOMFactory.createOMElement(new QName("simpletype"), data);
        omElement1.setText(this.simpleType ? "true" : "false");
        
        // enabled
        omElement1 = omDOMFactory.createOMElement(new QName("enabled"), data);
        omElement1.setText(this.enabled ? "true" : "false");
        
        // type
        if(this.type != null){
            omElement1 = omDOMFactory.createOMElement(new QName("type"), data);
            omElement2 = omDOMFactory.createOMElement(new QName("uri"), omElement1);        
            omElement2.setText(this.type.getNamespaceURI());
            omElement2 = omDOMFactory.createOMElement(new QName("localpart"), omElement1);  
            omElement2.setText(this.type.getLocalPart());
        }
        
        // value
        omElement1 = omDOMFactory.createOMElement(new QName("value"), data);
        omElement1.setText(value);
        
        // minOccurs
        omElement1 = omDOMFactory.createOMElement(new QName("minoccurs"), data);
        omElement1.setText(""+minOccurs);
        
        // maxOccurs
        omElement1 = omDOMFactory.createOMElement(new QName("maxoccurs"), data);
        omElement1.setText(""+minOccurs);
        
        // source
        omElement1 = omDOMFactory.createOMElement(new QName("source"), data);
        if(source != null){   
            if(source.getInputSourceType() == WSFInputSource.INPUT_FROM_FIXED_VALUE){
                omElement2 = omDOMFactory.createOMElement(new QName("fixedvalue"), omElement1);
                omElement2.setText(source.toString());
            }else if(source.getInputSourceType() == WSFInputSource.INPUT_FROM_DICTIONARY){
                omElement2 = omDOMFactory.createOMElement(new QName("fromdictionary"), omElement1);
                omElement2.setText(source.toString());
            }
        }
        
        // attributes
        omElement1 = omDOMFactory.createOMElement(new QName("attribute"), data);
        
        // elements
        omElement1 = omDOMFactory.createOMElement(new QName("elements"), data);
        
        if(this.simpleType){
            return omElement1;
        }
        
        for(WSFDataElement dataElement : dataElements) {
            dataElement.serializeToOMElement(omElement1);
        }
        
        return data;
    }
    
    public static WSFDataElement deserializeFromOMElement(OMElement omElement){
        WSFDataElement dataElement = new WSFDataElement();
        
        OMElement element = null;
        OMElement element2 = null;
        QName qName = null;
        
        // isRequest
        element = omElement.getFirstChildWithName(new QName("isrequest"));
        dataElement.isRequest = element.getText().equalsIgnoreCase("true") ? true : false;
        
        // name
        element = omElement.getFirstChildWithName(new QName("name"));
        qName = new QName(element.getFirstChildWithName(new QName("uri")).getText(), element.getFirstChildWithName(new QName("localpart")).getText());
        dataElement.name = qName;
        
        // simpleType
        element = omElement.getFirstChildWithName(new QName("simpletype"));
        dataElement.simpleType = element.getText().equalsIgnoreCase("true") ? true : false;
        
        // enabled
        element = omElement.getFirstChildWithName(new QName("enabled"));
        dataElement.enabled = element.getText().equalsIgnoreCase("true") ? true : false;
        
        // type
        element = omElement.getFirstChildWithName(new QName("type"));
        if(element != null){
            qName = new QName(element.getFirstChildWithName(new QName("uri")).getText(), element.getFirstChildWithName(new QName("localpart")).getText());
            dataElement.type = qName;
        }
        
        // value
        element = omElement.getFirstChildWithName(new QName("value"));
        dataElement.value = element.getText();
        
        // minOccurs
        element = omElement.getFirstChildWithName(new QName("minoccurs"));
        dataElement.minOccurs = Long.parseLong(element.getText());
        
        // maxOccurs
        element = omElement.getFirstChildWithName(new QName("maxoccurs"));
        dataElement.maxOccurs = Long.parseLong(element.getText());
        
        // source
        // skiped, should no negativ effect for testcase
        dataElement.source = null;
        
        // attributes
        // atrribtes is jetzt nicht supported
        
        // elements
        element = omElement.getFirstChildWithName(new QName("elements"));
        Iterator iterator = element.getChildElements();
        while(iterator.hasNext()){
            element2 = (OMElement)iterator.next();
            dataElement.addDataElement(deserializeFromOMElement(element2));
        }
        
        return dataElement;
    }
}

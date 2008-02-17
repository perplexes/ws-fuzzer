/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.namespace.QName;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.dom.soap11.SOAP11Factory;
import utils.StringUtils;
import utils.XMLUtils;

/**
 *
 * @author chang
 */
public class WSFOperation {

    private WSFPort port;
    private boolean supported;
    private String bindingOperationType;        // soapOperation ?
    private String bindingSoapAction;           // if soap binding
    private String bindingSOAPStyle;
    private String bindingHttpLocation;         // if httpbinding
    private QName name;
    private String document;
    private String mep;
    private QName requestMessageQName;
    private WSFDataElement requestData;
    private String bindingRequestSOAPUse;
    private QName requestHeaderMessageQName;
    private String requestHeaderMessagePart;
    private String requestHeaderMessageUse;
    private WSFDataElement requestHeaderData;
    private QName responseMessageQName;
    private WSFDataElement responseData;
    private String bindingResponseSOAPUse;
    private QName responseHeaderMessageQName;
    private String responseHeaderMessagePart;
    private String responseHeaderMessageUse;
    private WSFDataElement responseHeaderData;

    public void print() {
        System.out.println("----------- Operation -----------");
        System.out.println("QName: " + this.name);
        System.out.println("MEP:   " + this.mep);
        System.out.println("inHeader: " + this.requestHeaderMessageQName);
        System.out.println("inMsg: " + this.requestMessageQName);
        WSFDataElement.print(requestData);
        System.out.println("outHeader: " + this.responseHeaderMessageQName);
        System.out.println("outMsg:" + this.responseMessageQName);
        WSFDataElement.print(responseData);
        System.out.println();
    }

    public WSFOperation(WSFPort port) {
        this.port = port;
    }

    public WSFPort getPort() {
        return port;
    }

    public void setPort(WSFPort port) {
        this.port = port;
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        if (!this.port.isSupported()) {
            this.supported = false;
        } else {
            this.supported = supported;
        }
    }

    public String getBindingOperationType() {
        return bindingOperationType;
    }

    public void setBindingOperationType(String bindingOperationType) {
        this.bindingOperationType = bindingOperationType;
    }

    public String getBindingSoapAction() {
        return bindingSoapAction;
    }

    public void setBindingSoapAction(String bindingSoapAction) {
        this.bindingSoapAction = bindingSoapAction;
    }

    public String getBindingSOAPStyle() {
        return bindingSOAPStyle;
    }

    public void setBindingSOAPStyle(String bindingSOAPStyle) {
        this.bindingSOAPStyle = bindingSOAPStyle;
    }

    public String getBindingRequestSOAPUse() {
        return bindingRequestSOAPUse;
    }

    public void setBindingRequestSOAPUse(String bindingRequestSOAPUse) {
        this.bindingRequestSOAPUse = bindingRequestSOAPUse;
    }

    public String getBindingResponseSOAPUse() {
        return bindingResponseSOAPUse;
    }

    public void setBindingResponseSOAPUse(String bindingResponseSOAPUse) {
        this.bindingResponseSOAPUse = bindingResponseSOAPUse;
    }

    public String getBindingHttpLocation() {
        return bindingHttpLocation;
    }

    public void setBindingHttpLocation(String bindingHttpLocation) {
        this.bindingHttpLocation = bindingHttpLocation;
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

    public String getMEP() {
        return mep;
    }

    public void setMEP(String mep) {
        this.mep = mep;
    }

    public WSFDataElement getRequestData() {
        return requestData;
    }

    public void setRequestData(WSFDataElement requestData) {
        this.requestData = requestData;
    }

    public WSFDataElement getResponseData() {
        return responseData;
    }

    public void setResponseData(WSFDataElement responseData) {
        this.responseData = responseData;
    }

    public QName getRequestMessageQName() {
        return requestMessageQName;
    }

    public void setRequestMessageQName(QName requestMessageQName) {
        this.requestMessageQName = requestMessageQName;
    }

    public QName getResponseMessageQName() {
        return responseMessageQName;
    }

    public void setResponseMessageQName(QName responseMessageQName) {
        this.responseMessageQName = responseMessageQName;
    }

    public QName getRequestHeaderMessageQName() {
        return requestHeaderMessageQName;
    }

    public void setRequestHeaderMessageQName(QName requestHeaderMessageQName) {
        this.requestHeaderMessageQName = requestHeaderMessageQName;
    }

    public String getRequestHeaderMessagePart() {
        return requestHeaderMessagePart;
    }

    public void setRequestHeaderMessagePart(String requestHeaderMessagePart) {
        this.requestHeaderMessagePart = requestHeaderMessagePart;
    }

    public String getRequestHeaderMessageUse() {
        return requestHeaderMessageUse;
    }

    public void setRequestHeaderMessageUse(String requestHeaderMessageUse) {
        this.requestHeaderMessageUse = requestHeaderMessageUse;
    }

    public WSFDataElement getRequestHeaderData() {
        return requestHeaderData;
    }

    public void setRequestHeaderData(WSFDataElement requestHeaderData) {
        this.requestHeaderData = requestHeaderData;
    }

    public QName getResponseHeaderMessageQName() {
        return responseHeaderMessageQName;
    }

    public void setResponseHeaderMessageQName(QName responseHeaderMessageQName) {
        this.responseHeaderMessageQName = responseHeaderMessageQName;
    }

    public String getResponseHeaderMessagePart() {
        return responseHeaderMessagePart;
    }

    public void setResponseHeaderMessagePart(String responseHeaderMessagePart) {
        this.responseHeaderMessagePart = responseHeaderMessagePart;
    }

    public String getResponseHeaderMessageUse() {
        return responseHeaderMessageUse;
    }

    public void setResponseHeaderMessageUse(String responseHeaderMessageUse) {
        this.responseHeaderMessageUse = responseHeaderMessageUse;
    }

    public WSFDataElement getResponseHeaderData() {
        return responseHeaderData;
    }

    public void setResponseHeaderData(WSFDataElement responseHeaderData) {
        this.responseHeaderData = responseHeaderData;
    }

    public DefaultMutableTreeNode getRequestTreeNode() {

        DefaultMutableTreeNode requestTreeNode = new DefaultMutableTreeNode("Request Message");

        if(this.port.getBindingType().equalsIgnoreCase("SOAPBinding")){
            
            if (this.requestHeaderData != null) {
                DefaultMutableTreeNode requestHeaderTreeNode = new DefaultMutableTreeNode("SOAPHeader");
                requestTreeNode.add(requestHeaderTreeNode);
                this.requestHeaderData.toTreeNode(requestHeaderTreeNode);
            }
            
            if (this.requestData != null) {
                DefaultMutableTreeNode requestBodyTreeNode = new DefaultMutableTreeNode("SOAPbody");
                requestTreeNode.add(requestBodyTreeNode);
                this.requestData.toTreeNode(requestBodyTreeNode);
            }
            
            return requestTreeNode;
        }
        
        if(this.port.getBindingType().equalsIgnoreCase("HTTPBinding")){
            this.requestData.toTreeNode(requestTreeNode);
            return requestTreeNode;
        }

        
        requestTreeNode.add(new DefaultMutableTreeNode("Not Supported!"));
        return requestTreeNode;
    }

    public DefaultMutableTreeNode getResponseTreeNode() {

        DefaultMutableTreeNode responseTreeNode = new DefaultMutableTreeNode("Response Message");

        if(this.port.getBindingType().equalsIgnoreCase("SOAPBinding")){
            
            if (this.responseHeaderData != null) {
                DefaultMutableTreeNode responseHeaderTreeNode = new DefaultMutableTreeNode("SOAPHeader");
                responseTreeNode.add(responseHeaderTreeNode);
                this.responseHeaderData.toTreeNode(responseHeaderTreeNode);
            }
            
            if (this.responseData != null) {
                DefaultMutableTreeNode responseBodyTreeNode = new DefaultMutableTreeNode("SOAPBody");
                responseTreeNode.add(responseBodyTreeNode);
                this.responseData.toTreeNode(responseBodyTreeNode);
            }
            
            return responseTreeNode;
        }
        
        
        if(this.port.getBindingType().equalsIgnoreCase("HTTPBinding")){
            this.responseData.toTreeNode(responseTreeNode);
            return responseTreeNode;
        }
        
        responseTreeNode.add(new DefaultMutableTreeNode("Not Supported!"));
        return responseTreeNode;
    }

    public String getPreview() throws Exception{
        
        SOAP11Factory soapFactory = new SOAP11Factory();
        SOAPEnvelope envelop = soapFactory.createSOAPEnvelope();
        soapFactory.createSOAPHeader(envelop);
        soapFactory.createSOAPBody(envelop);
        
        if(this.requestHeaderData != null){
            requestHeaderData.toOMElement(envelop.getHeader(), true);
        }
        if(this.requestData != null){
            requestData.toOMElement(envelop.getBody(), true);
        }
        
        return StringUtils.prepareXmlForHtml(XMLUtils.toPrettifiedString(envelop));
    }
    
    /**
     * if there are no undefined Input, return true
     * @return
     */
    public boolean isInputDefinitionComplete(){
        
        if(this.requestData!=null && !this.requestData.isInputDefinitionComplete())
            return false;
        
        if(this.requestHeaderData!=null && !this.requestHeaderData.isInputDefinitionComplete())
            return false;
        
        return true;
    }
    
    public String toString() {
        return this.name.getLocalPart();
    }
}

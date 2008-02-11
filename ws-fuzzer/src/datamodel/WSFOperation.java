/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;

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
    private String bindingSOAPUse;
    
    private QName headerMessageQName;
    private String headerMessagePart;
    private String headerMessageUse;
    private WSFDataElement headerData;
    
    private String bindingHttpLocation;         // if httpbinding
    
    private QName name;
    private String document;
    private String mep;
    
    private QName inMessageQName;
    private WSFDataElement inData;
    private QName outMessageQName;
    private WSFDataElement outData;
    
    
    public WSFOperation(WSFPort port){
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
        this.supported = supported;
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

    public String getBindingSOAPUse() {
        return bindingSOAPUse;
    }

    public void setBindingSOAPUse(String bindingSOAPUse) {
        this.bindingSOAPUse = bindingSOAPUse;
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

    public WSFDataElement getInData() {
        return inData;
    }

    public void setInData(WSFDataElement inData) {
        this.inData = inData;
    }

    public WSFDataElement getOutData() {
        return outData;
    }

    public void setOutData(WSFDataElement outData) {
        this.outData = outData;
    }

    public QName getInMessageQName() {
        return inMessageQName;
    }

    public void setInMessageQName(QName inMessageQName) {
        this.inMessageQName = inMessageQName;
    }

    public QName getOutMessageQName() {
        return outMessageQName;
    }

    public void setOutMessageQName(QName outMessageQName) {
        this.outMessageQName = outMessageQName;
    }

    public QName getHeaderMessageQName() {
        return headerMessageQName;
    }

    public void setHeaderMessageQName(QName headerMessageQName) {
        this.headerMessageQName = headerMessageQName;
    }

    public String getHeaderMessagePart() {
        return headerMessagePart;
    }

    public void setHeaderMessagePart(String headerMessagePart) {
        this.headerMessagePart = headerMessagePart;
    }

    public String getHeaderMessageUse() {
        return headerMessageUse;
    }

    public void setHeaderMessageUse(String headerMessageUse) {
        this.headerMessageUse = headerMessageUse;
    }

    public WSFDataElement getHeaderData() {
        return headerData;
    }

    public void setHeaderData(WSFDataElement headerData) {
        this.headerData = headerData;
    }
    
    public void print(){
        System.out.println("----------- Operation -----------");
        System.out.println("QName: " + this.name);
        System.out.println("MEP:   " + this.mep);
        System.out.println("inMsg: " + this.inMessageQName);
        WSFDataElement.print(inData);
        System.out.println("outMsg:" + this.outMessageQName);
        WSFDataElement.print(outData);
        System.out.println();
    }
}

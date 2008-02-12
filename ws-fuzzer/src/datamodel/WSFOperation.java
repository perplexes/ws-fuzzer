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

    private String bindingHttpLocation;         // if httpbinding
    
    private QName name;
    private String document;
    private String mep;
    
    private QName requestMessageQName;
    private WSFDataElement requestData;
    private String bindingRequestSOAPUse;
    
    private QName headerRequestMessageQName;
    private String headerRequestMessagePart;
    private String headerRequestMessageUse;
    private WSFDataElement headerRequestData;
    
    private QName responseMessageQName;
    private WSFDataElement responseData;
    private String bindingResponseSOAPUse;
    
    private QName headerResponseMessageQName;
    private String headerResponseMessagePart;
    private String headerResponseMessageUse;
    private WSFDataElement headerResponseData;
    
    
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

    public QName getHeaderRequestMessageQName() {
        return headerRequestMessageQName;
    }

    public void setHeaderRequestMessageQName(QName headerRequestMessageQName) {
        this.headerRequestMessageQName = headerRequestMessageQName;
    }

    public String getHeaderRequestMessagePart() {
        return headerRequestMessagePart;
    }

    public void setHeaderRequestMessagePart(String headerRequestMessagePart) {
        this.headerRequestMessagePart = headerRequestMessagePart;
    }

    public String getHeaderRequestMessageUse() {
        return headerRequestMessageUse;
    }

    public void setHeaderRequestMessageUse(String headerRequestMessageUse) {
        this.headerRequestMessageUse = headerRequestMessageUse;
    }

    public WSFDataElement getHeaderRequestData() {
        return headerRequestData;
    }

    public void setHeaderRequestData(WSFDataElement headerRequestData) {
        this.headerRequestData = headerRequestData;
    }
    
    
    public QName getHeaderResponseMessageQName() {
        return headerResponseMessageQName;
    }

    public void setHeaderResponseMessageQName(QName headerResponseMessageQName) {
        this.headerResponseMessageQName = headerResponseMessageQName;
    }

    public String getHeaderResponseMessagePart() {
        return headerResponseMessagePart;
    }

    public void setHeaderResponseMessagePart(String headerResponseMessagePart) {
        this.headerResponseMessagePart = headerResponseMessagePart;
    }

    public String getHeaderResponseMessageUse() {
        return headerResponseMessageUse;
    }

    public void setHeaderResponseMessageUse(String headerResponseMessageUse) {
        this.headerResponseMessageUse = headerResponseMessageUse;
    }

    public WSFDataElement getHeaderResponseData() {
        return headerResponseData;
    }

    public void setHeaderResponseData(WSFDataElement headerResponseData) {
        this.headerResponseData = headerResponseData;
    }
    
    public void print(){
        System.out.println("----------- Operation -----------");
        System.out.println("QName: " + this.name);
        System.out.println("MEP:   " + this.mep);
        System.out.println("inMsg: " + this.requestMessageQName);
        WSFDataElement.print(requestData);
        System.out.println("outMsg:" + this.responseMessageQName);
        WSFDataElement.print(responseData);
        System.out.println();
    }
}

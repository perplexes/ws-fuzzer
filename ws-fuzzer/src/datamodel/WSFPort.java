/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import java.util.ArrayList;
import javax.xml.namespace.QName;

/**
 *
 * @author chang
 */
public class WSFPort {

    private WSFService service;
    
    private String name;
    
    private boolean supported;
    
    private String portAddressType;
    private String portLocation;
    
    private String bindingType;                 // soapBinding?
    private String bindingSOAPTransport;        // if soapbinding
    private String bindingHTTPVerb;             // if httpbinding
    
    private String bindingSOAPStyle;
    
    private ArrayList<WSFOperation> operations;
    
    public WSFPort(WSFService service){
        this.service = service;
        operations = new ArrayList<WSFOperation>();
    }

    public WSFService getService() {
        return service;
    }

    public void setService(WSFService service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortAddressType() {
        return portAddressType;
    }

    public void setPortAddressType(String portAddressType) {
        this.portAddressType = portAddressType;
    }

    public String getPortLocation() {
        return portLocation;
    }

    public void setPortLocation(String portLocation) {
        this.portLocation = portLocation;
    }

    public String getBindingType() {
        return bindingType;
    }

    public void setBindingType(String bindingType) {
        this.bindingType = bindingType;
    }

    public String getBindingSOAPTransport() {
        return bindingSOAPTransport;
    }

    public void setBindingSOAPTransport(String bindingSOAPTransport) {
        this.bindingSOAPTransport = bindingSOAPTransport;
    }

    public String getBindingHTTPVerb() {
        return bindingHTTPVerb;
    }

    public void setBindingHTTPVerb(String bindingHTTPVerb) {
        this.bindingHTTPVerb = bindingHTTPVerb;
    }

    public ArrayList<WSFOperation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<WSFOperation> operations) {
        this.operations = operations;
    }
    
    public void addOperation(WSFOperation op){
        this.operations.add(op);
    }

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        this.supported = supported;
    }

    public String getBindingSOAPStyle() {
        return bindingSOAPStyle;
    }

    public void setBindingSOAPStyle(String bindingSOAPStyle) {
        this.bindingSOAPStyle = bindingSOAPStyle;
    }
    
    public void print(){
        System.out.println("-------- Port ----------");
        System.out.println("name:        " + name);
        System.out.println("bindingType: " + this.bindingType);
        System.out.println("operation#:  " + this.operations.size());
        System.out.println();
    }
    
}

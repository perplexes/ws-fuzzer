/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import utils.StringUtils;
import utils.XMLUtils;

/**
 *
 * @author chang
 */
public class WSFResult {
    
    public static int PENDING = 0;
    public static int NOTFINISHED = 1;
    public static int FINISHED = 2;
    public static int PLACEHOLDER = 3;
    
    private int status;
    
    private int inputIndex;
    
    private String outRaw;
    private String inRaw;
    
    // in ms
    private long time;
    
    private WSFResult(){
    }
    
    public WSFResult(int inputIndex){
        this.status = WSFResult.PENDING;
        this.inputIndex = inputIndex;
    }
    
    public int getStatus(){
        return this.status;
    }
    
    public void setStatus(int status){
        this.status = status;
    }

    public String getOutRaw() {
        return outRaw;
    }

    public void setOutRaw(String outRaw) {
        this.outRaw = outRaw;
    }

    public String getInRaw() {
        return inRaw;
    }

    public void setInRaw(String inRaw) {
//        this.inRaw = inRaw;
        this.inRaw = inRaw;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        if(this.time == 0)
            this.time = time;
        else
            this.time = time - this.time;
    }

    public int getInputIndex() {
        return inputIndex;
    }

    public void setInputIndex(int inputIndex) {
        this.inputIndex = inputIndex;
    }
    
    public void clear(){
        this.inRaw = null;
        this.outRaw = null;
        this.time = 0;
    }
    
    @Override
    public WSFResult clone(){
        WSFResult result = new WSFResult();
        result.setStatus(status);
        result.setInRaw(inRaw);
        result.setOutRaw(outRaw);
        result.setInputIndex(inputIndex);
        result.setTime(time);
        return result;
    }
    
    private String prettifyMessage(String msg){
        try {
            int index = msg.indexOf("<?xml");

            if (index == -1) {
                return msg;
            }
            
            StringBuffer msgBuffer = new StringBuffer(msg);
            String xmlString = msgBuffer.substring(index);

            return new String(msgBuffer.replace(index, msgBuffer.length(), XMLUtils.prettify(xmlString)));
            
        } catch (XMLStreamException ex) {
        } catch (Exception ex) {
        }
        
        return msg;
    }
    
    public void prettify(){
        if(this.inRaw != null){
            this.inRaw = prettifyMessage(this.inRaw);
        }else{
            this.inRaw = "";
        }
        
        if(this.outRaw != null){
            this.outRaw = prettifyMessage(this.outRaw);
        }else{
            this.outRaw = "";
        }
    }
    
    public OMElement serializeToOMElement(OMElement parent){
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement result = omDOMFactory.createOMElement(new QName("result"), parent);
        
        OMElement omElement1 = null;
        
        // status
        omElement1 = omDOMFactory.createOMElement(new QName("status"), result);
        omElement1.setText(""+this.status);
        
        // index
        omElement1 = omDOMFactory.createOMElement(new QName("inputindex"), result);
        omElement1.setText(""+this.inputIndex);
        
        // outraw
        omElement1 = omDOMFactory.createOMElement(new QName("outraw"), result);
        if(outRaw != null)
            omElement1.setText(StringUtils.escapeForXML(outRaw));
        
        // inraw
        omElement1 = omDOMFactory.createOMElement(new QName("inraw"), result);
        if(inRaw != null)
            omElement1.setText(StringUtils.escapeForXML(inRaw));
        
        // time
        omElement1 = omDOMFactory.createOMElement(new QName("time"), result);
        omElement1.setText(""+time);
        
        return result;
    }
    
    public static WSFResult deserializeFromOMElement(OMElement omElement){
        WSFResult result = new WSFResult();
        
        OMElement element = null;
        
        // status
        element = omElement.getFirstChildWithName(new QName("status"));
        result.status = Integer.parseInt(element.getText());
        
        // index
        element = omElement.getFirstChildWithName(new QName("inputindex"));
        result.inputIndex = Integer.parseInt(element.getText());
        
        // outRaw
        element = omElement.getFirstChildWithName(new QName("outraw"));
        if(element != null)
            result.outRaw = StringUtils.unescapeForXML(element.getText());
        
        // inRaw
        element = omElement.getFirstChildWithName(new QName("inraw"));
        if(element != null)
            result.inRaw = StringUtils.unescapeForXML(element.getText());
        
        // time
        element = omElement.getFirstChildWithName(new QName("time"));
        result.time = Long.parseLong(element.getText());
        
        return result;
    }
}

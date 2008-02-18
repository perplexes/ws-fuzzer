/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.factory.OMDOMFactory;
import utils.StringUtils;

/**
 *
 * @author chang
 */
public class WSFResult {
    private int inputIndex;
    
    private String outRaw;
    private String inRaw;
    
    // in ms
    private long time;
    

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
        result.setInRaw(inRaw);
        result.setOutRaw(outRaw);
        result.setInputIndex(inputIndex);
        result.setTime(time);
        return result;
    }
    
    public OMElement serializeToOMElement(OMElement parent){
        OMDOMFactory omDOMFactory = new OMDOMFactory();
        
        OMElement result = omDOMFactory.createOMElement(new QName("result"), parent);
        
        OMElement omElement1 = null;
        
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

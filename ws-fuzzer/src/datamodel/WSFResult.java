/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package datamodel;

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
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import datamodel.WSFResult;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpConnection;

/**
 *
 * @author chang
 */
public class Hook {
    private HashMap<Long, WSFResult> buffer;
    private HashMap<Long, HttpConnection> connections;

    public Hook(){
        buffer = new HashMap<Long, WSFResult>();
        connections = new HashMap<Long, HttpConnection>();
    }
    
    public WSFResult getResult() {
        Long key = new Long(Thread.currentThread().getId());
        
        if(!buffer.containsKey(key))
            buffer.put(key, new WSFResult());
        
        return buffer.get(key);
    }

    public void setResult(WSFResult result) {
        Long key = new Long(Thread.currentThread().getId());
        buffer.put(key, result);
    }
    
    public boolean hasResult(){
        Long key = new Long(Thread.currentThread().getId());
        return buffer.containsKey(key);
    }
    
    public HttpConnection getHttpConnection(){
        Long key = new Long(Thread.currentThread().getId());
        return this.connections.get(key);
    }
    
    public void setHttpConnection(HttpConnection connection){
        
        Long key = new Long(Thread.currentThread().getId());
        this.connections.put(key, connection);
    }

}

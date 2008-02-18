/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import datamodel.WSFResult;
import java.util.ArrayList;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;

/**
 *
 * @author chang
 */
public class AxisCallbackImpl implements AxisCallback {

    private Hook hook;
    private ArrayList<WSFResult> results;
    private boolean isResultSaved;
    private int inputIndex;
    private Thread thread;
//    private ServiceClient serviceClient;
    
    AxisCallbackImpl(Hook hook, ArrayList<WSFResult> results, int inputIndex, Thread thread) {
        this.hook = hook;
        this.results = results;
        this.isResultSaved = false;
        this.thread = thread;
        this.inputIndex = inputIndex;
    }

    public void onMessage(MessageContext arg0) {
        System.out.println("AxisCallback -- onMessage: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
        saveResult();


//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onFault(MessageContext arg0) {
        System.out.println("AxisCallback -- onFault: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
        saveResult();

//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onError(Exception arg0) {
        System.out.println("AxisCallback -- onError: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName() + " || exception: " + arg0.getClass() + " ++ " + arg0.getMessage());
        saveResult();

        
//        throw new UnsupportedOperationException("Not supported yet." + );
        arg0.printStackTrace();
    }

    public void onComplete() {
        System.out.println("AxisCallback -- onComplete: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
        saveResult();
        
//        throw new UnsupportedOperationException("Not supported yet.");


    }

    private void saveResult() {
        
        hook.getHttpConnection().releaseConnection();

            if (!this.isResultSaved) {
                WSFResult result = hook.getResult();

                if (result.getOutRaw() != null) {
                    
                    result.setInputIndex(inputIndex);
                    
                    WSFResult resultCopy = result.clone();
                    
                    synchronized(results){
                        results.add(resultCopy);
                    }
                    System.out.println("AxisCallback -- saveResult: " + Thread.currentThread().getId() + " -- " + Thread.currentThread().getName());
                    thread.interrupt();
                }

                result.clear();
                this.isResultSaved = true;
                
            }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import datamodel.WSFResult;
import java.util.ArrayList;
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;

/**
 *
 * @author chang
 */
public class AxisCallbackImpl implements AxisCallback {

    private static Logger logger = Logger.getLogger(AxisCallbackImpl.class);
    private Hook hook;
    private ArrayList<WSFResult> results;
    private boolean isResultSaved;
    private int inputIndex;
    private Thread thread;

    AxisCallbackImpl(Hook hook, ArrayList<WSFResult> results, int inputIndex, Thread thread) {
        this.hook = hook;
        this.results = results;
        this.isResultSaved = false;
        this.thread = thread;
        this.inputIndex = inputIndex;
    }

    public void onMessage(MessageContext arg0) {
        logger.debug("Axis2: onMessage");
        saveResult();
    }

    public void onFault(MessageContext arg0) {
        logger.debug("Axis2: onFault");
        saveResult();
    }

    public void onError(Exception arg0) {
        logger.error("Axis2: onError: " + arg0 + "\n failed index: " + this.inputIndex);
        saveResult();
    }

    public void onComplete() {
        logger.debug("Axis2: onComplete");
        saveResult();
    }

    private void saveResult() {

        hook.getHttpConnection().releaseConnection();

        if (!this.isResultSaved) {
            WSFResult result = hook.getResult();

            if (result.getOutRaw() != null) {

                result.setInputIndex(inputIndex);

                synchronized (results) {
                    System.out.println("------------->: " + inputIndex);
                    results.add(result);
                }
                logger.info("Receive Response: index " + result.getInputIndex() + " in " + result.getTime() + " ms");
                thread.interrupt();
            }

            hook.setResult(new WSFResult(inputIndex));
            this.isResultSaved = true;

        }
    }
}

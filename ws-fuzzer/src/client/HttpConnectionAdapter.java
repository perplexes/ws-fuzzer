/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import datamodel.WSFResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;

/**
 *
 * @author chang
 */
public class HttpConnectionAdapter extends HttpConnection {
// the wrapped connection
    private HttpConnection wrappedConnection;
    private StringBuffer out;
    private StringBuffer in;
    
    private Hook hook;

    private boolean isReleased;
    /**
     * Creates a new HttpConnectionAdapter.
     * 
     * @param connection
     *            the connection to be wrapped
     */
    public HttpConnectionAdapter(HttpConnection connection, Hook hook) {
        super(connection.getHost(), connection.getPort(), connection.getProtocol());
        this.wrappedConnection = connection;

        if (this.out == null) {
            this.out = new StringBuffer();
        }

        if (this.in == null) {
            this.in = new StringBuffer();
        }
        
        this.hook = hook;
        this.isReleased = true;
    }

    /**
     * Tests if the wrapped connection is still available.
     * 
     * @return boolean
     */
    // protected boolean hasConnection() {
    // return wrappedConnection != null;
    // }
    /**
     * @return HttpConnection
     */
//	HttpConnection getWrappedConnection() {
//		return wrappedConnection;
//	}

    // TODO
    public void close() {
        wrappedConnection.close();
    }

    public InetAddress getLocalAddress() {
        return wrappedConnection.getLocalAddress();
    }

    /**
     * @deprecated
     */
    public boolean isStaleCheckingEnabled() {
        return wrappedConnection.isStaleCheckingEnabled();
    }

    public void setLocalAddress(InetAddress localAddress) {
        wrappedConnection.setLocalAddress(localAddress);
    }

    /**
     * @deprecated
     */
    public void setStaleCheckingEnabled(boolean staleCheckEnabled) {
        wrappedConnection.setStaleCheckingEnabled(staleCheckEnabled);
    }

    public String getHost() {
        return wrappedConnection.getHost();
    }

    public HttpConnectionManager getHttpConnectionManager() {
        return wrappedConnection.getHttpConnectionManager();
    }

    public InputStream getLastResponseInputStream() {
        return wrappedConnection.getLastResponseInputStream();
    }

    public int getPort() {
        return wrappedConnection.getPort();
    }

    public Protocol getProtocol() {
        return wrappedConnection.getProtocol();
    }

    public String getProxyHost() {
        return wrappedConnection.getProxyHost();
    }

    public int getProxyPort() {
        return wrappedConnection.getProxyPort();
    }

    public OutputStream getRequestOutputStream() throws IOException,
            IllegalStateException {
        // TODO:
        return new OutputStreamAdapter(wrappedConnection.getRequestOutputStream());
    }

    public InputStream getResponseInputStream() throws IOException,
            IllegalStateException {
        // TODO

        return new InputStreamAdapter(wrappedConnection.getResponseInputStream());
    }

    public boolean isOpen() {
        return wrappedConnection.isOpen();
    }

    public boolean closeIfStale() throws IOException {
        return wrappedConnection.closeIfStale();
    }

    public boolean isProxied() {
        return wrappedConnection.isProxied();
    }

    public boolean isResponseAvailable() throws IOException {
        return wrappedConnection.isResponseAvailable();
    }

    public boolean isResponseAvailable(int timeout) throws IOException {
        return wrappedConnection.isResponseAvailable(timeout);
    }

    public boolean isSecure() {
        return wrappedConnection.isSecure();
    }

    public boolean isTransparent() {
        return wrappedConnection.isTransparent();
    }

    public void open() throws IOException {
        //TODO:
        if (this.in == null) {
            this.in = new StringBuffer();
        }

        if (this.out == null) {
            this.out = new StringBuffer();
        }

        wrappedConnection.open();
    }

    /**
     * @deprecated
     */
    public void print(String data) throws IOException,
            IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 0");
        wrappedConnection.print(data);
    }

    public void printLine() throws IOException, IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 1");
        wrappedConnection.printLine();
    }

    /**
     * @deprecated
     */
    public void printLine(String data) throws IOException,
            IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 2");
        wrappedConnection.printLine(data);
    }

    /**
     * @deprecated
     */
    public String readLine() throws IOException, IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 3");
        return wrappedConnection.readLine();
    }

    public String readLine(String charset) throws IOException,
            IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 4");
//        System.out.println("#####################| " + Thread.currentThread().getId() + " : " +Thread.currentThread().getName());

        String str = this.wrappedConnection.readLine(charset);

        this.in.append(str);
        return str;
    }

    // TODO
    @Override
    public void releaseConnection() {
        
        
        WSFResult result = hook.getResult();
        result.setTime(System.currentTimeMillis());
        
        if(this.in != null){
            String inRaw = "";
            if(this.in.indexOf("<?xml") != -1){
                inRaw = new String(this.in.insert(this.in.indexOf("<?xml"), "\n\n"));
            }else{
                inRaw = new String(this.in);
            }
                String outRaw = new String(this.out);

            result.setOutRaw(outRaw);
            result.setInRaw(inRaw);
            
        }
        
        this.in = null;
        this.out = null;
        this.isReleased = true;
        
        System.out.println("HttpConnectionAdapter: connection released: " + Thread.currentThread().getName() + " -- " + Thread.currentThread().getId());
        
        wrappedConnection.releaseConnection();

    }

    /**
     * @deprecated
     */
    public void setConnectionTimeout(int timeout) {
        wrappedConnection.setConnectionTimeout(timeout);
    }

    public void setHost(String host) throws IllegalStateException {
        wrappedConnection.setHost(host);
    }

    public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
        wrappedConnection.setHttpConnectionManager(httpConnectionManager);
    }

    public void setLastResponseInputStream(InputStream inStream) {
        wrappedConnection.setLastResponseInputStream(inStream);
    }

    public void setPort(int port) throws IllegalStateException {
        wrappedConnection.setPort(port);
    }

    public void setProtocol(Protocol protocol) {
        wrappedConnection.setProtocol(protocol);
    }

    public void setProxyHost(String host) throws IllegalStateException {
        wrappedConnection.setProxyHost(host);
    }

    public void setProxyPort(int port) throws IllegalStateException {
        wrappedConnection.setProxyPort(port);
    }

    /**
     * @deprecated
     */
    public void setSoTimeout(int timeout) throws SocketException,
            IllegalStateException {
        wrappedConnection.setSoTimeout(timeout);
    }

    /**
     * @deprecated
     */
    public void shutdownOutput() {
        wrappedConnection.shutdownOutput();
    }

    public void tunnelCreated() throws IllegalStateException, IOException {
        wrappedConnection.tunnelCreated();
    }

    public void write(byte[] data, int offset, int length)
            throws IOException, IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 5");
        wrappedConnection.write(data, offset, length);
    }

    public void write(byte[] data) throws IOException,
            IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 6");
        wrappedConnection.write(data);
    }

    public void writeLine() throws IOException, IllegalStateException {

//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 7");
//        System.out.println("#####################| " + Thread.currentThread().getId() + " : " +Thread.currentThread().getName());
        this.out.append("\n");
        wrappedConnection.writeLine();
    }

    public void writeLine(byte[] data) throws IOException,
            IllegalStateException {
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 8");
        wrappedConnection.writeLine(data);
    }

    public void flushRequestOutputStream() throws IOException {
        wrappedConnection.flushRequestOutputStream();
    }

    /**
     * @deprecated
     */
    public int getSoTimeout() throws SocketException {
        return wrappedConnection.getSoTimeout();
    }

    /**
     * @deprecated
     */
    public String getVirtualHost() {
        return wrappedConnection.getVirtualHost();
    }

    /**
     * @deprecated
     */
    public void setVirtualHost(String host) throws IllegalStateException {
        wrappedConnection.setVirtualHost(host);
    }

    public int getSendBufferSize() throws SocketException {
        return wrappedConnection.getSendBufferSize();
    }

    /**
     * @deprecated
     */
    public void setSendBufferSize(int sendBufferSize)
            throws SocketException {
        wrappedConnection.setSendBufferSize(sendBufferSize);
    }

    public HttpConnectionParams getParams() {
        return wrappedConnection.getParams();
    }

    public void setParams(final HttpConnectionParams params) {
        wrappedConnection.setParams(params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.httpclient.HttpConnection#print(java.lang.String,
     *      java.lang.String)
     */
    public void print(String data, String charset) throws IOException,
            IllegalStateException {

        this.out.append(data);
        
        if(isReleased){
            WSFResult result = this.hook.getResult();
            result.setTime(System.currentTimeMillis());
            isReleased = false;
        }
        
//        System.out.println(Thread.currentThread().getId());
//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 9");
//        System.out.println("#####################| " + Thread.currentThread().getId() + " : " +Thread.currentThread().getName());
        wrappedConnection.print(data, charset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.httpclient.HttpConnection#printLine(java.lang.String,
     *      java.lang.String)
     */
    public void printLine(String data, String charset) throws IOException,
            IllegalStateException {

//        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$ --> 10");
        wrappedConnection.printLine(data, charset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.httpclient.HttpConnection#setSocketTimeout(int)
     */
    public void setSocketTimeout(int timeout) throws SocketException,
            IllegalStateException {
        wrappedConnection.setSocketTimeout(timeout);
    }

    private class OutputStreamAdapter extends OutputStream {

        private OutputStream wrappedOutputStream;

        public OutputStreamAdapter(OutputStream os) {
            this.wrappedOutputStream = os;
        }

        public void close() throws IOException {
            this.wrappedOutputStream.close();
        }

        public void flush() throws IOException {
            this.wrappedOutputStream.flush();
        }

        public void write(byte[] b) throws IOException {
            out.append(new String(b));
            this.wrappedOutputStream.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.append(new String(b, off, len));
            this.wrappedOutputStream.write(b, off, len);
        }

        public void write(int b) throws IOException {
            // TODO
            this.wrappedOutputStream.write(b);
        }
    }

    private class InputStreamAdapter extends InputStream {

        private InputStream wrappedInputStream;

        public InputStreamAdapter(InputStream is) {
            this.wrappedInputStream = is;
        }

        public int available() throws IOException {
            return this.wrappedInputStream.available();
        }

        public int read() throws IOException {
            // TODO
            return this.wrappedInputStream.read();
        }

        public int read(byte[] b) throws IOException {
            int leng = this.wrappedInputStream.read(b);
            in.append(new String(b));
            return leng;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int leng = this.wrappedInputStream.read(b, off, len);
            in.append(new String(b, off, len));
            return leng;
        }

        public long skip(long n) throws IOException {
            return this.wrappedInputStream.skip(n);
        }

        public void close() throws IOException {
            this.wrappedInputStream.close();
        }

        public void mark(int readlimit) {
            this.wrappedInputStream.mark(readlimit);
        }

        public void reset() throws IOException {
            this.wrappedInputStream.reset();
        }

        public boolean markSupported() {
            return this.wrappedInputStream.markSupported();
        }
    }
}

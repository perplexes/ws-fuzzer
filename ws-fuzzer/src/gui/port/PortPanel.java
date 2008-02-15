/*
 * PortPanel.java
 *
 * Created on February 15, 2008, 12:27 AM
 */

package gui.port;

import datamodel.WSFPort;
import java.awt.Color;
import javax.swing.SwingConstants;

/**
 *
 * @author  chang
 */
public class PortPanel extends javax.swing.JPanel {
    private WSFPort port;
    /** Creates new form PortPanel */
    public PortPanel(WSFPort port) {
        this.port = port;
        initComponents();
        showPort();
    }
    
    public void setPort(WSFPort port){
        this.port = port;
        showPort();
    }
    
    private void showPort(){
        
        if(port.isSupported()){
            supportedLabel.setBackground(new Color(0,170,110));
            supportedLabel.setText("Supported");
        }else{
            supportedLabel.setBackground(new Color(200,50,0));
            supportedLabel.setText("Not Supported");
        }
        
        if(port.getBindingType().equalsIgnoreCase("soapbinding")){
            showSOAPBindingPanel();
            return;
        }
        if(port.getBindingType().equalsIgnoreCase("httpbinding")){
            showHTTPBindingPanel();
            return;
        }
    }
    
    private void showHTTPBindingPanel(){
        
        if(httpBindingPanel==null){
            httpBindingPanel = new HTTPBindingPanel(port);
        }else{
            httpBindingPanel.setPort(port);
        }
        
        if(httpBindingPanel.getParent() != displayPanel){
            displayPanel.removeAll();
            displayPanel.add(httpBindingPanel);
        }
    }
    
    private void showSOAPBindingPanel(){
        
        if(soapBindingPanel==null){
            soapBindingPanel = new SOAPBindingPanel(port);
        }else{
            soapBindingPanel.setPort(port);
        }
        
        if(soapBindingPanel.getParent() != displayPanel){
            displayPanel.removeAll();
            displayPanel.add(soapBindingPanel);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayPanel = new javax.swing.JPanel();
        supportedLabel = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        displayPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        displayPanel.setName("displayPanel"); // NOI18N
        displayPanel.setLayout(new java.awt.GridLayout(0, 1));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(PortPanel.class);
        supportedLabel.setBackground(resourceMap.getColor("supportedLabel.background")); // NOI18N
        supportedLabel.setFont(resourceMap.getFont("supportedLabel.font")); // NOI18N
        supportedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        supportedLabel.setText(resourceMap.getString("supportedLabel.text")); // NOI18N
        supportedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        supportedLabel.setName("supportedLabel"); // NOI18N
        supportedLabel.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(displayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
                    .addComponent(supportedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(supportedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel displayPanel;
    private javax.swing.JLabel supportedLabel;
    // End of variables declaration//GEN-END:variables
    
    private HTTPBindingPanel httpBindingPanel;
    private SOAPBindingPanel soapBindingPanel;
}

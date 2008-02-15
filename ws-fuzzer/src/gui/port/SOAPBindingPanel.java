/*
 * SOAPBindingPanel.java
 *
 * Created on February 15, 2008, 12:56 AM
 */

package gui.port;

import datamodel.WSFPort;

/**
 *
 * @author  chang
 */
public class SOAPBindingPanel extends javax.swing.JPanel {
    
    private WSFPort port;
    
    /** Creates new form SOAPBindingPanel */
    public SOAPBindingPanel(WSFPort port) {
        this.port = port;
        initComponents();
        showPort();
    }
    
    public void setPort(WSFPort port){
        this.port = port;
        showPort();
    }
    private void showPort(){
        bindingTypeLabel.setText(port.getBindingType());
        serviceAddressLabel.setText(port.getPortLocation());
        transportProtocolLabel.setText(port.getBindingSOAPTransport());
        soapStyleLabel.setText(port.getBindingSOAPStyle());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        soapStyleLabel = new javax.swing.JLabel();
        serviceAddressLabel = new javax.swing.JLabel();
        bindingTypeLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        transportProtocolLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(SOAPBindingPanel.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        soapStyleLabel.setText(resourceMap.getString("soapStyleLabel.text")); // NOI18N
        soapStyleLabel.setName("soapStyleLabel"); // NOI18N

        serviceAddressLabel.setText(resourceMap.getString("serviceAddressLabel.text")); // NOI18N
        serviceAddressLabel.setName("serviceAddressLabel"); // NOI18N

        bindingTypeLabel.setText(resourceMap.getString("bindingTypeLabel.text")); // NOI18N
        bindingTypeLabel.setName("bindingTypeLabel"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        transportProtocolLabel.setText(resourceMap.getString("transportProtocolLabel.text")); // NOI18N
        transportProtocolLabel.setName("transportProtocolLabel"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(42, 42, 42)
                        .addComponent(bindingTypeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(20, 20, 20)
                        .addComponent(serviceAddressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transportProtocolLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(51, 51, 51)
                        .addComponent(soapStyleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(bindingTypeLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(serviceAddressLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(transportProtocolLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(soapStyleLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bindingTypeLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel serviceAddressLabel;
    private javax.swing.JLabel soapStyleLabel;
    private javax.swing.JLabel transportProtocolLabel;
    // End of variables declaration//GEN-END:variables
    
}

/*
 * OptionsDialog.java
 *
 * Created on February 13, 2008, 2:59 AM
 */

package gui.options;

import datamodel.WSFConfiguration;
import gui.WSFApplication;
import java.io.IOException;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.application.Action;

/**
 *
 * @author  chang
 */
public class OptionsDialog extends javax.swing.JDialog {
    
    /** Creates new form OptionsDialog */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        optionsList = new javax.swing.JList();
        displayPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        applayButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(OptionsDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane1.border.title"))); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        optionsList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Network", "Dictionaries", "General" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        optionsList.setName("optionsList"); // NOI18N
        optionsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                optionsListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(optionsList);

        displayPanel.setName("displayPanel"); // NOI18N
        displayPanel.setLayout(new java.awt.GridLayout(0, 1));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getActionMap(OptionsDialog.class, this);
        okButton.setAction(actionMap.get("okButtonPressed")); // NOI18N
        okButton.setText(resourceMap.getString("okButton.text")); // NOI18N
        okButton.setName("okButton"); // NOI18N

        applayButton.setAction(actionMap.get("applyChanges")); // NOI18N
        applayButton.setText(resourceMap.getString("applayButton.text")); // NOI18N
        applayButton.setName("applayButton"); // NOI18N

        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(applayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(okButton)
                            .addComponent(applayButton)
                            .addComponent(cancelButton)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void optionsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_optionsListValueChanged
        // TODO add your handling code here:
        switch(optionsList.getSelectedIndex()){
            case 0:         // network options
                showNetworkOptions();
                break;

            case 1:         // dictionaries options
                showDictionariesOptions();
                break;
            
            case 2:         // general options
                showGeneralOptions();
                break;

            default:        // do nothing
        }
    }//GEN-LAST:event_optionsListValueChanged

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();
}//GEN-LAST:event_cancelButtonActionPerformed
    
    @Action
    public void okButtonPressed() throws IOException, XMLStreamException, Exception{
        applyChanges();
        this.dispose();
    }
    
    @Action
    public void applyChanges() throws IOException, XMLStreamException, Exception{
        
        if(networkPanel!=null)
            networkPanel.saveChanges();
        
        if(dictionariesPanel!=null)
            dictionariesPanel.saveChanges();
        
        if(generalPanel!=null)
            generalPanel.saveChanges();
        
        WSFConfiguration config = WSFApplication.getApplication().getWSFConfiguration();
        config.saveChanges();
    }
    
    private void showNetworkOptions(){
        
        if(networkPanel == null){
            networkPanel = new NetworkPanel();
        }
        
        if(displayPanel.isAncestorOf(networkPanel))
            return;
        
        displayPanel.removeAll();
        displayPanel.add(networkPanel);
        networkPanel.revalidate();
        networkPanel.repaint();
    }
    
    private void showDictionariesOptions(){
        
        if(dictionariesPanel == null){
            dictionariesPanel = new DictionariesPanel();
        }
        
        if(displayPanel.isAncestorOf(dictionariesPanel))
            return;
        
        displayPanel.removeAll();
        displayPanel.add(dictionariesPanel);
        
        dictionariesPanel.revalidate();
        dictionariesPanel.repaint();
    }
    
    private void showGeneralOptions(){
        
        if(generalPanel == null){
            generalPanel = new GeneralPanel(this);
        }
        
        if(displayPanel.isAncestorOf(generalPanel))
            return;
        
        displayPanel.removeAll();
        displayPanel.add(generalPanel);
        
        generalPanel.revalidate();
        generalPanel.repaint();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applayButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JList optionsList;
    // End of variables declaration//GEN-END:variables
    
    private NetworkPanel networkPanel;
    private DictionariesPanel dictionariesPanel;
    private GeneralPanel generalPanel;
}

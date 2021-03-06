/*
 * MakeTestCasePanel.java
 *
 * Created on February 12, 2008, 12:45 AM
 */
package gui.operation;

import datamodel.WSFConfiguration;
import datamodel.WSFDataElement;
import datamodel.WSFDictionaryInfo;
import datamodel.WSFInputSource;
import datamodel.WSFOperation;
import datamodel.WSFTestCase;
import gui.WSFApplication;
import gui.WSFApplicationView;
import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import utils.JTreeUtils;

/**
 *
 * @author  chang
 */
public class MakeTestCasePanel extends javax.swing.JPanel implements PropertyChangeListener {

    private static Logger logger = Logger.getLogger(MakeTestCasePanel.class);
    

    private WSFOperation operation;
    private DefaultComboBoxModel dictionaryComboBoxModel;

    /** Creates new form MakeTestCasePanel */
    public MakeTestCasePanel(WSFOperation operation) {
        initComponents();
        this.operation = operation;

        postInit();

        showOperation();
    }

    private void postInit() {
        buttonGroup1.add(fixedValueRadioButton);
        buttonGroup1.add(fromDictionaryRadioButton);
        fixedValueRadioButton.setSelected(true);

        setDictionaries();

        WSFConfiguration config = WSFApplication.getApplication().getWSFConfiguration();
        config.addPropertyChangeListener("dictionaries", this);

        requestMessageTree.setCellRenderer(new MyCellRenderer());
        
        previewTextPane.setEditorKit(new HTMLEditorKit() );
    }

    public void setOperation(WSFOperation operation) {
        this.operation = operation;
        showOperation();
    }

    private void showOperation() {
        
        DefaultMutableTreeNode requestTreeNode = operation.getRequestTreeNode();
        DefaultMutableTreeNode responseTreeNode = operation.getResponseTreeNode();

        DefaultTreeModel requestMessageTreeModel = new DefaultTreeModel(requestTreeNode);
        DefaultTreeModel responseMessageTreeModel = new DefaultTreeModel(responseTreeNode);

        requestMessageTree.setModel(requestMessageTreeModel);
        responseMessageTree.setModel(responseMessageTreeModel);

        JTreeUtils.expandAll(requestMessageTree, new TreePath(requestTreeNode.getPath()));
        JTreeUtils.expandAll(responseMessageTree, new TreePath(responseTreeNode.getPath()));

        disableInputdefinition();
        
        logger.info("Input Definition: Operation Name: " + operation.getName().getLocalPart());

        try{
            previewTextPane.setText(operation.getPreview());
        }catch(Exception ex){
            logger.warn("Could not get Preview");
            previewTextPane.setText("Could not get Preview");
        }
        
        tryEnableGenerateTestCaseButton();
    }
    
    @Action
    public void generateTestCase() {
        try {
            
            WSFTestCase testCase = operation.generateTestCase(testCaseNameTextField.getText());
            ((WSFApplicationView) WSFApplication.getApplication().getMainView()).addNewTestCaseToTree(testCase);
            testCase.getProject().saveTestCasesToFile();
            operation.clearInputdefinition();

            testCaseNameTextField.setEnabled(false);
            generateTestCaseButton.setEnabled(false);

            logger.info("Generate TestCase: \n Input Definition:");
            WSFDataElement header = operation.getRequestHeaderData();
            if (header != null) {
                logger.info("header:\n" + header.toOMElement(null, true).toStringWithConsume());
            }
            logger.info("body:\n" + operation.getRequestData().toOMElement(null, true).toStringWithConsume());

            testCaseNameTextField.setText("");

            disableInputdefinition();

            ((DefaultTreeModel) requestMessageTree.getModel()).reload();
            JTreeUtils.expandAll(requestMessageTree, null);
            
            operation.getPort().getService().getProject().setSaveTestCaseNeeded(true);
            
        } catch (Exception ex) {
            WSFApplication.showMessage("Exception: " + ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        requestMessageTreePopupMenu = new javax.swing.JPopupMenu();
        disableDataElementPopupMenuItem = new javax.swing.JMenuItem();
        enableDataElementPopupMenuItem = new javax.swing.JMenuItem();
        generateTestCaseButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        requestMessageTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        responseMessageTree = new javax.swing.JTree();
        inputDefinitionPanel = new javax.swing.JPanel();
        fixedValueRadioButton = new javax.swing.JRadioButton();
        fromDictionaryComboBox = new javax.swing.JComboBox();
        fixedValueTextField = new javax.swing.JTextField();
        fromDictionaryRadioButton = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        previewTextPane = new javax.swing.JTextPane();
        testCaseNameTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        requestMessageTreePopupMenu.setName("requestMessageTreePopupMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getActionMap(MakeTestCasePanel.class, this);
        disableDataElementPopupMenuItem.setAction(actionMap.get("disableDataElement")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(MakeTestCasePanel.class);
        disableDataElementPopupMenuItem.setText(resourceMap.getString("disableDataElementPopupMenuItem.text")); // NOI18N
        disableDataElementPopupMenuItem.setName("disableDataElementPopupMenuItem"); // NOI18N
        requestMessageTreePopupMenu.add(disableDataElementPopupMenuItem);

        enableDataElementPopupMenuItem.setAction(actionMap.get("enableDataElement")); // NOI18N
        enableDataElementPopupMenuItem.setText(resourceMap.getString("enableDataElementPopupMenuItem.text")); // NOI18N
        enableDataElementPopupMenuItem.setName("enableDataElementPopupMenuItem"); // NOI18N
        requestMessageTreePopupMenu.add(enableDataElementPopupMenuItem);

        setName("Form"); // NOI18N

        generateTestCaseButton.setAction(actionMap.get("generateTestCase")); // NOI18N
        generateTestCaseButton.setText(resourceMap.getString("generateTestCaseButton.text")); // NOI18N
        generateTestCaseButton.setName("generateTestCaseButton"); // NOI18N

        jSplitPane1.setDividerLocation(240);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane2.setDividerSize(4);
        jSplitPane2.setResizeWeight(0.5);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Request"));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(91, 215));

        requestMessageTree.setMaximumSize(new java.awt.Dimension(10000, 10000));
        requestMessageTree.setName("requestMessageTree"); // NOI18N
        requestMessageTree.setPreferredSize(new java.awt.Dimension(100, 57));
        requestMessageTree.setRootVisible(false);
        requestMessageTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                requestMessageTreeMouseClicked(evt);
            }
        });
        requestMessageTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                requestMessageTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(requestMessageTree);

        jSplitPane2.setLeftComponent(jScrollPane1);

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("Response"));
        jScrollPane2.setName("jScrollPane2"); // NOI18N
        jScrollPane2.setPreferredSize(new java.awt.Dimension(91, 215));

        responseMessageTree.setMaximumSize(new java.awt.Dimension(10000, 10000));
        responseMessageTree.setName("responseMessageTree"); // NOI18N
        responseMessageTree.setRootVisible(false);
        jScrollPane2.setViewportView(responseMessageTree);

        jSplitPane2.setRightComponent(jScrollPane2);

        inputDefinitionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Definition"));
        inputDefinitionPanel.setName("inputDefinitionPanel"); // NOI18N

        fixedValueRadioButton.setName("fixedValueRadioButton"); // NOI18N
        fixedValueRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fixedValueRadioButtonActionPerformed(evt);
            }
        });

        fromDictionaryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "From Dictionary", "Item 2", "Item 3", "Item 4" }));
        fromDictionaryComboBox.setToolTipText(resourceMap.getString("fromDictionaryComboBox.toolTipText")); // NOI18N
        fromDictionaryComboBox.setName("fromDictionaryComboBox"); // NOI18N
        fromDictionaryComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                fromDictionaryComboBoxItemStateChanged(evt);
            }
        });

        fixedValueTextField.setText(resourceMap.getString("fixedValueTextField.text")); // NOI18N
        fixedValueTextField.setToolTipText(resourceMap.getString("fixedValueTextField.toolTipText")); // NOI18N
        fixedValueTextField.setName("fixedValueTextField"); // NOI18N
        fixedValueTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fixedValueTextFieldActionPerformed(evt);
            }
        });
        fixedValueTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                fixedValueTextFieldKeyReleased(evt);
            }
        });

        fromDictionaryRadioButton.setName("fromDictionaryRadioButton"); // NOI18N
        fromDictionaryRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fromDictionaryRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputDefinitionPanelLayout = new javax.swing.GroupLayout(inputDefinitionPanel);
        inputDefinitionPanel.setLayout(inputDefinitionPanelLayout);
        inputDefinitionPanelLayout.setHorizontalGroup(
            inputDefinitionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputDefinitionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fixedValueRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fixedValueTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fromDictionaryRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fromDictionaryComboBox, 0, 286, Short.MAX_VALUE))
        );
        inputDefinitionPanelLayout.setVerticalGroup(
            inputDefinitionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputDefinitionPanelLayout.createSequentialGroup()
                .addGroup(inputDefinitionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fixedValueRadioButton)
                    .addComponent(fixedValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromDictionaryRadioButton)
                    .addComponent(fromDictionaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(inputDefinitionPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputDefinitionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setTopComponent(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane3.border.title"))); // NOI18N
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        previewTextPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        previewTextPane.setEditable(false);
        previewTextPane.setName("previewTextPane"); // NOI18N
        jScrollPane3.setViewportView(previewTextPane);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel2);

        testCaseNameTextField.setText(resourceMap.getString("testCaseNameTextField.text")); // NOI18N
        testCaseNameTextField.setName("testCaseNameTextField"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(testCaseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateTestCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateTestCaseButton)
                    .addComponent(jLabel1)
                    .addComponent(testCaseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void fixedValueRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fixedValueRadioButtonActionPerformed
        fixedValueTextField.setEnabled(true);
        fixedValueTextField.grabFocus();
        fromDictionaryComboBox.setEnabled(false);
    }//GEN-LAST:event_fixedValueRadioButtonActionPerformed

    private void fromDictionaryRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fromDictionaryRadioButtonActionPerformed
        fixedValueTextField.setEnabled(false);
        fromDictionaryComboBox.setEnabled(true);
        fromDictionaryComboBox.grabFocus();
    }//GEN-LAST:event_fromDictionaryRadioButtonActionPerformed

    private void requestMessageTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_requestMessageTreeValueChanged
        
        TreePath treePath = evt.getNewLeadSelectionPath();
        
        if(treePath == null)    
            return;
        
        if (treePath != null && !(((DefaultMutableTreeNode) treePath.getLastPathComponent()).getUserObject() instanceof WSFDataElement)) {
            disableInputdefinition();
            return;
        }

        WSFDataElement element = ((WSFDataElement) ((DefaultMutableTreeNode)treePath.getLastPathComponent()).getUserObject());
        if (element.isSimpleType() && element.isEnabled()) {

            if (element.getSource() != null) {

                WSFInputSource source = element.getSource();

                if (source.getInputSourceType() == WSFInputSource.INPUT_FROM_DICTIONARY) {
                    enableFromDictionaryInputDefinition();
                    DefaultComboBoxModel model = (DefaultComboBoxModel) fromDictionaryComboBox.getModel();

                    fromDictionaryComboBox.setSelectedIndex(WSFApplication.getApplication().getWSFConfiguration().getDictionaryIndex(source.toString()) + 1);
                    fixedValueTextField.setText("undefined");
                }
                if (source.getInputSourceType() == WSFInputSource.INPUT_FROM_FIXED_VALUE) {
                    enableFixedInputDefinition();
                    fixedValueTextField.setText(source.toString());
                    fromDictionaryComboBox.setSelectedIndex(0);
                }
            } else {
                fixedValueTextField.setText("undefined");
                enableFixedInputDefinition();
            }
        } else {
            disableInputdefinition();
        }
    }//GEN-LAST:event_requestMessageTreeValueChanged

    private void fixedValueTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fixedValueTextFieldActionPerformed
        setInputDefinition();
    }//GEN-LAST:event_fixedValueTextFieldActionPerformed

    private void fromDictionaryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_fromDictionaryComboBoxItemStateChanged
        setInputDefinition();
    }//GEN-LAST:event_fromDictionaryComboBoxItemStateChanged

    private void requestMessageTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_requestMessageTreeMouseClicked
        if(evt.getButton() == 3 ){
            TreePath path = requestMessageTree.getPathForLocation(evt.getX(), evt.getY());
            
            requestMessageTree.setSelectionPath(path);
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            WSFDataElement dataElement = (WSFDataElement)node.getUserObject();
            
            if(dataElement.getMinOccurs() == 0){
                if(dataElement.isEnabled()){
                    disableDataElementPopupMenuItem.setEnabled(true);
                    enableDataElementPopupMenuItem.setEnabled(false);
    //                requestMessageTreePopupMenu.removeAll();
    //                requestMessageTreePopupMenu.add(disableDataElementPopupMenuItem);
                }else{
                    disableDataElementPopupMenuItem.setEnabled(false);
                    enableDataElementPopupMenuItem.setEnabled(true);
    //                requestMessageTreePopupMenu.removeAll();
    //                requestMessageTreePopupMenu.add(enableDataElementPopupMenuItem);
                }
                requestMessageTreePopupMenu.show((JComponent)evt.getSource(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_requestMessageTreeMouseClicked

    private void fixedValueTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fixedValueTextFieldKeyReleased
        // TODO add your handling code here:
        setInputDefinition();
    }//GEN-LAST:event_fixedValueTextFieldKeyReleased

    private void setInputDefinition(){
        try {

            TreePath treePath = requestMessageTree.getSelectionPath();
            if(treePath == null)
                return;
            
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            WSFDataElement element = (WSFDataElement) treeNode.getUserObject();

            if (!element.isSimpleType()) {
                return;
            }

            WSFInputSource source = null;

            if (fixedValueTextField.isEnabled()) {
                String fixedValue = fixedValueTextField.getText().trim();
                if (fixedValue.equalsIgnoreCase("") || fixedValue.equalsIgnoreCase("undefined")) {
                    source = null;
                } else {
                    source = WSFInputSource.createSourceFromDefaultValue(fixedValue);
                }
            }

            if (fromDictionaryComboBox.isEnabled()) {
                if (fromDictionaryComboBox.getSelectedIndex() == -1 || fromDictionaryComboBox.getSelectedIndex() == 0) {
                    source = null;
                } else {
                    source = WSFInputSource.createInputSourceFromDictionary(((WSFDictionaryInfo) fromDictionaryComboBox.getSelectedItem()).getDictionary());
                }
            }
            element.setSource(source);

            previewTextPane.setText(operation.getPreview());

            ((DefaultTreeModel) requestMessageTree.getModel()).reload(treeNode);

            tryEnableGenerateTestCaseButton();
        } catch (FileNotFoundException ex) {
            WSFApplication.showMessage(ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        } catch (IOException ex) {
            WSFApplication.showMessage(ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        }catch (Exception ex) {
            WSFApplication.showMessage(ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        }
    }
    
    @Action
    public void enableDataElement(){
        try {
            TreePath treePath = requestMessageTree.getSelectionPath();
            if (treePath == null) {
                return;
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            WSFDataElement element = (WSFDataElement) treeNode.getUserObject();

            element.setEnabled(true);

            previewTextPane.setText(operation.getPreview());
        } catch (Exception ex) {
            WSFApplication.showMessage(ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        }
    }
    
    @Action 
    public void disableDataElement(){
        try {
            TreePath treePath = requestMessageTree.getSelectionPath();
            if (treePath == null) {
                return;
            }

            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            WSFDataElement element = (WSFDataElement) treeNode.getUserObject();

            element.setEnabled(false);

            previewTextPane.setText(operation.getPreview());
        } catch (Exception ex) {
            WSFApplication.showMessage(ex.getMessage());
            StringWriter sWriter = new StringWriter();
            ex.printStackTrace(new PrintWriter(sWriter));
            logger.error(sWriter.toString());
        }
    }

    private void enableFixedInputDefinition() {
        fixedValueRadioButton.setEnabled(true);
        fromDictionaryRadioButton.setEnabled(true);

        fixedValueRadioButton.setSelected(true);

        fixedValueTextField.setEnabled(true);
        fromDictionaryComboBox.setEnabled(false);
    }

    private void enableFromDictionaryInputDefinition() {
        fixedValueRadioButton.setEnabled(true);
        fromDictionaryRadioButton.setEnabled(true);

        fromDictionaryRadioButton.setSelected(true);

        fixedValueTextField.setEnabled(false);
        fromDictionaryComboBox.setEnabled(true);
    }

    private void disableInputdefinition() {
        fixedValueRadioButton.setEnabled(false);
        fromDictionaryRadioButton.setEnabled(false);

        fixedValueTextField.setEnabled(false);
        fromDictionaryComboBox.setEnabled(false);

        fixedValueTextField.setText("undefined");
        fromDictionaryComboBox.setSelectedIndex(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JMenuItem disableDataElementPopupMenuItem;
    private javax.swing.JMenuItem enableDataElementPopupMenuItem;
    private javax.swing.JRadioButton fixedValueRadioButton;
    private javax.swing.JTextField fixedValueTextField;
    private javax.swing.JComboBox fromDictionaryComboBox;
    private javax.swing.JRadioButton fromDictionaryRadioButton;
    private javax.swing.JButton generateTestCaseButton;
    private javax.swing.JPanel inputDefinitionPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextPane previewTextPane;
    private javax.swing.JTree requestMessageTree;
    private javax.swing.JPopupMenu requestMessageTreePopupMenu;
    private javax.swing.JTree responseMessageTree;
    private javax.swing.JTextField testCaseNameTextField;
    // End of variables declaration//GEN-END:variables


    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("dictionaries")) {
            setDictionaries();
        }
    }
    
    private void tryEnableGenerateTestCaseButton(){
        if(operation.isInputDefinitionComplete()){
            testCaseNameTextField.setEnabled(true);
            generateTestCaseButton.setEnabled(true);
            
            if(testCaseNameTextField.getText().trim().equals(""))
                testCaseNameTextField.setText(operation.getName().getLocalPart());
        }else{
            testCaseNameTextField.setEnabled(false);
            generateTestCaseButton.setEnabled(false);
        }
    }
    private void setDictionaries() {

        Vector dictionaries = new Vector();
        dictionaries.add("From Dictionary");
        for (WSFDictionaryInfo dictInfo : WSFApplication.getApplication().getWSFConfiguration().getDictionaries()) {
            dictionaries.add(dictInfo);
        }
        
        dictionaryComboBoxModel = new DefaultComboBoxModel(dictionaries);
        fromDictionaryComboBox.setModel(dictionaryComboBoxModel);
    }
    
    class MyCellRenderer extends DefaultTreeCellRenderer{
        
        
        final private Color colorForDisabledElement = new Color(193, 193, 193);
        final private Color colorForNormalElement = Color.WHITE;
        final private Color colorForSelection = new Color(134,171,217);
        
        public MyCellRenderer(){
            super();
            setOpaque(true);
        }
        
        @Override
        public Component getTreeCellRendererComponent(
                                                JTree tree, 
                                                Object value,
                                                boolean sel, 
                                                boolean expanded,
                                                boolean leaf,
                                                int row,
                                                boolean hasFocus){
                    
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
            TreeNode[] pathNodes = treeNode.getPath();
            TreePath treePath = new TreePath(pathNodes);
            
            Object userObject = treeNode.getUserObject();
            
            setText(value.toString());
            
            if(sel){
                this.setBackground(colorForSelection);
                return this;
            }
            
            boolean enabled = true;
            for(TreeNode node : pathNodes){
                
                userObject = ((DefaultMutableTreeNode)node).getUserObject();
                
                if(!(userObject instanceof WSFDataElement))
                    continue;
                
                if(!((WSFDataElement)(userObject)).isEnabled()){
                    enabled =false;
                    break;
                }
            }
            
            if(enabled){
                this.setBackground(colorForNormalElement);
            }else{
                this.setBackground(colorForDisabledElement);
            }
            
            return this;
            
        }
    }
}

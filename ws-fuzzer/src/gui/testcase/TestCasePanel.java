/*
 * TestCasePanel.java
 *
 * Created on February 12, 2008, 2:39 AM
 */

package gui.testcase;

import datamodel.WSFOperation;
import datamodel.WSFResult;
import datamodel.WSFTestCase;
import gui.WSFApplication;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeModel;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import utils.JTreeUtils;

/**
 *
 * @author  chang
 */
public class TestCasePanel extends javax.swing.JPanel {
    
    private static Logger logger = Logger.getLogger(TestCasePanel.class);
    
    private WSFTestCase testCase;
    private int currentIndex;
    DefaultListModel indexListModel;
    
    private HashMap<WSFTestCase, ExecuteTestCase> executeTestCases;
    
    /** Creates new form TestCasePanel */
    public TestCasePanel(WSFTestCase testCase) {
        this.testCase = testCase;
        initComponents();
        
        postInit();
        
        showTestCase();
    }
    
    private void postInit(){
        executeTestCases = new HashMap<WSFTestCase, ExecuteTestCase>();
        indexList.setCellRenderer(new MyCellRenderer());
    }
    
    public void setTestCase(WSFTestCase testCase){
        this.testCase = testCase;
        showTestCase();
    }
    
    private void showTestCase(){
        WSFOperation operation = testCase.getOperation();
        
        indexListModel = new DefaultListModel();
        for(int i=0; i<testCase.getInputDataVector().size(); i++){
            indexListModel.addElement(i);
        }
        indexList.setModel(indexListModel);
        
        currentIndex = indexList.getSelectedIndex();
        if(currentIndex == -1)
            currentIndex = 0;
        
        DefaultTreeModel requestTreeModel = new DefaultTreeModel(testCase.getRequestTreeNode(currentIndex));
        requestTree.setModel(requestTreeModel);
        JTreeUtils.expandAll(requestTree, null);
        
        showResult(currentIndex);
    }
    
    @Action
    public Task executeTestCase(){
        
        logger.info("executeTestCase: "+testCase.getProject().getName()+"/"+testCase.getName());
        
        ExecuteTestCase executeTestCase = new ExecuteTestCase(WSFApplication.getApplication(), testCase, this);
        executeTestCases.put(testCase, executeTestCase);
        return executeTestCase;
    }
    
    @Action
    public void stopExecuteTestCase(){
        logger.info("Try to stop the executing TestCase"+testCase.getName());
        executeTestCases.get(testCase).cancel(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        indexList = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        executeButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        addFilterButton = new javax.swing.JButton();
        filterComboBox = new javax.swing.JComboBox();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        requestTextArea = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        responseTextArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        clearFilerButton = new javax.swing.JButton();
        addedFilterComboBox = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        requestTree = new javax.swing.JTree();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(TestCasePanel.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(66, 0));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(0, 1));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        indexList.setFont(resourceMap.getFont("indexList.font")); // NOI18N
        indexList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "0", "10", "100", "1000", "10000", "100000", "1000000", "10000000", "99999999" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        indexList.setName("indexList"); // NOI18N
        indexList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                indexListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(indexList);

        jPanel1.add(jScrollPane1);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getActionMap(TestCasePanel.class, this);
        executeButton.setAction(actionMap.get("executeTestCase")); // NOI18N
        executeButton.setText(resourceMap.getString("executeButton.text")); // NOI18N
        executeButton.setFocusable(false);
        executeButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        executeButton.setName("executeButton"); // NOI18N
        executeButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(executeButton);

        stopButton.setAction(actionMap.get("stopExecuteTestCase")); // NOI18N
        stopButton.setText(resourceMap.getString("stopButton.text")); // NOI18N
        stopButton.setFocusable(false);
        stopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopButton.setName("stopButton"); // NOI18N
        stopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(stopButton);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        addFilterButton.setText(resourceMap.getString("addFilterButton.text")); // NOI18N
        addFilterButton.setName("addFilterButton"); // NOI18N

        filterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        filterComboBox.setName("filterComboBox"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(filterComboBox, 0, 201, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(addFilterButton)
                .addComponent(filterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane3.border.title"))); // NOI18N
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        requestTextArea.setColumns(20);
        requestTextArea.setRows(5);
        requestTextArea.setName("requestTextArea"); // NOI18N
        jScrollPane3.setViewportView(requestTextArea);

        jSplitPane1.setTopComponent(jScrollPane3);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane4.border.title"))); // NOI18N
        jScrollPane4.setName("jScrollPane4"); // NOI18N

        responseTextArea.setColumns(20);
        responseTextArea.setRows(5);
        responseTextArea.setName("responseTextArea"); // NOI18N
        jScrollPane4.setViewportView(responseTextArea);

        jSplitPane1.setRightComponent(jScrollPane4);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        clearFilerButton.setText(resourceMap.getString("clearFilerButton.text")); // NOI18N
        clearFilerButton.setName("clearFilerButton"); // NOI18N

        addedFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        addedFilterComboBox.setName("addedFilterComboBox"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(addedFilterComboBox, 0, 201, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearFilerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(clearFilerButton)
                .addComponent(addedFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jScrollPane2.border.title"))); // NOI18N
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        requestTree.setName("requestTree"); // NOI18N
        requestTree.setRootVisible(false);
        jScrollPane2.setViewportView(requestTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)))
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void indexListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_indexListValueChanged

        // TODO add your handling code here:
        int index = indexList.getSelectedIndex();

        if(index == currentIndex){
            return;
        }
        
        if(index == -1)
            index = 0;
        
        showResult(index);
    }//GEN-LAST:event_indexListValueChanged
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFilterButton;
    private javax.swing.JComboBox addedFilterComboBox;
    private javax.swing.JButton clearFilerButton;
    private javax.swing.JButton executeButton;
    private javax.swing.JComboBox filterComboBox;
    private javax.swing.JList indexList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea requestTextArea;
    private javax.swing.JTree requestTree;
    private javax.swing.JTextArea responseTextArea;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
    
    public void updateIndexList(int index){
        Object object = indexListModel.get(index);
        indexListModel.set(index, object);
    }
    
    public void showResult(int index){
        currentIndex = index;
        
        if(indexList.getSelectedIndex() != index)
            indexList.setSelectedIndex(index);
        
        
        DefaultTreeModel requestTreeModel = new DefaultTreeModel(testCase.getRequestTreeNode(index));
        requestTree.setModel(requestTreeModel);
        JTreeUtils.expandAll(requestTree, null);
        
        WSFResult result = testCase.getResults().get(index);
        if(result.getInputIndex() == -1) {
            ((TitledBorder)jScrollPane4.getBorder()).setTitle("Response (Raw)");
            
            requestTextArea.setText("");
            responseTextArea.setText("");
            
            return;
        }
        requestTextArea.setText(result.getOutRaw());
        responseTextArea.setText(result.getInRaw());
        ((TitledBorder)jScrollPane4.getBorder()).setTitle("Response (Raw) -- Response Time: " + result.getTime() + " ms");
        jScrollPane4.repaint();
    }
    
    class MyCellRenderer extends DefaultListCellRenderer{
        
        final private Color colorForExecutedResult = new Color(112,228,143);
        final private Color colorForNotExecutedResult = Color.WHITE;
        final private Color colorForFailed = new Color(226,108,114);
        final private Color colorForSelection = new Color(134,171,217);
        
        public MyCellRenderer(){
            super();
            setOpaque(true);
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean isFocused){
            
            ArrayList<WSFResult> results = testCase.getResults();
            WSFResult result = results.get(index);
            
            setText(object.toString());
            
            
            if(isSelected){
                setBackground( colorForSelection );
                return this;
            }
            
            if(result.getInputIndex() >= 0){
                setBackground( colorForExecutedResult );
            }else {
                setBackground( colorForNotExecutedResult );
            }
            
            return this;
        }
    }
    
}

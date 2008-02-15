/*
 * WSFApplicationView.java
 */

package gui;

import datamodel.WSFOperation;
import datamodel.WSFPort;
import datamodel.WSFProject;
import datamodel.WSFService;
import datamodel.WSFTestCase;
import gui.operation.OperationPanel;
import gui.options.OptionsDialog;
import gui.port.PortPanel;
import gui.project.NewProjectDialog;
import gui.project.ProjectPanel;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * The application's main frame.
 */
public class WSFApplicationView extends FrameView {

    private DefaultTreeModel projectsTreeModel;
    
    public WSFApplicationView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        
        postInit();
    }
    
    private void postInit(){
        ArrayList<WSFProject> projects = WSFApplication.getApplication().getProjects();
        projectsTreeModel = new DefaultTreeModel(createTreeNode(projects, null));
        projectsTree.setModel(projectsTreeModel);
        projectsTree.setRootVisible(false);
    }
    
    // object should be one of 
    // ArrayList<WSFProject>, WSFProject, WSFService, WSFPort, 
    // WSFOperation or WSFTestCase
    private DefaultMutableTreeNode createTreeNode(Object object, DefaultMutableTreeNode parent){
        
        DefaultMutableTreeNode node = null;
        
        if(object instanceof ArrayList){    // root node
            node = new DefaultMutableTreeNode(object);
            
            ArrayList<WSFProject> projects = (ArrayList<WSFProject>)object;
            for(WSFProject project : projects){
                createTreeNode(project, node);
            }
        }
        
        if(object instanceof WSFProject){
            node = new DefaultMutableTreeNode(object);
            
            ArrayList<WSFService> services = ((WSFProject)object).getServices();
            for(WSFService service : services){
                createTreeNode(service, node);
            }
            
            DefaultMutableTreeNode testCasesnode = new DefaultMutableTreeNode("Test Cases");
            node.add(testCasesnode);
            
            ArrayList<WSFTestCase> testCases = ((WSFProject)object).getTestCases();
            for(WSFTestCase testCase : testCases){
                createTreeNode(testCase, testCasesnode);
            }
        }
        
        if(object instanceof WSFService){
            node = new DefaultMutableTreeNode(object);
            
            ArrayList<WSFPort> ports = ((WSFService)object).getPorts();
            for(WSFPort port : ports){
                createTreeNode(port, node);
            }
        }
        
        if(object instanceof WSFPort){
            node = new DefaultMutableTreeNode(object);
            
            ArrayList<WSFOperation> operations = ((WSFPort)object).getOperations();
            for(WSFOperation operation : operations){
                createTreeNode(operation, node);
            }
        }
        
        if(object instanceof WSFOperation){
            node = new DefaultMutableTreeNode(object);
        }
        
         if(object instanceof WSFTestCase){
            node = new DefaultMutableTreeNode(object);
        }
        
        if(parent != null)
            parent.add(node);

        return node;
    }
    
    public void addNewProjectToTree(WSFProject project){
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)projectsTreeModel.getRoot();
        DefaultMutableTreeNode projectNode = createTreeNode(project, rootNode);
        projectsTreeModel.reload();
    }
    
    @Action
    public void showNewProjectDialog(){
        if(newProjectDialog == null){
            JFrame mainFrame = WSFApplication.getApplication().getMainFrame();
            newProjectDialog = new NewProjectDialog(mainFrame, true);
            newProjectDialog.setLocationRelativeTo(mainFrame);
        }
        WSFApplication.getApplication().show(newProjectDialog);
    }
    
    @Action
    public void showOptionsDialog(){
        if(optionsDialog == null){
            JFrame mainFrame = WSFApplication.getApplication().getMainFrame();
            optionsDialog = new OptionsDialog(mainFrame, true);
            optionsDialog.setLocationRelativeTo(mainFrame);
        }
        WSFApplication.getApplication().show(optionsDialog);
    }
    
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = WSFApplication.getApplication().getMainFrame();
            aboutBox = new WSFApplicationAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        WSFApplication.getApplication().show(aboutBox);
    }

    @Action
    public void deleteSelectedProject() throws IOException, XMLStreamException, Exception{
        if(projectsTree.getSelectionCount()==0)
            return;
        
        TreePath treePath = projectsTree.getSelectionPath();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)treePath.getPath()[0];
        DefaultMutableTreeNode projectNode = (DefaultMutableTreeNode)treePath.getPath()[1];
        rootNode.remove(projectNode);
        projectsTreeModel.reload();
        
        WSFProject project = (WSFProject)projectNode.getUserObject();
        WSFApplication.getApplication().deleteProject(project);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        newProjectButton = new javax.swing.JButton();
        optionButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        projectsTree = new javax.swing.JTree();
        displayPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        projectsTreePopupMenu = new javax.swing.JPopupMenu();
        deleteProjectPopupMenuItem = new javax.swing.JMenuItem();
        deleteTestCasePopupMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 550));

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getActionMap(WSFApplicationView.class, this);
        newProjectButton.setAction(actionMap.get("showNewProjectDialog")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(gui.WSFApplication.class).getContext().getResourceMap(WSFApplicationView.class);
        newProjectButton.setText(resourceMap.getString("newProjectButton.text")); // NOI18N
        newProjectButton.setFocusable(false);
        newProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newProjectButton.setName("newProjectButton"); // NOI18N
        newProjectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(newProjectButton);

        optionButton.setAction(actionMap.get("showOptionsDialog")); // NOI18N
        optionButton.setText(resourceMap.getString("optionButton.text")); // NOI18N
        optionButton.setFocusable(false);
        optionButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optionButton.setName("optionButton"); // NOI18N
        optionButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(optionButton);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setDividerSize(2);
        jSplitPane1.setResizeWeight(0.2);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(150, 382));

        projectsTree.setMinimumSize(new java.awt.Dimension(100, 0));
        projectsTree.setName("projectsTree"); // NOI18N
        projectsTree.setPreferredSize(new java.awt.Dimension(150, 76));
        projectsTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projectsTreeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(projectsTree);

        jSplitPane1.setLeftComponent(jScrollPane1);

        displayPanel.setName("displayPanel"); // NOI18N
        displayPanel.setLayout(new java.awt.GridLayout(0, 1));
        jSplitPane1.setRightComponent(displayPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 769, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 585, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        projectsTreePopupMenu.setName("projectsTreePopupMenu"); // NOI18N

        deleteProjectPopupMenuItem.setAction(actionMap.get("deleteSelectedProject")); // NOI18N
        deleteProjectPopupMenuItem.setText(resourceMap.getString("deleteProjectPopupMenuItem.text")); // NOI18N
        deleteProjectPopupMenuItem.setName("deleteProjectPopupMenuItem"); // NOI18N
        projectsTreePopupMenu.add(deleteProjectPopupMenuItem);

        deleteTestCasePopupMenuItem.setText(resourceMap.getString("deleteTestCasePopupMenuItem.text")); // NOI18N
        deleteTestCasePopupMenuItem.setName("deleteTestCasePopupMenuItem"); // NOI18N
        projectsTreePopupMenu.add(deleteTestCasePopupMenuItem);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void projectsTreeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_projectsTreeMouseClicked
        // TODO add your handling code here:
            
        if(evt.getButton()==3){
            
            TreePath path = projectsTree.getPathForLocation(evt.getX(), evt.getY());
            
            projectsTree.setSelectionPath(path);
            
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
            
            if(node.getUserObject() instanceof WSFTestCase){
                projectsTreePopupMenu.removeAll();
                projectsTreePopupMenu.add(deleteTestCasePopupMenuItem);
            }else{
                projectsTreePopupMenu.removeAll();
                projectsTreePopupMenu.add(deleteProjectPopupMenuItem);
            }
            projectsTreePopupMenu.show((JComponent)evt.getSource(), evt.getX(), evt.getY());
            
            return;
        }
        
        TreePath path = projectsTree.getSelectionPath();
            
        if( path!=null && path.getPathCount()==2 && evt.getButton()==1 ){
            
            WSFProject project = (WSFProject)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
            showProjectPanel(project);
            
        }
        
        if( path!=null && path.getPathCount()==4 && evt.getButton()==1 ){
            
            WSFPort port = (WSFPort)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
            showPortPanel(port);
//            port.print();
        }
        
        if( path!=null && path.getPathCount()==5 && evt.getButton()==1 ){
            
            WSFOperation operation = (WSFOperation)((DefaultMutableTreeNode)path.getLastPathComponent()).getUserObject();
            showOperationPanel(operation);
//            operation.print();
        }
    }//GEN-LAST:event_projectsTreeMouseClicked

    public void showOperationPanel(WSFOperation operation){
        if(operationPanel == null){
            operationPanel = new OperationPanel(operation);
        }else{
            operationPanel.setOperation(operation);
        }
        
        if(operationPanel.getParent() != displayPanel){
            displayPanel.removeAll();
            displayPanel.add(operationPanel);
        }
        
        displayPanel.revalidate();
        displayPanel.repaint();
    }
    
    public void showPortPanel(WSFPort port){
        if(portPanel == null){
            portPanel = new PortPanel(port);
        }else{
            portPanel.setPort(port);
        }
        
        if(portPanel.getParent() != displayPanel){
            displayPanel.removeAll();
            displayPanel.add(portPanel);
        }
        
        displayPanel.revalidate();
        displayPanel.repaint();
    }
    
    public void showProjectPanel(WSFProject project){
        if(projectPanel == null){
            projectPanel = new ProjectPanel(project);
        }else{
            projectPanel.setProject(project);
        }
        
        if(projectPanel.getParent() != displayPanel){
            displayPanel.removeAll();
            displayPanel.add(projectPanel);
        }
        
        displayPanel.revalidate();
        displayPanel.repaint();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem deleteProjectPopupMenuItem;
    private javax.swing.JMenuItem deleteTestCasePopupMenuItem;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newProjectButton;
    private javax.swing.JButton optionButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTree projectsTree;
    private javax.swing.JPopupMenu projectsTreePopupMenu;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private JDialog optionsDialog;
    private JDialog newProjectDialog;
    
    private ProjectPanel projectPanel;
    private PortPanel portPanel;
    private OperationPanel operationPanel;
}

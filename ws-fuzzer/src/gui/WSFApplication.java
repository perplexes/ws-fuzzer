/*
 * WSFApplication.java
 */

package gui;

import datamodel.WSFConfiguration;
import datamodel.WSFProject;
import datamodel.WSFProjectInfo;
import exceptions.UnSupportedException;
import exceptions.WSFProjectNotFoundException;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.wsdl.WSDLException;
import javax.xml.stream.XMLStreamException;
import org.apache.log4j.Level;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import utils.FileUtils;

import org.apache.log4j.Logger;


/**
 * The main class of the application.
 */
public class WSFApplication extends SingleFrameApplication {
    
    private final String configurationFilePath = "configuration.xml";
    private WSFConfiguration configuration;
    private ArrayList<WSFProject> projects;
    private Logger logger;
    
    public ArrayList<WSFProject> getProjects(){
        return projects;
    }
    
    public void deleteProject(WSFProject project) throws IOException, XMLStreamException, Exception{
        configuration.removeProject(project.getName());
        FileUtils.deleteDirectory(project.getPath());
        configuration.saveChanges();
    }
    
    @Override
    protected void initialize(String[] args){
        if(logger == null)
            logger = Logger.getLogger(WSFApplication.class);
        
        logger.info("Initialize");
        try {
            File configurationFile = new File(configurationFilePath);
            logger.info("Load configuration from File: " + configurationFile.getAbsolutePath());
            configuration = WSFConfiguration.createWSFConfigruation(configurationFile);
            projects = new ArrayList<WSFProject>() {
                @Override
                public String toString() {
                    return "WSFProjects";
                }
            };

            Vector<String> projectInfosToDelete = new Vector<String>();
            for (WSFProjectInfo projectInfo : configuration.getProjects()) {
                try {
                    WSFProject project = new WSFProject(projectInfo, configuration.getProjectsDirectory());
                    projects.add(project);
                } catch (WSDLException ex) {
                    logger.debug(ex);
                } catch (UnSupportedException ex) {
                    logger.debug(ex);
                } catch (WSFProjectNotFoundException ex) {
                    projectInfosToDelete.add(projectInfo.getName());
                    logger.debug(ex);
                } catch (XMLStreamException ex) {
                    logger.debug(ex);
                } catch (Exception ex) {
                    logger.debug(ex);
                }
            }

            if (projectInfosToDelete.size() != 0) {
                for (String name : projectInfosToDelete) {
                    configuration.removeProject(name);
                }
            }
        } catch (FileNotFoundException ex) {
                    logger.debug(ex);
        } catch (XMLStreamException ex) {
                    logger.debug(ex);
        }
          
    }
    
    public WSFConfiguration getWSFConfiguration(){
        return this.configuration;
    }
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override 
    protected void startup() {
        
        if(logger == null)
            logger = Logger.getLogger(WSFApplication.class);
        
        logger.info("Start GUI");
        WSFApplicationView appView = new WSFApplicationView(this);
        appView.getFrame().setPreferredSize(new Dimension(900,700));
        show(appView);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of WSFApplication
     */
    public static WSFApplication getApplication() {
        return Application.getInstance(WSFApplication.class);
    }
    
    @Override
    protected void shutdown(){
        
        logger.info("Shutdown the Application");
        for(WSFProject project : projects){
            try {
                project.saveTestCasesToFile();
            } catch (Exception ex) {
                StringWriter writer = new StringWriter();
                ex.printStackTrace(new PrintWriter(writer));
                logger.debug(writer.toString());
            }
        }
    }

    public static void showMessage(String msg) {
        JOptionPane.showMessageDialog(getApplication().getMainFrame(), msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        
        String log4jFile = System.getProperty("log4j.configuration");
        
        File file = new File(log4jFile);
        
        System.setProperty("log4j.configuration", file.toURI().toString());
        
        Logger logger = Logger.getLogger(WSFApplication.class);
        
        Logger.getLogger("org.apache").setLevel(Level.WARN);
        Logger.getLogger("org.apache.axiom.om.util.StAXUtils").setLevel(Level.WARN);
        logger.info("\n\n");
        logger.info("*************************************************");
        logger.info("***                WS - FUZZER                ***");
        logger.info("*************************************************");
        logger.info("Launch the application");
        launch(WSFApplication.class, args);
    }
    
}

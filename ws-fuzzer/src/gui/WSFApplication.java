/*
 * WSFApplication.java
 */

package gui;

import datamodel.WSFConfiguration;
import datamodel.WSFProject;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class WSFApplication extends SingleFrameApplication {

    
    private final String configurationFilePath = "./configuration.xml";
    private WSFConfiguration configuration;
    private ArrayList<WSFProject> projects;
    
    public ArrayList<WSFProject> getProjects(){
        return projects;
    }
    
    protected void initialize(String[] args){
        try {
            File configurationFile = new File(configurationFilePath);
            configuration = WSFConfiguration.createWSFConfigruation(configurationFile);
            
            projects = new ArrayList<WSFProject>();
                    
        } catch (FileNotFoundException ex) {
            
//            Logger.getLogger(WSFApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XMLStreamException ex) {
            
//            Logger.getLogger(WSFApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public WSFConfiguration getWSFConfiguration(){
        return this.configuration;
    }
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        WSFApplicationView appView = new WSFApplicationView(this);
        
        appView.getFrame().setPreferredSize(new Dimension(800,600));
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

    
    
    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(WSFApplication.class, args);
    }
    
}

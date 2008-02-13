/*
 * WSFApplication.java
 */

package gui;

import java.awt.Dimension;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

/**
 * The main class of the application.
 */
public class WSFApplication extends SingleFrameApplication {

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

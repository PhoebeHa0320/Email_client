/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email_client.global.macOS;

import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Desktop;
import javax.swing.JFrame;

/**
 *
 * @author notmiyouji
 */
public class mainForm {
    
    public void desktopMac() {
        Desktop desktop = Desktop.getDesktop();
        if( desktop.isSupported( Desktop.Action.APP_ABOUT ) ) {
            desktop.setAboutHandler( e -> {
                // show about dialog
            } );
        }
        if( desktop.isSupported( Desktop.Action.APP_PREFERENCES ) ) {
            desktop.setPreferencesHandler( e -> {
                // show preferences dialog
            } );
        }         
    }
    
    public static void isMacOS() {
        if( SystemInfo.isMacOS ) {
            System.setProperty( "apple.laf.useScreenMenuBar", "true" ); //menubar lên Screen Menu Bar
            System.setProperty( "apple.awt.application.name", "EmailClient" ); //tên phần mềm lên Screen Menu Bar               
        }
    }
    
    public static void isMacFullWindowContentSupported(JFrame frame) {
        if( SystemInfo.isMacFullWindowContentSupported ) { //macOS ONLY                              
            frame.getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true ); //transparent titlebar
            frame.getRootPane().putClientProperty( "apple.awt.fullWindowContent", true );        
        }
        frame.getRootPane().putClientProperty( "apple.awt.windowTitleVisible", false ); //ẩn tên phần mềm trên titlebar
        frame.getRootPane().putClientProperty( "apple.awt.fullscreenable", true ); //fullscreen mode
    }
    
}

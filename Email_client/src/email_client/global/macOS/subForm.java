/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package email_client.global.macOS;

import com.formdev.flatlaf.util.SystemInfo;
import javax.swing.JFrame;

/**
 *
 * @author notmiyouji
 */
public class subForm {
    public static void isMacOS() {
        if( SystemInfo.isMacOS ) {
            System.setProperty( "apple.laf.useScreenMenuBar", "true" ); //menubar lên Screen Menu Bar
            //System.setProperty( "apple.awt.application.name", "Thông tin phần mềm" ); //tên phần mềm lên Screen Menu Bar               
        }
    }
    
    public static void isMacFullWindowContentSupported(JFrame frame) {
        if( SystemInfo.isMacFullWindowContentSupported ) { //macOS ONLY
            frame.getRootPane().putClientProperty( "apple.awt.transparentTitleBar", true ); //transparent titlebar
            //frame.getRootPane().putClientProperty( "apple.awt.fullscreenable", true ); //fullscreen mode
        }
    }
}

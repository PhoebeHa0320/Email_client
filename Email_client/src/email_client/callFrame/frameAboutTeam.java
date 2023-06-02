package email_client.callFrame;

import email_client.anotherAboutTeam;
import email_client.global.LookandFeel;
import email_client.global.macOS.subForm;

public class frameAboutTeam {

    public static void anotherframe() {
        LookandFeel.setTheme();
        subForm.isMacOS();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            anotherAboutTeam frame = new anotherAboutTeam();
            subForm.isMacFullWindowContentSupported(frame);
            frame.setVisible(true);
            frame.setTitle("Thông tin nhóm");
            frame.setResizable(false);
        });
    }
    
}

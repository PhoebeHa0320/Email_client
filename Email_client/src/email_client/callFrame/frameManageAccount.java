package email_client.callFrame;

import email_client.global.LookandFeel;
import email_client.global.macOS.subForm;
import email_client.manageAccount;

public class frameManageAccount {
    public static void callframe() {
        LookandFeel.setTheme();
        subForm.isMacOS();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            manageAccount frame = new manageAccount();           
            subForm.isMacFullWindowContentSupported(frame);
            frame.setVisible(true);
            frame.setTitle("Quản lý tài khoản");
            frame.setResizable(false);
        });
    }
    
}

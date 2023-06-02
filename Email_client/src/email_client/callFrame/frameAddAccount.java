package email_client.callFrame;

import email_client.addAccount;
import email_client.global.LookandFeel;
import email_client.global.macOS.subForm;

public class frameAddAccount {
    public static void callframe() {
        LookandFeel.setTheme();
        subForm.isMacOS();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            addAccount frame = new email_client.addAccount();           
            subForm.isMacFullWindowContentSupported(frame);
            frame.setVisible(true);
            frame.setTitle("Thêm tài khoản");
            frame.setResizable(false);
        });
    }
    
}

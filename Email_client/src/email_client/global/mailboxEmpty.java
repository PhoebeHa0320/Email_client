package email_client.global;

import javax.swing.JOptionPane;

public class mailboxEmpty {
    public void callNotify() {
        JOptionPane.showMessageDialog(null, "Hộp thư trống!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }   
}

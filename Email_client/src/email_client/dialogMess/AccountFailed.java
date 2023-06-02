package email_client.dialogMess;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class AccountFailed {

    public static void NotifyMesseage() {
        //Thông báo về các tài khoản xác thực hai bước
        EventQueue.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(AccountFailed.class.getResource("/icon/icons8-2fa-64.png"));
                       
            String text = "Kiểm tra thất bại. "
                    + "\n1. Tài khoản email của bạn có tồn tại không?"
                    + "\n2. Cấu hình tài khoản email của bạn có đúng không?"
                    + "\n3. Bạn có kích hoạt xác thực hai bước cho tài khoản không?";
                    
            JOptionPane.showMessageDialog(null, text, "Thông báo", JOptionPane.INFORMATION_MESSAGE, icon);
        });
    }
}

package email_client.dialogMess;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class TwoFANotify {

    public static void NotifyMesseage() {
        //Thông báo về các tài khoản xác thực hai bước
        EventQueue.invokeLater(() -> {        
            ImageIcon icon = new ImageIcon(TwoFANotify.class.getResource("/icon/icons8-2fa-64.png"));
            
            
            String text = "Với các tài khoản xác thực hai bước, bạn cần tạo mật khẩu ứng dụng."
                    + "\nCác trang sau sẽ hỗ trợ bạn tạo mật khẩu với loại tài khoản "
                    + "\n1. Google: https://support.google.com/accounts/answer/185833"
                    + "\n2. Outlook: https://bom.so/Z2mG1r"
                    + "\n3. Yahoo Mail: https://vn.trogiup.yahoo.com/kb/SLN15241.html"
                    + "\nChú ý: Vui lòng không chia sẻ mật khẩu một lần cho bất kỳ ai!";
                    
            JOptionPane.showMessageDialog(null, text, "Thông báo", JOptionPane.INFORMATION_MESSAGE, icon);
        });
    }
}

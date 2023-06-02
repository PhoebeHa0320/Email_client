package email_client.dialogMess;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ExplainCC {
    public static void NotifyMesseage() {
        //Thông báo về các tài khoản xác thực hai bước
        EventQueue.invokeLater(() -> {          
            ImageIcon icon = new ImageIcon(TwoFANotify.class.getResource("/icon/icons8-email-64.png"));
            
            
            String text = "CC là từ viết tắt Carbon Coppy có nghĩa là tạ ra các bản sao khi bạn chọn CC email sẽ gởi thêm một bản sao email nữa cho người khác. "
                    + "\nKhi bạn dùng CC gửi email đến nhiều người dùng lúc, những người này có thể xem danh sách người các người nhận email. "
                    + "\nLựa chọn này có thể bỏ qua.";
                    
            JOptionPane.showMessageDialog(null, text, "Thông báo", JOptionPane.INFORMATION_MESSAGE, icon);
        });
    }
}

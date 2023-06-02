package email_client.dialogMess;

import javax.swing.JOptionPane;

public class loadContentFailed {
    
    public boolean callNotify(int rowSelected) {
        int reply = JOptionPane.showConfirmDialog(null, "Tải thất bại!\n"
                            + "Bạn có muốn tải dạng Plain Text?", "Thông báo", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {           
            return true;
        }
        else {
            JOptionPane.showMessageDialog(null, "Bạn vẫn có thể tải dạng plain text bằng cách nhấn nút\n"
                    + "Plain Text trên màn hình", "Thông báo", JOptionPane.INFORMATION_MESSAGE);           
            return false;
        }
    }
}

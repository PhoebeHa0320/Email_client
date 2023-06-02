package email_client.utils.yahoo;

import javax.swing.JOptionPane;

//Java Mail không tìm đc folder thư rác của yahoo mail
public class YahooSpam {
    public void notifyMesseage() {
        JOptionPane.showMessageDialog(null, "Spam email Yahoo chưa hỗ trợ\n"
                                    + "Vui lòng thử lại sau.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
}

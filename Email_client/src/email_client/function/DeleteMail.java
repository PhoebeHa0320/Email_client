package email_client.function;

import email_client.global.getSessionIMAPS;
import java.io.IOException;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;

public class DeleteMail {
    getSessionIMAPS getSession = new getSessionIMAPS();
    String storeType = "imaps";
    public void deleteEmail(int rowSelected,String imap, String foldername, String user, String password, JEditorPane messagePane ) 
            throws NoSuchProviderException, MessagingException, IOException {
             
        Session emailSession = getSession.getSession(imap, storeType, user, password);               
        Store store =  emailSession.getStore(storeType);
        store.connect();
        Folder emailFolder = store.getFolder(foldername);
        emailFolder.open(Folder.READ_WRITE);

        Message[] messages = emailFolder.getMessages();
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fetchProfile);
        Message msg = messages[rowSelected];

        int reply = JOptionPane.showConfirmDialog(null, "Bạn muốn xoá Mail này?\n"
                + "Hãy cẩn thận, thao tác này sẽ xoá vĩnh viễn!", "Thông báo", 
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (reply == JOptionPane.YES_OPTION) {
            msg.setFlag(Flags.Flag.DELETED, true);
            JOptionPane.showMessageDialog(null, "Mai đã bị xoá!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Huỷ bỏ từ người dùng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }           
        emailFolder.close(true);
        store.close();            
    }
    
}

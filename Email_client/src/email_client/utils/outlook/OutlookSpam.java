package email_client.utils.outlook;

import email_client.global.folderMailName;
import email_client.global.getSessionIMAPS;
import email_client.global.mailboxEmpty;
import email_client.utils.MailList;
import email_client.utils.MailListModel;
import java.util.LinkedList;
import java.util.List;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.swing.table.DefaultTableModel;


public class OutlookSpam {
    //<editor-fold defaultstate="collapsed" desc="Global Function">
    getSessionIMAPS getSession = new getSessionIMAPS();
    mailboxEmpty empty = new mailboxEmpty();
    folderMailName foldername = new folderMailName();
    public final DefaultTableModel tableModel = new DefaultTableModel();
    MailListModel mailListModel;
    List<MailList> Data = new  LinkedList<>();
    //</editor-fold>
    public MailListModel startFetch(String imap, String storeType, String user, String password) 
            throws NoSuchProviderException, MessagingException {
        
        Data.clear(); //clear old linked list             
        Session emailSession = getSession.getSession(imap, storeType, user, password);    
        Store store =  emailSession.getStore(storeType);
        store.connect();
        Folder emailFolder = store.getFolder(foldername.getSpamOutlook());
        emailFolder.open(Folder.READ_WRITE);

        Message[] messages = emailFolder.getMessages();
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fetchProfile);
        if (messages.length == 0)
        {
            String[] headers = {"Người Gửi", "Tiêu Đề",  "Thời Gian"};
            Data.add(new MailList("Hộp thư trống", "Hộp thư trống", "Hộp thư trống"));               
            mailListModel = new MailListModel(headers, Data);
            empty.callNotify();
        }
        else {
            for(int i = 0, n = messages.length; i < n; i++) 
            {     
                Message message = messages[i];
                String from = "";
                InternetAddress[] addresses  =(InternetAddress[]) message.getFrom();
                for(InternetAddress address:addresses)
                {
                    from+=address.getAddress();
                }
                String[] headers = {"Người Gửi", "Tiêu Đề",  "Thời Gian"};
                Data.add(new MailList(from,message.getSubject(),message.getSentDate().toString()));               
                mailListModel = new MailListModel(headers, Data);            
            }
        }
        store.close();
        return mailListModel;
    }   
}

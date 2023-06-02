package email_client.utils;

import com.sun.mail.util.BASE64DecoderStream;
import email_client.global.getSessionIMAPS;
import email_client.global.mailboxEmpty;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.SearchTerm;
import javax.swing.JEditorPane;
import javax.swing.table.DefaultTableModel;

public class SearchMail {
    //<editor-fold defaultstate="collapsed" desc="Global Function">
    mailboxEmpty empty = new mailboxEmpty();
    getSessionIMAPS getSession = new getSessionIMAPS();
    public final DefaultTableModel tableModel = new DefaultTableModel();        
    MailListModel mailListModel;
    List<MailList> Data = new  LinkedList<>();
    
    Store store;
    Folder emailFolder;
    //</editor-fold>
    
    public Message[] getmessages (String imap, String storeType, String user, String password, 
            String MailSearchInput, String selection, String folder) throws MessagingException {
        Session emailSession = getSession.getSession(imap, storeType, user, password);               
        store =  emailSession.getStore(storeType);
        store.connect();
        emailFolder = store.getFolder(folder);
        emailFolder.open(Folder.READ_WRITE);
       

        SearchTerm searchCondition = new SearchTerm() {
            @Override
            public boolean match(Message message) {
                try {
                    switch(selection) {
                        case "Theo người gửi" -> {
                            Address[] fromAddress = message.getFrom();
                            if (fromAddress != null && fromAddress.length > 0) {
                                if (fromAddress[0].toString().contains(MailSearchInput)) {
                                    return true;
                                }
                            }   
                        }
                        case "Theo ngày gửi" -> {
                            if (message.getSentDate().after(new SimpleDateFormat("dd/MM/yyyy").parse(MailSearchInput))) {
                                return true;
                            }
                        }
                    }
                    
                    
                } catch (MessagingException ex) {
                } catch (ParseException ex) {
                    Logger.getLogger(SearchMail.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };
        Message[] messages = emailFolder.search(searchCondition);
        FetchProfile fetchProfile = new FetchProfile();
        fetchProfile.add(FetchProfile.Item.ENVELOPE);
        emailFolder.fetch(messages, fetchProfile);  
               
        return messages;
    }
    
    public MailListModel startFetch(String imap, String storeType, String user, String password, 
            String MailSearchInput, String selection, String folder) throws MessagingException 
             {
        Data.clear(); //clear old linked list                  
        Message[] messages = this.getmessages (imap, storeType, user, password, 
            MailSearchInput, selection, folder);
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
                mailListModel.setRowCount(0);
            }
        }
        emailFolder.close(false);
        store.close();
        
        return mailListModel;
    }
    
    public void fetchResultContent (String imap, String storeType, String user, String password, 
            String MailSearchInput, String selection, String folder, int rowSelected, JEditorPane messagePane) throws MessagingException, IOException {
        
        Message[] messages = this.getmessages (imap, storeType, user, password, 
            MailSearchInput, selection, folder);
        Message msg = messages[rowSelected];

        String contentType = msg.getContentType();
        String messageContent = "";           

        if (contentType.contains("multipart")) {
            Multipart multiPart = (Multipart) msg.getContent();
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                messageContent = part.getContent().toString();
            }
        }
        else if (contentType.contains("multipart")|| contentType.contains("text/html")) {

            BASE64DecoderStream content =(BASE64DecoderStream) msg.getContent();
            if (content != null) {
                messageContent = content.toString();
           }     
        }

        System.out.println("Nội dung: " + messageContent);
        messagePane.setContentType("text/html");
        messagePane.setText(messageContent);
        emailFolder.close(false);
        store.close();
    }
}

package email_client.utils;

import email_client.global.getSessionIMAPS;
import java.io.IOException;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JEditorPane;

public class FetchContentPlainText {
       
    getSessionIMAPS getSession = new getSessionIMAPS();
    String storeType = "imaps"; 
    
    public void plainTextShow(int rowSelected,String imap, String foldername, String user, String password, JEditorPane messagePane) 
            throws NoSuchProviderException, MessagingException, IOException, Exception {
             
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

        Object content = msg.getContent();
        messagePane.setContentType("text/html");
        if (content instanceof Multipart) {
            StringBuilder messageContent = new StringBuilder();
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                Part part = (Part) multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    messageContent.append(part.getContent().toString());
                }
            }
            System.out.println("Nội dung: " + messageContent.toString());                
            messagePane.setText(messageContent.toString());              
        } else {
            System.out.println("Nội dung: " + content.toString());               
            messagePane.setText(content.toString());                
        }

        emailFolder.close(false);
        store.close();

    }
    public String getMessageContent(Message message)
    throws Exception {
        Object content = message.getContent();
        if (content instanceof Multipart) {
            StringBuilder messageContent = new StringBuilder();
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                Part part = (Part) multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    messageContent.append(part.getContent().toString());
                }
            }
            return messageContent.toString();
        } else {
            return content.toString();
        }
    }      
}
